package trust.core.zcash

import com.google.common.primitives.Bytes
import org.spongycastle.util.encoders.Hex
@Suppress("ArrayInDataClass")



data class ZcashTransactionInput(
        var txid: ByteArray = ByteArray(32),
        var index: Int = 0,
        var value: Long = 0L,
        var sequence: Int = -0x1,
        var unlockingScript:ByteArray = ByteArray(32)
)

fun buildTXIn(value: Long, signature: String, publicKeyHex: String): ZcashTransactionInput {
    return ZcashTransactionInput(
            value = value,

            /**
             *  Builds a Pay-to-PublicKey-Hash unlocking script
             *  <SIGNATURE>  <PUBKEYHASH>
             *
             */
            unlockingScript = Bytes.concat(Hex.decode(signature), Hex.decode(publicKeyHex))

    )
}