package trust.core.zcash

import com.google.common.primitives.Bytes
import org.spongycastle.crypto.digests.Blake2bDigest
import org.spongycastle.util.encoders.Hex
import trust.core.zcash.ZcashUtil.compactSizeIntLittleEndian
import trust.core.zcash.ZcashUtil.int32ToBytesLittleEndian
import trust.core.zcash.ZcashUtil.int64ToBytesLittleEndian

data class ZcashTransaction(

        // https://github.com/zcash/zips/blob/master/zip-0202.rst#transaction-format-version-3
        // Tx header: 0x80000003 : 10000000000000000000000000000011 : First bit (Overwinter flag, 2 last bits 11(3) == version)

        var header: Long = 0x80000003,
        var versionGroupId: Int = 0x03C48270,  // VERSION_BRANCH_ID_OVERWINTER
        var consensusBranchId: Int  = 0x5ba81b19,
        var nExpiryHeight: Int = 0,
        var sigHashAll: Int = 1,
        var shielded: Boolean = false,
        var overwintered: Boolean = true,
        var nLockTime: Int = 0,
        var inputs: List<ZcashTransactionInput> = emptyList(),
        var outputs: List<ZcashTransactionOutput> = emptyList()


) {

    /**
     * Concatenate all transaction properties, inputs and outputs byte array data in respect to ZIP 0143
     * Apply the `BLAKE2B` hash function on the specified data in the ZIP spec
     * The resulting hash will be used for signing the transaction before relaying to the network.
     *
     * `BLAKE2B` is the standard hash function for Zcash transactions : https://github.com/zcash/zips/blob/master/zip-0143.rst
     */


    fun getHashDataForSign(): String {
        val txSignatureBytes = computeSigBytes()
        return Hex.toHexString(txSignatureBytes)!!


    }

    private fun computeSigBytes(): ByteArray? {
        val hashPrevTxOuts = ByteArray(32)
        val hashSequence = ByteArray(32)
        val hashOutputs = ByteArray(32)

        val prevTxOutsDigest = Blake2bDigest(null, 32, null, ZCASH_PREVOUTS_HASH_PERSONALIZATION)
        var prevTxOutsSerialized = ByteArray(0)

        for (i in inputs.indices) {
            val input = inputs[i]
            prevTxOutsSerialized = Bytes.concat(prevTxOutsSerialized, input.txid, int32ToBytesLittleEndian(input.index))
        }

        prevTxOutsDigest.update(prevTxOutsSerialized, 0, prevTxOutsSerialized.size)
        prevTxOutsDigest.doFinal(hashPrevTxOuts, 0)

        val sequenceDigest = Blake2bDigest(null, 32, null, ZCASH_SEQUENCE_HASH_PERSONALIZATION)

        var sequenceSerialization = ByteArray(0)

        for (i in inputs.indices) {
            sequenceSerialization = Bytes.concat(sequenceSerialization, int32ToBytesLittleEndian(inputs[i].sequence))
        }

        sequenceDigest.update(sequenceSerialization, 0, sequenceSerialization.size)
        sequenceDigest.doFinal(hashSequence, 0)

        val outputsDigest = Blake2bDigest(null, 32, null, ZCASH_OUTPUTS_HASH_PERSONALIZATION)
        var outputsSerialized = ByteArray(0)

        for (i in outputs.indices) {
            val out = outputs[i]
            outputsSerialized = Bytes.concat(
                    outputsSerialized,
                    int64ToBytesLittleEndian(out.value),
                    compactSizeIntLittleEndian(out.lockingScript.size.toLong()),
                    out.lockingScript
            )
        }

        outputsDigest.update(outputsSerialized, 0, outputsSerialized.size)
        outputsDigest.doFinal(hashOutputs, 0)

        return Bytes.concat(
                int32ToBytesLittleEndian(header.toInt()),
                int32ToBytesLittleEndian(versionGroupId),
                hashPrevTxOuts,
                hashSequence,
                hashOutputs,
                ByteArray(32), //hashJoinSplits, zeros for this type of  transparent address transaction
                int32ToBytesLittleEndian(nExpiryHeight),
                int32ToBytesLittleEndian(sigHashAll)
        )
    }


    companion object {
        private val ZCASH_SEQUENCE_HASH_PERSONALIZATION = byteArrayOf('Z'.toByte(), 'c'.toByte(), 'a'.toByte(), 's'.toByte(), 'h'.toByte(), 'S'.toByte(), 'e'.toByte(), 'q'.toByte(), 'u'.toByte(), 'e'.toByte(), 'n'.toByte(), 'c'.toByte(), 'H'.toByte(), 'a'.toByte(), 's'.toByte(), 'h'.toByte()) //ZcashSequencHash
        private val ZCASH_OUTPUTS_HASH_PERSONALIZATION = byteArrayOf('Z'.toByte(), 'c'.toByte(), 'a'.toByte(), 's'.toByte(), 'h'.toByte(), 'O'.toByte(), 'u'.toByte(), 't'.toByte(), 'p'.toByte(), 'u'.toByte(), 't'.toByte(), 's'.toByte(), 'H'.toByte(), 'a'.toByte(), 's'.toByte(), 'h'.toByte())  //ZcashOutputsHash
        private val ZCASH_PREVOUTS_HASH_PERSONALIZATION = byteArrayOf('Z'.toByte(), 'c'.toByte(), 'a'.toByte(), 's'.toByte(), 'h'.toByte(), 'P'.toByte(), 'r'.toByte(), 'e'.toByte(), 'v'.toByte(), 'o'.toByte(), 'u'.toByte(), 't'.toByte(), 'H'.toByte(), 'a'.toByte(), 's'.toByte(), 'h'.toByte()) //ZcashPrevoutHash

    }
}
