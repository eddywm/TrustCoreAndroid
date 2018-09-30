package trust.core.zcash

import trust.core.util.Hex
import trust.core.zcash.ZcashUtil.BLAKE2B
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

class ZcashTransaction {
    var transactionHash = String()

    var transactionInputs: Array<TransactionInput> = arrayOf()
    var transactionOutputs: Array<TransactionOutput> = arrayOf()


    // BLAKE2B hash function is used for hashing all transaction inputs and outputs
    // BLAKE2B is the standard hash function for Zcash transactions : https://github.com/zcash/zips/blob/master/zip-0243.rst
    fun toHash(): String? {
        val inputData = StringBuilder()
        val outputData = StringBuilder()

        for (input in transactionInputs!!) {
            inputData.append(input.toString())
        }

        for (output in transactionOutputs!!) {
            outputData.append(output.toString())
        }

        val output = ByteArrayOutputStream()


        val inputByteArray = inputData.toString().toByteArray(StandardCharsets.UTF_8)
        val outputByteArray = outputData.toString().toByteArray(StandardCharsets.UTF_8)

        output.write(inputByteArray)
        output.write(outputByteArray)


        return Hex.byteArrayToHexString(BLAKE2B(
                output.toByteArray()
        ))
    }


}
