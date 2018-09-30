package com.trustwallet.core.zcash

import org.junit.Assert.assertNotNull
import org.junit.Test
import trust.core.blockchain.Slip44
import trust.core.zcash.ZcashAccountFactory

class ZcashAccountTests {

    @Test
    fun test_account_creation() {
        val accountFactory = ZcashAccountFactory()
        val ecKey = accountFactory.ecKey!!
        val acc = accountFactory.createAccount(ecKey, Slip44.ZEC).blockingFirst()


        assertNotNull(accountFactory.privateKey)
        assertNotNull(accountFactory.publicKey)


        assertNotNull(acc)
        assertNotNull(acc.address)
        assertNotNull(acc.coin)

    }
}