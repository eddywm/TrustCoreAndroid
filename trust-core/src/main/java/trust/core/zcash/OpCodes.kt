package trust.core.zcash


/**
* In this file are defined the basic operation codes
* Which encounter for the majority of transactions
* That occur on the Zcash blockchain [P2PKH].
* P2PKH or pay to public key hash transaction.
*/
object OpCodes {
    //Stack Operations

    // Pop the top element duplicate it and push it back on the stack
    const val OP_DUP = "0x76"

    // Bit Logic

    // Pop 2 elements on top of the stack, verify if they are equal
    // Return true if they are equal and false otherwise
    const val OP_EQUALVERIFY = "0x88"

    // Crypto

    // Pop top stack element, apply RPEMD160() hash function to it and push it back.
    const val OP_HASH160 = "0xA9"
    // Check the validity of the signed transaction data by verifying with the public key
    const val OP_CHECKSIG = "0xac"


}