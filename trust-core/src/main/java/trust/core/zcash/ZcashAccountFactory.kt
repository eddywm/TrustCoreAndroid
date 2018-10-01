package trust.core.zcash

import io.reactivex.Observable
import org.bitcoinj.core.ECKey
import org.web3j.crypto.ECKeyPair
import trust.core.blockchain.Slip44
import trust.core.blockchain.account.AccountFactory
import trust.core.entity.Account

class ZcashAccountFactory : AccountFactory {

    override fun createAccount(keyChain: ECKey?, coin: Slip44?): Observable<Account> {
        return Observable.fromCallable {
            val ecKeyPair = ECKeyPair.create(keyChain!!.privKey)
            val address = ZcashUtil.getZcashAddress(ecKeyPair)
            Account(ZcashAddress(address), coin)
        }
    }

    override fun getMaintainedCoins(): Array<Slip44> {
        return arrayOf(Slip44.ZEC)
    }

}
