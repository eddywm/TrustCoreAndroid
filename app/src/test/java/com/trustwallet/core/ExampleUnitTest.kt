package com.trustwallet.core

import org.junit.Test

import org.junit.Assert.*

import trust.core.util.*


class ExampleUnitTest {
    @Test
    fun hexToInt_iSCorrect() {

        val hexValue = "0xf"

        val intValue = Hex.hexToInteger(hexValue)


        assertEquals(intValue, 15)
    }
}
