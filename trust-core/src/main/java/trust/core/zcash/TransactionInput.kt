package trust.core.zcash


data class TransactionInput(
        var amount: Long = 0,
        var unlockingScript: List<Byte> = ArrayList<Byte>()
)