package com.trustwallet.core.zcash

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import trust.core.zcash.*


class ZcashTransactionTests {


    @Test
    fun transaction_signing_and_verification_test() {

        val zcashWallet = ZcashWallet()
        val transactionHashData = "4c022acf30183b09d02a56632396c23228b018b65dcaf78be9db3e3c2f27b89d"

        val signature = zcashWallet.signTransaction(transactionHashData)
        assertNotNull(signature)

        val signatureVerification = ZcashUtil.verifySignature(
                pubKeyHex = zcashWallet.publicKeyHex!!,
                signatureHexData = signature,
                transactionHashData = transactionHashData
        )
        assertEquals(true, signatureVerification)

    }


    @Test
    fun transaction_formation_signing_verification() {
        val txInputs = listOf(
                buildTXIn(1230000,
                        "3046022100a137c0c3a6dea9e703d71fdc74777973c9fb94a3f49674851560dddf9d010c4d022100aa11383c93c1879d8e852525199159f66fd0d53993e827f39c6478b4e7ddb1bb",
                        "3059301306072a8648ce3d020106082a8648ce3d030107034200048ea7f06f7e33654798a26f50337080a7f4ad82232d5f67839abd472885fa498615eefc7c727358324d04d3e4e042f287e5ebdfa213d07ed563f38999628c77d6")
        )
        val txOutputs = listOf(
                buildTXO(5000000000, "3b82be84abed90d5833f46fedebab6811de08420"),
                buildTXO(1800000, "3b82be84abed90d5833f46fedebab6811de08420")

        )

        val transaction = ZcashTransaction(
                shielded = false,
                nLockTime = 0,
                inputs = txInputs,
                outputs = txOutputs
        )

        val txHashData = transaction.getHashDataForSign()

        val zcashWallet = ZcashWallet()

        val signature = zcashWallet.signTransaction(txHashData)
        assertNotNull(signature)


        val signatureVerification = ZcashUtil.verifySignature(
                pubKeyHex = zcashWallet.publicKeyHex!!,
                signatureHexData = signature,
                transactionHashData = txHashData
        )
        assertEquals(true, signatureVerification)

    }

}