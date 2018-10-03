package trust.core.zcash

import com.google.common.primitives.Bytes
import trust.core.util.Hex


data class ZcashTransactionInput(
        var index: Long = 0,
        var value: Long = 0L,
        var sequence: Int = -0x1,
        var unlockingScript: List<Byte> = ArrayList()
) {
    fun getBytes(): ByteArray {
        return Bytes.concat(ZcashUtil.int64ToBytesLittleEndian(value), ZcashUtil.compactSizeIntLittleEndian(unlockingScript.size.toLong()), unlockingScript.toByteArray())
    }
}

fun buildTXIn(value: Long, signature: String, publicKeyHex: String): ZcashTransactionInput {
    return ZcashTransactionInput(
            value = value,
            /** Builds a Pay-to-PublicKey-Hash unlocking script script
             *  <SIGNATURE>  <PUBKEYHASH>
             * */
            unlockingScript = Bytes.concat(Hex.hexStringToByteArray(signature), org.spongycastle.util.encoders.Hex.decode(publicKeyHex)).toList()

    )
}