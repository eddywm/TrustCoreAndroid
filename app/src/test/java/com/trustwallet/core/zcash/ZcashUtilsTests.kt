package com.trustwallet.core.zcash

import org.junit.Assert.assertEquals
import org.junit.Test
import org.spongycastle.util.encoders.Hex
import trust.core.zcash.ZcashUtil.compactSizeIntLittleEndian
import trust.core.zcash.ZcashUtil.int32ToBytesLittleEndian
import trust.core.zcash.ZcashUtil.int64ToBytesLittleEndian

class ZcashUtilsTests {

    @Test
    fun test_int32toLittleEndianByteArray_conversion () {

        val number = "00040000"
        val numberLEBytes = int32ToBytesLittleEndian(1024)

        val numberLEHex = Hex.toHexString(numberLEBytes)
        assertEquals(
                number,
                numberLEHex
        )

    }


    @Test
    fun test_int64toLittleEndianByteArray_conversion () {

        val number = "0008000000000000"
        val numberLEBytes = int64ToBytesLittleEndian(2048L)

        val numberLEHex = Hex.toHexString(numberLEBytes)
        assertEquals(
                number,
                numberLEHex
        )

    }

    @Test
    fun test_compactInSize () {

        val number = 16L
        val compactedLEBytes = compactSizeIntLittleEndian(number)

        assertEquals("10", Hex.toHexString(compactedLEBytes) )

    }

}