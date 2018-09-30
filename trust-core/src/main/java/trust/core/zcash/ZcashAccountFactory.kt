package trust.core.zcash

import io.reactivex.Observable
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Utils
import trust.core.blockchain.Slip44
import trust.core.blockchain.account.AccountFactory
import trust.core.entity.Account
import trust.core.util.Base58Check
import java.io.ByteArrayOutputStream
import java.security.SecureRandom

class ZcashAccountFactory : AccountFactory {

    var ecKey: ECKey? = null
    var privateKey = String()
    var publicKey = String()

    init {
        if (ecKey == null) {
            ecKey = ECKey(SecureRandom())
            privateKey = ecKey!!.privateKeyAsHex
            publicKey = ecKey!!.publicKeyAsHex
        }
    }

    override fun createAccount(keyChain: ECKey, coin: Slip44): Observable<Account> {
        return Observable.fromCallable {
            val address = getZcashAddress(keyChain)
            Account(ZcashAddress(address), coin)
        }
    }

    /*
     Address format : P2PKH Zcash t-address.
     Base58Check([0x1C, 0xB8] || RIPEMD-160(SHA-256(PUBKEY)))
    */
    fun getZcashAddress(keyChain: ECKey): String {

        val output = ByteArrayOutputStream()

        val pubKey = keyChain.pubKey

        val hashedPubKey = Utils.sha256hash160(pubKey)

        output.write(0x1c)
        output.write(0xb8)
        output.write(hashedPubKey)

        val appended = output.toByteArray()

        return Base58Check.bytesToBase58(appended)
    }

    override fun getMaintainedCoins(): Array<Slip44?> {
        return arrayOfNulls(0)
    }
}
