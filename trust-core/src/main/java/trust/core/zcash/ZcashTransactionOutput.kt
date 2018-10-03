package trust.core.zcash

import com.google.common.primitives.Bytes
import org.spongycastle.util.encoders.Hex
@Suppress("ArrayInDataClass")


data class ZcashTransactionOutput(
        var value: Long = 0L,
        var lockingScript: ByteArray = ByteArray(32)
)
fun buildTXO(amount: Long, publicKeyHash: String) : ZcashTransactionOutput {
    return ZcashTransactionOutput(
            value = amount,

            /**
             *  Builds a Pay-to-PublicKey-Hash locking script
             *  The returned script is a series of instructions that will be executed
             *  By full nodes once the transaction is relayed to the network.
             *  Instructions are encoded as byte values with specific opcode meaning
             *
             *   OP_DUP      OP_HASH160     <PubkeyHash>    OP_EQUALVERIFY   OP_CHECKSIG
             *
             * */
            lockingScript = Bytes.concat(byteArrayOf(0x76.toByte(), 0xa9.toByte(), 0x14.toByte()), Hex.decode(publicKeyHash), byteArrayOf(0x88.toByte(), 0xac.toByte()))

    )
}