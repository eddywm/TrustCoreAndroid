package trust.core.blockchain.wallet;

import io.reactivex.Completable;
import io.reactivex.Single;
import trust.core.blockchain.Slip44;
import trust.core.entity.Account;
import trust.core.entity.WalletDescriptor;

public interface WalletAdapter {
    Single<WalletDescriptor> create(String password, Slip44[] coins);
    Single<WalletDescriptor> importWallet(byte[] data, String password, String newPassword, Slip44[] coins);

    Single<byte[]> sign(byte[] data, String password, Account account, byte[] message, boolean isHashed);

    Completable delete(byte[] data, String password);

    int getType();
}
