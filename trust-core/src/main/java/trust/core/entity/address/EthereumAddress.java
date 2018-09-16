package trust.core.entity.address;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.regex.Pattern;

public class EthereumAddress extends PlainAddress {
    private static final Pattern addressRegex = Pattern.compile("^0x[a-fA-F0-9]{40}$");
    public static final Address EMPTY = new EthereumAddress("0x0000000000000000000000000000000000000000");

    public EthereumAddress(String value) {
        super(value);
    }

    public EthereumAddress(Parcel parcel) {
        super(parcel);
    }

    public static boolean isAddress(String address) {
        return !TextUtils.isEmpty(address) && addressRegex.matcher(address).matches();
    }

    public Parcelable.Creator<EthereumAddress> CREATOR = new Parcelable.Creator<EthereumAddress>() {
        @Override
        public EthereumAddress createFromParcel(Parcel parcel){
            return new EthereumAddress(parcel);
        }

        @Override
        public EthereumAddress[] newArray(int size) {
            return new EthereumAddress[size];
        }
    };
}
