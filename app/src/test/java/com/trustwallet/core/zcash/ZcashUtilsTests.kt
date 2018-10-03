package com.trustwallet.core.zcash

import org.junit.Assert.assertEquals
import org.junit.Test
import org.spongycastle.util.encoders.Hex
import trust.core.zcash.ZcashUtil.int32ToBytesLittleEndian
import trust.core.zcash.ZcashUtil.int64ToBytesLittleEndian

class ZcashUtilsTests {

    @Test
    fun test_int32toLittleEndianByteArray_conversion () {

        val number = "00040000"
        var numberLE = int32ToBytesLittleEndian(1024)

        val numberLEHex = Hex.toHexString(numberLE)
        assertEquals(
                number,
                numberLEHex
        )


    }


    @Test
    fun test_int64toLittleEndianByteArray_conversion () {

        val number = "0004000000000000"
        var numberLE = int64ToBytesLittleEndian(1024L)

        val numberLEHex = Hex.toHexString(numberLE)
        assertEquals(
                number,
                numberLEHex
        )


    }

}