package com.trustwallet.core.zcash

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.spongycastle.jce.provider.BouncyCastleProvider
import trust.core.zcash.ZcashUtil
import java.security.Security

class ZcashTransactionTests {

    init {
        Security.addProvider(BouncyCastleProvider())
    }


    @Test
    fun transaction_signing_test() {
        val message = "4c022acf30183b09d02a56632396c23228b018b65dcaf78be9db3e3c2f27b89d"
        val privateKey = "ec67a6f6aa475d0e696f66b24f6105c9eeb152100733b08ffeed4053c25ccb64"
        val signature = ZcashUtil.signTransaction(message, privateKey)
        assertNotNull(signature)
    }

    @Test
    fun transaction_signature_verification_should_pass() {
        val signatureVerification = ZcashUtil.verifySignature(
                pubKeyHex = "3056301006072a8648ce3d020106052b8104000a03420004111c75eb8a908f13c7ddf23d439e79ee599c701a9cb8a77c533a3a53d1a0bfbbe30406aff91a635b089c49071b613c08d73a85bacfb6dcb6729ad82dda9ff8e7",
                signatureHexData = "3046022100ea4d8dba900b06e33db46dcf88c026b37778b1aebe0eb8e3adefbd6cc930a4e9022100c46e31e8fd07183fb81a9066b275c0a94888fb02da1e4f855dad1cb1b889c3c1",
                transactionHashData = "c8ed5e65713f8170b8d0769a8ed8c35740ba0b706d5856e22d709be1e001cf8c"
        )
        assertEquals(true, signatureVerification)
    }

}