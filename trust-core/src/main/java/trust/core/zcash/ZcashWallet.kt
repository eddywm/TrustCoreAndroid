package trust.core.zcash

import org.spongycastle.jce.interfaces.ECPrivateKey
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.util.encoders.Hex
import trust.core.zcash.ZcashUtil.ZcashCurve
import trust.core.zcash.ZcashUtil.getAddressFromPubKey
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.Signature
import java.security.spec.ECGenParameterSpec


class ZcashWallet {

    var privateKeyHex: String? = null
    var publicKeyHex: String? = null
    var address: String? = null
    var ecPrivateKey: ECPrivateKey? = null


    init {
        Security.addProvider(BouncyCastleProvider())
        initializeKeys()
    }


    private fun initializeKeys() {

        val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "SC")

        val ecGenParameterSpec = ECGenParameterSpec(ZcashCurve)

        keyPairGenerator.initialize(ecGenParameterSpec, SecureRandom())

        val keyPair = keyPairGenerator.genKeyPair()

        ecPrivateKey = keyPair.private as ECPrivateKey

        privateKeyHex = (keyPair.private as ECPrivateKey).d.toString(16)


        publicKeyHex = Hex.toHexString(keyPair.public.encoded)

        address = getAddressFromPubKey(Hex.toHexString(keyPair.public.encoded))

    }


    fun signTransaction(transactionHashData: String): String {

        val signature = Signature.getInstance("ECDSA", "SC")

        signature.initSign(ecPrivateKey, SecureRandom())

        val hashedMessage = Hex.decode(transactionHashData)

        signature.update(hashedMessage)

        val signatureBytes = signature.sign()

        return Hex.toHexString(signatureBytes)
    }

}