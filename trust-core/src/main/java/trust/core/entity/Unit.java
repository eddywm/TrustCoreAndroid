package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Unit implements Parcelable {
    public static final Unit DEFAULT = new Unit(18, "");
    public final int decimals;
    public final String symbol;

    public Unit(int decimals, String symbol) {
        this.decimals = decimals;
        this.symbol = symbol;
    }

    protected Unit(Parcel in) {
        decimals = in.readInt();
        symbol = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(decimals);
        dest.writeString(symbol);
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };
}
