package trust.core.zcash;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.regex.Pattern;

import trust.core.entity.address.Address;
import trust.core.entity.address.PlainAddress;

// The type of Zcash address that this implementation supports are the t type address or vanity address
// Note : Zcash does not yet support Hierarchical Deterministic Wallet addresses [BIP-32].
// This implies that the notion of account and derivation path won't be applied for this Zcash integration
// E.g: t1ShwyK2o1Pj2qTpz33tqG8aEMszcxxmax8
public class ZcashAddress extends PlainAddress {

    public static final Address EMPTY = new ZcashAddress("0x0000000000000000000000000000000000000000");

    private static final Pattern tAddressPattern = Pattern.compile("^t[1-9A-HJ-NP-Za-km-z]{34}");


    public ZcashAddress(String value) {
        super(value);
    }

    protected ZcashAddress(Parcel parcel) {
        super(parcel);
    }

    // Valid Zcash t-addresses are 35 length  string
    public static boolean isAddress(String address) {
        return !TextUtils.isEmpty(address) && address.length() == 35 && tAddressPattern.matcher(address).matches();
    }


    // Return the Base58 Encoded t-address
    @Override
    public String value() {
        return super.value();
    }

    // Return the hex value of the address
    @Override
    public String hexValue() {
        return super.hexValue();
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



    