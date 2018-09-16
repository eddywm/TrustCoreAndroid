package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import trust.core.blockchain.Slip44;
import trust.core.entity.address.Address;


public class Contract implements Parcelable {
    @NonNull
    public final Address address;
    @NonNull
    public final String name;
    @NonNull
    public final Unit unit;
    @NonNull
    public final Slip44 coin;

    public Contract(@NonNull String address, @NonNull String name, @NonNull Unit unit, Slip44 coin) {
        this(coin.toAddress(address), name, unit, coin);
    }

    public Contract(@NonNull Address address, @NonNull String name, @NonNull Unit unit, @NonNull Slip44 coin) {
        this.address = address;
        this.name = name;
        this.unit = unit;
        this.coin = coin;
    }

    protected Contract(Parcel in) {
        address = in.readParcelable(Address.class.getClassLoader());
        name = in.readString();
        unit = in.readParcelable(Unit.class.getClassLoader());
        coin = Slip44.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address, flags);
        dest.writeString(name);
        dest.writeParcelable(unit, flags);
        dest.writeString(coin.name());
    }

    public static final Creator<Contract> CREATOR = new Creator<Contract>() {
        @Override
        public Contract createFromParcel(Parcel in) {
            return new Contract(in);
        }

        @Override
        public Contract[] newArray(int size) {
            return new Contract[size];
        }
    };
}
