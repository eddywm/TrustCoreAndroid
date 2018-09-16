package trust.core.blockchain.account;


import org.bitcoinj.core.ECKey;

import io.reactivex.Observable;
import trust.core.blockchain.Slip;
import trust.core.blockchain.Slip44;
import trust.core.entity.Account;

public interface AccountFactory extends Slip {
    Observable<Account> createAccount(ECKey keyChain, Slip44 coin);
}
