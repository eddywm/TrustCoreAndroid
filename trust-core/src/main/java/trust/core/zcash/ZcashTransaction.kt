package trust.core.zcash

import trust.core.zcash.ZcashUtil.BLAKE2B
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

data class ZcashTransaction(
        var shielded: Boolean = false,
        var overwintered: Boolean = true,
        var nLockTime: Long = 0L,
        var inputs: List<TransactionInput> = emptyList(),
        var outputs: List<TransactionOutput> = emptyList()


) {

    // Concatenate all transaction properties, inputs and outputs data
    // Apply the [BLAKE2B] hash function on the combined data
    // The resulting hash will be used for signing the transaction before relaying to the network
    // [BLAKE2B] is the standard hash function for Zcash transactions : https://github.com/zcash/zips/blob/master/zip-0243.rst
    fun toHash(): String? {
        val inputData = StringBuilder()
        val outputData = StringBuilder()

        for (input in inputs) {
            inputData.append(input.toString())
        }

        for (output in outputs) {
            outputData.append(output.toString())
        }

        val output = ByteArrayOutputStream()


        val inputByteArray = inputData.toString().toByteArray(StandardCharsets.UTF_8)
        val outputByteArray = outputData.toString().toByteArray(StandardCharsets.UTF_8)

        output.write(inputByteArray)
        output.write(outputByteArray)
        output.write(nLockTime.toString().toByteArray(StandardCharsets.UTF_8))


        return org.spongycastle.util.encoders.Hex.toHexString(BLAKE2B(
                output.toByteArray()
        ))
    }


}
