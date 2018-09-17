package trust.core.blockchain.account;

import org.bitcoinj.core.ECKey;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import io.reactivex.Observable;
import trust.core.blockchain.Slip44;
import trust.core.entity.Account;
import trust.core.entity.address.EthereumAddress;

public class EthereumAccountFactory implements AccountFactory {

    private static final Slip44[] servesCoin = new Slip44[] {
            Slip44.ETH,
            Slip44.ETC,
            Slip44.CLO,
            Slip44.POA,
            Slip44.GO,
            Slip44.VET,
    };

    @Override
    public Observable<Account> createAccount(ECKey keyChain, Slip44 coin) {
        return Observable.fromCallable(() -> {
            ECKeyPair ecKeyPair = ECKeyPair.create(keyChain.getPrivKey());
            String address = Keys.getAddress(ecKeyPair);
            return new Account(new EthereumAddress(address), coin);
        });
    }

    @Override
    public Slip44[] getMaintainedCoins() {
        return servesCoin;
    }
}
