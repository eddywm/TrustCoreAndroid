package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

import trust.core.blockchain.Slip44;
import trust.core.entity.address.Address;

public class Account implements Parcelable {
    public final Address address;
    public final Slip44 coin;

    public Account(String address, Slip44 coin) {
        this(coin.toAddress(address), coin);
    }

    public Account(Address address, Slip44 coin) {
        this.address = address;
        this.coin = coin;
    }

    protected Account(Parcel in) {
        String value = in.readString();
        coin = Slip44.valueOf(in.readString());
        address = coin.toAddress(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address.value());
        dest.writeString(coin.name());
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
