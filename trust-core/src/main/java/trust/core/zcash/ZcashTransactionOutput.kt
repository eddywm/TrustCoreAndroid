package trust.core.zcash

import com.google.common.primitives.Bytes
import trust.core.zcash.ZcashUtil.compactSizeIntLittleEndian
import trust.core.zcash.ZcashUtil.int64ToBytesLittleEndian

data class ZcashTransactionOutput(
        var value: Long = 0L,
        var lockingScript: List<Byte> = ArrayList()
) {

    fun getBytes(): ByteArray {
        return Bytes.concat(int64ToBytesLittleEndian(value), compactSizeIntLittleEndian(lockingScript.size.toLong()), lockingScript.toByteArray())
    }

}

fun buildTXO(amount: Long, publicKeyHash: String) : ZcashTransactionOutput {
    return ZcashTransactionOutput(
            value = amount,

            /** Builds a Pay-to-PublicKey-Hash locking script
             *  The returned script is a list of instructions that will be executed
             *  By full nodes once the transaction is relayed to the network.
             *  Instructions are encoded as hex strings values
             *
             *   OP_DUP      OP_HASH160     <PubkeyHash>    OP_EQUALVERIFY   OP_CHECKSIG
             *
             * */
            lockingScript = Bytes.concat(byteArrayOf(0x76.toByte(), 0xa9.toByte(), 0x14.toByte()), org.spongycastle.util.encoders.Hex.decode(publicKeyHash), byteArrayOf(0x88.toByte(), 0xac.toByte())).toList()

    )
}