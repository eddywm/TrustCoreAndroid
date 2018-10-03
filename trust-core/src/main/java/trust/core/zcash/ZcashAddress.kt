package trust.core.zcash

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import trust.core.entity.address.Address
import trust.core.entity.address.PlainAddress
import java.util.regex.Pattern


/**
* The type of Zcash address that this implementation supports is the `t type` address or transparent address
* Note : Zcash does not yet have a ZIP for Hierarchical Deterministic Wallet addresses. [Open Issue] https://github.com/zcash/zcash/issues/2673
* This implies that the notion of account (as defined in BIP44) and derivation path won't be applied for this Zcash integration
* Type of Zcash address supported : t1ShwyK2o1Pj2qTpz33tqG8aEMszcxxmax8
*/
open class ZcashAddress : PlainAddress {

    var CREATOR: Parcelable.Creator<ZcashAddress> = object : Parcelable.Creator<ZcashAddress> {
        override fun createFromParcel(parcel: Parcel): ZcashAddress {
            return ZcashAddress(parcel)
        }

        override fun newArray(size: Int): Array<ZcashAddress?> {
            return arrayOfNulls(size)
        }
    }

    constructor(value: String) : super(value)
    protected constructor(parcel: Parcel) : super(parcel)


    // Zcash does not use 0x type addresses
    override fun hexValue(): String? {
        return null
    }

    companion object {
        val EMPTY: Address = ZcashAddress("0x0000000000000000000000000000000000000000")
        private val tAddressPattern = Pattern.compile("^t[1-9A-HJ-NP-Za-km-z]{34}")

        // Valid Zcash t-addresses are 35 length Base58 string
        fun isAddress(address: String): Boolean {
            return !TextUtils.isEmpty(address) && address.length == 35 && tAddressPattern.matcher(address).matches()
        }
    }


}



