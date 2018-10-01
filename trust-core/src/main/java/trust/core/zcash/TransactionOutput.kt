package trust.core.zcash

data class TransactionOutput(
        var amount: Long = 0,
        var lockingScript: List<String> = ArrayList<String>()
)