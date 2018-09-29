package trust.core.zcash;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import trust.core.entity.address.PlainAddress;

// The type of Zcash address that are supported are the t type address or vanity address
// Note that Zcash does not have yet a BIP 32 standard type for addresses.

public class ZcashAddress extends PlainAddress {

    public ZcashAddress(String value) {
        super(value);
    }

    protected ZcashAddress(Parcel parcel) {
        super(parcel);
    }

    public static boolean isAddress(String address) {

        return !TextUtils.isEmpty(address);
    }

    public Parcelable.Creator<ZcashAddress> CREATOR = new Parcelable.Creator<ZcashAddress>() {
        @Override
        public ZcashAddress createFromParcel(Parcel parcel) {
            return new ZcashAddress(parcel);
        }

        @Override
        public ZcashAddress[] newArray(int size) {
            return new ZcashAddress[size];
        }
    };


}



    