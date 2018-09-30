package trust.core.zcash;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import io.reactivex.Observable;
import trust.core.blockchain.Slip44;
import trust.core.blockchain.account.AccountFactory;
import trust.core.entity.Account;
import trust.core.util.Base58Check;

public class ZcashAccountFactory implements AccountFactory {

    public ECKey ecKey;

    public String passphrase; // Will be used to store the private key encrypted

    public ZcashAccountFactory() {
        if (ecKey == null) {
            ecKey = new ECKey(new SecureRandom());
        }
    }

    @Override
    public Observable<Account> createAccount(ECKey keyChain, Slip44 coin) {
        return Observable.fromCallable(() -> {

            String address = getZcashAddress(keyChain);

            // TODO: Before returning save also keys on disk

            return new Account(new ZcashAddress(address), coin);
        });
    }

    // P2PKH Zcash t-address.
    //  Base58Check([0x1C, 0xB8] || RIPEMD-160(SHA-256(PUBKEY)))
    public String getZcashAddress(ECKey keyChain) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] pubKey = keyChain.getPubKey();

        byte[] hashedPubKey = Utils.sha256hash160(pubKey);

        output.write(0x1c);
        output.write(0xb8);
        output.write(hashedPubKey);

        byte[] appended = output.toByteArray();

        return Base58Check.bytesToBase58(appended);
    }

    @Override
    public Slip44[] getMaintainedCoins() {
        return new Slip44[6];
    }
}
