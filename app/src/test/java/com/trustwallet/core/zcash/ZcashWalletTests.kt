package com.trustwallet.core.zcash

import org.junit.Assert.*
import org.junit.Test
import org.spongycastle.util.encoders.Hex
import trust.core.zcash.ZcashAddress
import trust.core.zcash.ZcashUtil.getAddressFromPubKey
import trust.core.zcash.ZcashUtil.getPrivateKeyFromHex
import trust.core.zcash.ZcashUtil.getPublicKeyFromHex
import trust.core.zcash.ZcashWallet

class ZcashWalletTests {

    @Test
    fun test_wallet_creation() {

        val zcashWallet = ZcashWallet()
        assertNotNull(zcashWallet.privateKey)

        val publicKey = zcashWallet.publicKey
        assertNotNull(publicKey)

        val address = zcashWallet.address
        assertNotNull(address)

        assertEquals(getAddressFromPubKey(publicKey!!), address)
    }

    @Test

    fun address_created_isValid() {
        val zcashWallet = ZcashWallet()
        assertTrue(ZcashAddress.isAddress(zcashWallet.address!!))
    }

    @Test
    fun keys_recovery_from_hexFormat_to_ECKeys() {
        val privateKeyHex = "c2f60d25deac1a6048d2cbd7a9141c91e98f41c213dc22047c49837ae39e4a01"
        val pubKeyHex =  "3056301006072a8648ce3d020106052b8104000a03420004157a3f04aec9eba1c404cc05e280e8cd869404ece7683a87cba8cdf849904a94793ad282c74c5d12b5af2c73a2c5fc6f6dc9eb848dc26ba5203a0af909faaf5c"


        val ecPrivateKey = getPrivateKeyFromHex(privateKeyHex)

        assertEquals(
                ecPrivateKey.d.toString(16),
                privateKeyHex
        )
        val ecPubKey = getPublicKeyFromHex(pubKeyHex)

        assertEquals(
                Hex.toHexString(ecPubKey.encoded),
                pubKeyHex
        )
    }

}

