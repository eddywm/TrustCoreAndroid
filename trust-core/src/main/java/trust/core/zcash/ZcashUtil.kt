package trust.core.zcash

import org.bitcoinj.core.Utils
import org.spongycastle.jce.ECNamedCurveTable
import org.spongycastle.jce.interfaces.ECPrivateKey
import org.spongycastle.jce.interfaces.ECPublicKey
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.jce.spec.ECPrivateKeySpec
import org.spongycastle.util.encoders.Hex
import org.web3j.crypto.ECKeyPair
import trust.core.util.Base58Check
import trust.core.zcash.OpCodes.OP_DUP
import trust.core.zcash.OpCodes.OP_HASH160
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.util.*


object ZcashUtil {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    const val ZcashCurve = "secp256k1"

    fun BLAKE2B(input: ByteArray): ByteArray {
        try {
            val digest = MessageDigest.getInstance("BLAKE2B-256")
            return digest.digest(input)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Couldn't find a BLAKE2B-256 provider", e)
        }

    }

    /*
    Address format : P2PKH Zcash t-address.
    Base58Check([0x1C, 0xB8] || RIPEMD-160(SHA-256(PUBKEY)))
   */
    fun getZcashAddress(keyChain: ECKeyPair): String {

        val output = ByteArrayOutputStream()

        val pubKey = keyChain.publicKey.toByteArray()
        val hashedPubKey = Utils.sha256hash160(pubKey)

        output.write(0x1c)
        output.write(0xb8)
        output.write(hashedPubKey)

        val appended = output.toByteArray()

        return Base58Check.bytesToBase58(appended)
    }


    fun getZcashAddressFromPubKeyHex(publicKey: String): String {
        val output = ByteArrayOutputStream()

        val pubKey = publicKey.toByteArray()
        val hashedPubKey = Utils.sha256hash160(pubKey)

        output.write(0x1c)
        output.write(0xb8)
        output.write(hashedPubKey)

        val appended = output.toByteArray()

        return Base58Check.bytesToBase58(appended)
    }




    /** Given an hex String privateKey [hexPrivateKey] get the [ECPrivateKey] corresponding */
    fun getPrivateKeyFromHex(hexPrivateKey: String): ECPrivateKey {
        val params = ECNamedCurveTable.getParameterSpec("secp256k1")

        val keyFactory = KeyFactory.getInstance("EC", "SC")

        val ecPrivateKeySpec = ECPrivateKeySpec(
                BigInteger(1, Hex.decode(hexPrivateKey)),
                params
        )



        return keyFactory.generatePrivate(ecPrivateKeySpec) as ECPrivateKey
    }


    /** Signature verification function */
    fun verifySignature(pubKeyHex: String, signatureHexData: String, transactionHashData: String): Boolean {
        try {

            val encodedPublicKey = Hex.decode(pubKeyHex)


            val signature = Hex.decode(signatureHexData)

            val data = Hex.decode(transactionHashData)

            val formattedPublicKey = X509EncodedKeySpec(encodedPublicKey)

            val kf = KeyFactory.getInstance("ECDSA", "SC")

            val publicKey = kf.generatePublic(formattedPublicKey)


            val ecdsa = Signature.getInstance("ECDSA")

            with(ecdsa) {
                initVerify(publicKey)
                update(data)
            }
            return ecdsa.verify(signature)

        } catch (e: Exception) {
            println("Error : ${e.message}")
            return false
        }
    }


    /** This function builds a Pay-to-PublicKey-Hash locking script
     *  The returned script is a list of instructions that will be executed
     *  By full nodes once the transaction is relayed to the network.
     *  Instructions are encoded as hex strings values
     * */
    fun buildP2PKHLockingScript(address: String): ArrayList<String> {
        val lockingScript = ArrayList<String>()
        lockingScript.add(OP_DUP)
        lockingScript.add(OP_HASH160)
        lockingScript.add(address)
        lockingScript.add(OpCodes.OP_EQUALVERIFY)
        lockingScript.add(OpCodes.OP_CHECKSIG)
        return lockingScript
    }

    /** This function builds a Pay-to-PublicKey-Hash unlocking script script */
    fun buildP2PKHUnlockingScript(signature: String, publicKey: String): ArrayList<String> {
        val unlockingScript = ArrayList<String>()
        unlockingScript.add(signature)
        unlockingScript.add(publicKey)
        return unlockingScript
    }

    fun getPublicKeyFromHex(hexPublicKey: String): ECPublicKey {

        val encodedPublicKey = Hex.decode(hexPublicKey)

        val formattedPublicKey = X509EncodedKeySpec(encodedPublicKey)

        val kf = KeyFactory.getInstance("ECDSA", "SC")

        val publicKey = kf.generatePublic(formattedPublicKey)

        return publicKey as ECPublicKey
    }



}
