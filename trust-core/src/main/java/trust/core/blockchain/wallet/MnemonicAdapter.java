package trust.core.blockchain.wallet;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.wallet.DeterministicSeed;
import org.web3j.crypto.ECKeyPair;

import java.security.SecureRandom;
import java.util.Arrays;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import trust.core.blockchain.Slip;
import trust.core.blockchain.Slip44;
import trust.core.blockchain.account.AccountFactory;
import trust.core.blockchain.signer.Signer;
import trust.core.entity.Account;
import trust.core.entity.Wallet;
import trust.core.entity.WalletDescriptor;
import trust.core.util.TrustDeterministicKeyChain;

import static org.web3j.crypto.Hash.sha256;

public class MnemonicAdapter implements WalletAdapter {
    private static final SecureRandom secureRandom = new SecureRandom();

    private final String[] wordList;
    private final Signer[] signers;
    private final AccountFactory[] accountFactories;

    public MnemonicAdapter(String[] wordList, Signer[] signers, AccountFactory[] accountFactories) {
        this.wordList = wordList;
        this.signers = signers;
        this.accountFactories = accountFactories;
    }

    @Override
    public Single<WalletDescriptor> create(String password, Slip44[] coins) {
        return initialEntropy().flatMap(this::generateMnemonic)
                .flatMap(mnemonic -> generateDescriptor(mnemonic, password, coins));
    }

    @Override
    public Single<WalletDescriptor> importWallet(byte[] data, String password, String newPassword, Slip44[] coins) {
        return generateDescriptor(new String(data), newPassword, coins);
    }

    @Override
    public Single<byte[]> sign(byte[] data, String password, Account account, byte[] message, boolean isHashed) {
        return getECKey(data, password, account)
                .flatMap(ecKeyPair -> getHelper(signers, account.coin).sign(ecKeyPair, message, isHashed));
    }

    @Override
    public Completable delete(byte[] data, String password) {
        return Completable.complete();
    }

    @Override
    public int getType() {
        return Wallet.MNEMONIC;
    }

    private Single<WalletDescriptor> generateDescriptor(String mnemonic, String password, Slip44[] coins) {
        return getKeyChain(mnemonic)
                .flatMapObservable(keyChain -> generateAccounts(keyChain, coins))
                .toList()
                .map(accounts -> {
                    return accounts.toArray(new Account[accounts.size()]);
                })
                .map(accounts -> new WalletDescriptor(mnemonic.getBytes("UTF-8"), accounts));
    }

    private Single<byte[]> initialEntropy() {
        return Single.fromCallable(() -> {
            byte[] initialEntropy = new byte[16];
            secureRandom.nextBytes(initialEntropy);
            return initialEntropy;
        });
    }

    private Single<String> generateMnemonic(byte[] initialEntropy) {
        return validateInitialEntropy(initialEntropy)
            .andThen(Single.fromCallable(() -> {
                int ent = initialEntropy.length * 8;
                int checksumLength = ent / 32;

                byte checksum = calculateChecksum(initialEntropy);
                boolean[] bits = convertToBits(initialEntropy, checksum);

                int iterations = (ent + checksumLength) / 11;
                StringBuilder mnemonicBuilder = new StringBuilder();
                for (int i = 0; i < iterations; i++) {
                    int index = toInt(nextElevenBits(bits, i));
                    mnemonicBuilder.append(wordList[index]);

                    boolean notLastIteration = i < iterations - 1;
                    if (notLastIteration) {
                        mnemonicBuilder.append(" ");
                    }
                }

                return mnemonicBuilder.toString();
            }));
    }

    private static Single<TrustDeterministicKeyChain> getKeyChain(String mnemonic) {
        return Single.fromCallable(() -> {
            final DeterministicSeed seed = new DeterministicSeed(mnemonic, null, "", 0);
            seed.check();
            return new TrustDeterministicKeyChain(seed);
        });
    }

    private static Single<ECKey> getKeyChain(TrustDeterministicKeyChain keyChain, Account account) {
        return Single.fromCallable(() -> keyChain.getKeyByPath(account.coin));
    }

    private static Single<ECKey> getECKey(byte[] data, String password, Account account) {
        return Single.just(new String(data))
                .flatMap(MnemonicAdapter::getKeyChain)
                .flatMap(deterministicKeyChain -> getKeyChain(deterministicKeyChain, account));
    }

    private static Single<ECKeyPair> getECKeyPair(byte[] data, String password, Account account) {
        return getECKey(data, password, account)
                .map(ecKey -> ECKeyPair.create(ecKey.getPrivKey()));
    }

    private Observable<Account> generateAccounts(TrustDeterministicKeyChain keyChain, Slip44[] coins) {
        return Observable.fromArray(coins)
                .flatMap(coin -> {
                    DeterministicKey deterministicKey = keyChain.getKeyByPath(coin);
                    return getHelper(accountFactories, coin).createAccount(deterministicKey, coin);
                })
                .subscribeOn(Schedulers.io());
    }

    private <T extends Slip> T getHelper(T[] helpers, Slip44 coin) {
        for (T helper : helpers) {
            if (coin.available(helper.getMaintainedCoins())) {
                return helper;
            }
        }
        return null;
    }

    private static byte calculateChecksum(byte[] initialEntropy) {
        int ent = initialEntropy.length * 8;
        byte mask = (byte) (0xff << 8 - ent / 32);
        byte[] bytes = sha256(initialEntropy);

        return (byte) (bytes[0] & mask);
    }

    private static boolean[] convertToBits(byte[] initialEntropy, byte checksum) {
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        int totalLength = ent + checksumLength;
        boolean[] bits = new boolean[totalLength];

        for (int i = 0; i < initialEntropy.length; i++) {
            for (int j = 0; j < 8; j++) {
                byte b = initialEntropy[i];
                bits[8 * i + j] = toBit(b, j);
            }
        }

        for (int i = 0; i < checksumLength; i++) {
            bits[ent + i] = toBit(checksum, i);
        }

        return bits;
    }

    private static boolean toBit(byte value, int index) {
        return ((value >>> (7 - index)) & 1) > 0;
    }

    private static int toInt(boolean[] bits) {
        int value = 0;
        for (int i = 0; i < bits.length; i++) {
            boolean isSet = bits[i];
            if (isSet)  {
                value += 1 << bits.length - i - 1;
            }
        }

        return value;
    }

    private static boolean[] nextElevenBits(boolean[] bits, int i) {
        int from = i * 11;
        int to = from + 11;
        return Arrays.copyOfRange(bits, from, to);
    }

    private static Completable validateInitialEntropy(byte[] initialEntropy) {
        return Completable.fromAction(() -> {
            if (initialEntropy == null) {
                throw new IllegalArgumentException("Initial entropy is required");
            }

            int ent = initialEntropy.length * 8;
            if (ent < 128 || ent > 256 || ent % 32 != 0) {
                throw new IllegalArgumentException("The allowed size of ENT is 128-256 bits of "
                        + "multiples of 32");
            }
        });
    }
}
