package trust.core.zcash

import org.spongycastle.jce.provider.BouncyCastleProvider
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


}
