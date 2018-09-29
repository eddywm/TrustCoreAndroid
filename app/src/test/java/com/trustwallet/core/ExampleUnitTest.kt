package com.trustwallet.core

import org.junit.Test

import org.junit.Assert.*
import trust.core.blockchain.account.EthereumAccountFactory

import trust.core.util.*


class ExampleUnitTest {
    @Test
    fun hexToInt_iSCorrect() {

        val hexValue = "0xf"

        val intValue = Hex.hexToInteger(hexValue)


        assertEquals(intValue, 15)
    }

    @Test
    fun account_test() {


        val acc = EthereumAccountFactory()

        val coinX = acc.maintainedCoins[2]

        println("${coinX.name} is Valid addr ? ${coinX.isValid("0xacd")}")
    }
}
