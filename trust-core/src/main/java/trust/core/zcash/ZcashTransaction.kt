package trust.core.zcash

import trust.core.zcash.ZcashUtil.BLAKE2B
import trust.core.zcash.ZcashUtil.int32ToBytesLittleEndian
import trust.core.zcash.ZcashUtil.int64ToBytesLittleEndian
import java.io.ByteArrayOutputStream

data class ZcashTransaction(
        var header: Int = -0x7ffffffd,
        var versionGroupId : Int = 0x03C48270,  // VERSION_BRANCH_ID_OVERWINTER
        var shielded: Boolean = false,
        var overwintered: Boolean = true,
        var nLockTime: Long = 0L,
        var inputs: List<ZcashTransactionInput> = emptyList(),
        var outputs: List<ZcashTransactionOutput> = emptyList()


) {

    /**
    * Concatenate all transaction properties, inputs and outputs byte array data
    * Apply the [BLAKE2B] hash function on the combined data
    * The resulting hash will be used for signing the transaction before relaying to the network
    * [BLAKE2B] is the standard hash function for Zcash transactions : https://github.com/zcash/zips/blob/master/zip-0243.rst
    */
    fun toHash(): String? {
        val inputData = ByteArrayOutputStream()
        val outputData = ByteArrayOutputStream()

        for (input in inputs) {
            inputData.write(input.getBytes())
        }

        for (output in outputs) {
            outputData.write(output.getBytes())
        }


        val output = ByteArrayOutputStream()

        val inputByteArray = inputData.toByteArray()
        val outputByteArray = outputData.toByteArray()

        output.write(int32ToBytesLittleEndian(header.toLong()))
        output.write(int32ToBytesLittleEndian(versionGroupId.toLong()))
        output.write(inputByteArray)
        output.write(outputByteArray)
        output.write(int64ToBytesLittleEndian(nLockTime))


        return org.spongycastle.util.encoders.Hex.toHexString(BLAKE2B(
                output.toByteArray()
        ))
    }


}
