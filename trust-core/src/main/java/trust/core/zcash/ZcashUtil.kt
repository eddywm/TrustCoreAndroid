package trust.core.zcash

import org.spongycastle.jce.provider.BouncyCastleProvider
import trust.core.zcash.OpCodes.OP_DUP
import trust.core.zcash.OpCodes.OP_HASH160
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Security

object ZcashUtil {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    fun BLAKE2B(input: ByteArray): ByteArray {
        try {
            val digest = MessageDigest.getInstance("BLAKE2B-256")
            return digest.digest(input)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Couldn't find a BLAKE2B-256 provider", e)
        }

    }


    /** This function builds a Pay-to-PublicKey-Hash locking script
     *  The returned script is a list of instructions that will be executed
     *  By full nodes once the transaction is relayed to the network.
     *  Instructions are encoded as hex strings values
     * */
    fun buildP2PKHLockingScript(publicKeyHash: String): ArrayList<String> {
        val lockingScript = ArrayList<String>()
        lockingScript.add(OP_DUP)
        lockingScript.add(OP_HASH160)
        lockingScript.add(publicKeyHash)
        lockingScript.add(OpCodes.OP_EQUALVERIFY)
        lockingScript.add(OpCodes.OP_CHECKSIG )
        return lockingScript
    }

    /** This function builds a Pay-to-PublicKey-Hash unlocking script script */
    fun buildP2PKHUnlockingScript(signature: String, publicKey: String): ArrayList<String> {
        val unlockingScript = ArrayList<String>()
        unlockingScript.add(signature)
        unlockingScript.add(publicKey)
        return unlockingScript
    }


}
