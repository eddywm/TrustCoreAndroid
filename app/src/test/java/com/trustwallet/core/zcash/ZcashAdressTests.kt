package com.trustwallet.core.zcash

import org.junit.Assert.assertEquals
import org.junit.Test
import trust.core.zcash.ZcashAddress
import trust.core.zcash.ZcashUtil.getAddressFromPubKey
import trust.core.zcash.ZcashWallet

class ZcashAdressTests {

    @Test
    fun test_address_format() {

        val address = "t1ShwyK2o1Pj2qTpz33tqG8aEMszcxxmax8"
        val isValidAddress = ZcashAddress.isAddress(address)
        assertEquals(true, isValidAddress)


        val wrongAddress = "t1ShwyK2o1Pj2qTpz33tqG8aEMszcxxmax8yhd"
        val isValidZcashAddress = ZcashAddress.isAddress(wrongAddress)

        assertEquals(false, isValidZcashAddress)

    }

    @Test
    fun derive_address_from_publicKey () {

        val zcashWallet = ZcashWallet()

        val publicKey = zcashWallet.publicKey!!

        val address = zcashWallet.address

        assertEquals(getAddressFromPubKey(publicKey), address)

    }
}