package trust.core.entity.address;

import android.os.Parcel;

import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

public class PlainAddress implements Address {

    private final String value;

    public PlainAddress(String value) {
        value = value.toLowerCase();
        if (Numeric.containsHexPrefix(value)) {
            value = Numeric.cleanHexPrefix(value);
        }
        this.value = value;
    }

    protected PlainAddress(Parcel parcel) {
        value = parcel.readString();
    }

    @Override
    public String checksum() {
        String digested = Numeric.cleanHexPrefix(Numeric.toHexString(Hash.sha3(value.getBytes())));
        int len = value.length();
        StringBuilder result = new StringBuilder(len + 2);
        result.append("0x");
        for (int i = 0; i < len; i++) {
            int c = digested.charAt(i);
            if (c > 55) {
                result.append(String.valueOf(value.charAt(i)).toUpperCase());
            } else {
                result.append(value.charAt(i));
            }
        }

        return result.toString();
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String hexValue() {
        return toString();
    }

    @Override
    public String toString() {
        return "0x" + value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other != null && getClass().equals(other.getClass())
                && value.equals(((Address) other).value());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
    }

    public Creator<PlainAddress> CREATOR = new Creator<PlainAddress>() {
        @Override
        public PlainAddress createFromParcel(Parcel parcel){
            return new PlainAddress(parcel);
        }

        @Override
        public PlainAddress[] newArray(int size) {
            return new PlainAddress[size];
        }
    };
}
