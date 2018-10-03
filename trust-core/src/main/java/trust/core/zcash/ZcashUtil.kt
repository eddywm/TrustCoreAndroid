package trust.core.zcash

import org.bitcoinj.core.Utils
import org.bitcoinj.core.Utils.sha256hash160
import org.spongycastle.jce.ECNamedCurveTable
import org.spongycastle.jce.interfaces.ECPrivateKey
import org.spongycastle.jce.interfaces.ECPublicKey
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.jce.spec.ECPrivateKeySpec
import org.spongycastle.util.encoders.Hex
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash.sha256
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.KeyFactory
import java.security.Security
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.*

// This class defines utility functions and constants used in this Zcash integration
object ZcashUtil {

    init {
        Security.addProvider(BouncyCastleProvider())
    }
    const val ZcashCurve = "secp256k1"

    val ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"  // Everything except 0OIl : BASE58 Standard characters
    private val ALPHABET_SIZE = BigInteger.valueOf(ALPHABET.length.toLong())


    /*
    Address format : P2PKH Zcash t-address.
    Base58Check([0x1C, 0xB8] || RIPEMD-160(SHA-256(PUBKEY)))
   */
    fun getZcashAddress(keyChain: ECKeyPair): String {

        val output = ByteArrayOutputStream()

        val pubKey = keyChain.publicKey.toByteArray()
        val hashedPubKey = sha256hash160(pubKey)

        output.write(0x1c)
        output.write(0xb8)
        output.write(hashedPubKey)

        val appended = output.toByteArray()

        return bytesToBase58(appended)
    }


    fun getAddressFromPubKey(publicKey: String): String {
        val output = ByteArrayOutputStream()

        val pubKey = publicKey.toByteArray()
        val hashedPubKey = Utils.sha256hash160(pubKey)

        output.write(0x1c)
        output.write(0xb8)
        output.write(hashedPubKey)

        val appended = output.toByteArray()

        return bytesToBase58(appended)
    }


    fun getPrivateKeyFromHex(hexPrivateKey: String): ECPrivateKey {
        val params = ECNamedCurveTable.getParameterSpec("secp256k1")

        val keyFactory = KeyFactory.getInstance("EC", "SC")

        val ecPrivateKeySpec = ECPrivateKeySpec(
                BigInteger(1, Hex.decode(hexPrivateKey)),
                params
        )
        return keyFactory.generatePrivate(ecPrivateKeySpec) as ECPrivateKey
    }

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
            println("Error during verification: : ${e.message}")
            return false
        }
    }
    fun getPublicKeyFromHex(hexPublicKey: String): ECPublicKey {

        val encodedPublicKey = Hex.decode(hexPublicKey)

        val formattedPublicKey = X509EncodedKeySpec(encodedPublicKey)

        val kf = KeyFactory.getInstance("ECDSA", "SC")

        val publicKey = kf.generatePublic(formattedPublicKey)

        return publicKey as ECPublicKey
    }


    // BASE58 Utilities

    // Adds the checksum and converts to Base58Check.
    private fun bytesToBase58(data: ByteArray): String {
        return rawBytesToBase58(addCheckHash(data))
    }


    // Directly converts to Base58Check without adding a checksum.
    private fun rawBytesToBase58(data: ByteArray): String {
        // Convert to base-58 string
        val sb = StringBuilder()
        var num = BigInteger(1, data)
        while (num.signum() != 0) {
            val quotrem = num.divideAndRemainder(ALPHABET_SIZE)
            sb.append(ALPHABET[quotrem[1].toInt()])
            num = quotrem[0]
        }

        // Add '1' characters for leading 0-value bytes
        var i = 0
        while (i < data.size && data[i].toInt() == 0) {
            sb.append(ALPHABET[0])
            i++
        }
        return sb.reverse().toString()
    }


    // Returns a new byte array by concatenating the given array with its checksum.
     fun addCheckHash(data: ByteArray): ByteArray {
        try {
            val hash = Arrays.copyOf(sha256(sha256(data)), 4)
            val buf = ByteArrayOutputStream()
            buf.write(data)
            buf.write(hash)
            return buf.toByteArray()
        } catch (e: IOException) {
            throw AssertionError(e)
        }

    }




    // Bytes & Bits Manipulation, Serialization, Layout

    fun int64ToBytesLittleEndian(value: Long): ByteArray {
        val byteBuffer = ByteBuffer.allocate(8)
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        byteBuffer.putLong(value)
        return byteBuffer.array()
    }

    fun int32ToBytesLittleEndian(value: Int): ByteArray {
        val byteBuffer = ByteBuffer.allocate(4)
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        byteBuffer.putInt(value)
        return byteBuffer.array()
    }

    fun compactSizeIntLittleEndian(value: Long): ByteArray {
        val result: ByteArray
        when {
            value < 253 -> {
                result = ByteArray(1)
                result[0] = value.toByte()
            }
            value < 0x10000 -> {
                result = ByteArray(3)
                result[0] = 253.toByte()
                result[1] = (0xff and value.toInt()).toByte()
                result[2] = (0xff and ((value shr 8).toInt())).toByte()
            }
            value < 0x1000000 -> {
                result = ByteArray(5)
                result[0] = 254.toByte()
                result[1] = (0xff and value.toInt()).toByte()
                result[2] = (0xff and ((value shr 8).toInt())).toByte()
                result[3] = (0xff and ((value shr 16).toInt())).toByte()
                result[4] = (0xff and ((value shr 24).toInt())).toByte()
            }
            else -> {
                result = ByteArray(9)
                result[0] = 255.toByte()
                result[1] = (0xff and value.toInt()).toByte()
                result[2] = (0xff and ((value shr 8).toInt())).toByte()
                result[3] = (0xff and ((value shr 16).toInt())).toByte()
                result[4] = (0xff and ((value shr 24).toInt())).toByte()
                result[5] = (0xff and ((value shr 32).toInt())).toByte()
                result[6] = (0xff and ((value shr 40).toInt())).toByte()
                result[7] = (0xff and ((value shr 48).toInt())).toByte()
                result[8] = (0xff and ((value shr 56).toInt())).toByte()
            }
        }

        return result
    }


}
