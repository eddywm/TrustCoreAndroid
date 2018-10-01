package trust.core.zcash

import org.spongycastle.jce.interfaces.ECPrivateKey
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.util.encoders.Hex
import trust.core.zcash.ZcashUtil.ZcashCurve
import trust.core.zcash.ZcashUtil.getZcashAddressFromPubKeyHex
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.Signature
import java.security.spec.ECGenParameterSpec



class ZcashWallet {

    var privateKey: String? = null
    var publicKey: String? = null
    var address: String? = null

    init {
        Security.addProvider(BouncyCastleProvider())
        val keyPair = generateKeyPair()
        privateKey = keyPair.first
        val pubkey = keyPair.second
        publicKey = pubkey
        address = getZcashAddressFromPubKeyHex(pubkey)
    }


    private fun generateKeyPair(): Pair<String, String> {

        val keyPairGenerator = KeyPairGenerator.getInstance("EC", "SC")
        val ecGenParameterSpec = ECGenParameterSpec(ZcashCurve)
        keyPairGenerator.initialize(ecGenParameterSpec, SecureRandom())

        val keyPair = keyPairGenerator.genKeyPair()
        val privateKey = keyPair.private

        val publicKey = keyPair.public


        return Pair(
                (privateKey as (ECPrivateKey)).d.toString(16),
                Hex.toHexString(publicKey.encoded)
        )
    }


    fun signTransaction(transactionHashData: String): String {

        val signature = Signature.getInstance("ECDSA", "SC")

        val privateKey = ZcashUtil.getPrivateKeyFromHex(this.privateKey!!)

        signature.initSign(privateKey, SecureRandom())

        val hashedMessage = Hex.decode(transactionHashData)


        signature.update(hashedMessage)

        val signatureBytes = signature.sign()

        return Hex.toHexString(signatureBytes)
    }

    override fun toString(): String {
        return "ZcashWallet(privateKey=$privateKey, publicKey=$publicKey, address=$address)"
    }
}