package com.trustwallet.core.zcash

import org.junit.Assert.*
import org.junit.Test
import org.spongycastle.util.encoders.Hex
import trust.core.zcash.ZcashAddress
import trust.core.zcash.ZcashUtil.getPrivateKeyFromHex
import trust.core.zcash.ZcashUtil.getPublicKeyFromHex
import trust.core.zcash.ZcashWallet

class ZcashWalletTests {

    @Test
    fun test_wallet_creation() {
        val zcashWallet = ZcashWallet()
        assertNotNull(zcashWallet.privateKey)
        assertNotNull(zcashWallet.publicKey)
        assertNotNull(zcashWallet.address)
    }

    @Test

    fun address_created_isValid() {
        val zcashWallet = ZcashWallet()
        assertTrue(ZcashAddress.isAddress(zcashWallet.address!!))
    }

    @Test
    fun keys_recovery_from_hexFormat_to_ECKeys() {
        val zcashWallet = ZcashWallet()
        val privateKeyHex = zcashWallet.privateKey
        val pubKeyHex = zcashWallet.publicKey


        val ecPrivateKey = getPrivateKeyFromHex(privateKeyHex!!)

        assertEquals(
                ecPrivateKey.d.toString(16),
                privateKeyHex
        )

        val ecPubKey = getPublicKeyFromHex(pubKeyHex!!)


        assertEquals(
                Hex.toHexString(ecPubKey.encoded),
                pubKeyHex
        )
    }

}

