package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class Asset implements Parcelable {

    private static final String ID_TEMPLATE = "addr%s-node%s-type%s-cat%s-item_id%s";

    public static final int COIN = 1;
    public static final int TOKEN = 2;
    public static final int STUFF = 3;
    public static final int GAS = 4;

    @Retention(SOURCE)
    @IntDef({COIN, TOKEN, STUFF, GAS})
    public @interface AssetType {}

    @AssetType
    public final int type;
    @NonNull
    public final Contract contract;
    @NonNull
    public final AssetInfo info;
    @NonNull
    public final Account account;
    @Nullable
    public final TickerType ticker;
    @Nullable
    public final Value balance;
    public final long updateBalanceTime;

    public Asset(@AssetType int type, Contract contract, AssetInfo assetInfo, Account account) {
        this(type, contract, assetInfo, account, null, null, 0);
    }

    public Asset(Asset asset, TickerType ticker) {
        this(asset.type, asset.contract, asset.info, asset.account, asset.balance, ticker, asset.updateBalanceTime);
    }

    public Asset(
            @AssetType int type,
            @NonNull
            Contract contract,
            @NonNull
            AssetInfo info,
            @NonNull
            Account account,
            @Nullable
            Value balance,
            @Nullable
            TickerType ticker,
            long updateBalanceTime) {
        this.type = type;
        this.contract = contract;
        this.info = info;
        this.account = account;
        this.ticker = ticker;
        this.balance = balance;
        this.updateBalanceTime = updateBalanceTime;
    }

    protected Asset(Parcel in) {
        type = in.readInt();
        contract = in.readParcelable(Contract.class.getClassLoader());
        info = in.readParcelable(AssetInfo.class.getClassLoader());
        account = in.readParcelable(Node.class.getClassLoader());
        ticker = in.readParcelable(TickerType.class.getClassLoader());
        balance = in.readParcelable(Value.class.getClassLoader());
        updateBalanceTime = in.readLong();
    }

    public String id() {
        return String.format(ID_TEMPLATE,
                contract.address.toString(),
                contract.coin.type(),
                type,
                info.category,
                info.itemId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeParcelable(contract, flags);
        dest.writeParcelable(info, flags);
        dest.writeParcelable(account, flags);
        dest.writeParcelable(ticker, flags);
        dest.writeParcelable(balance, flags);
        dest.writeLong(updateBalanceTime);
    }

    public static final Creator<Asset> CREATOR = new Creator<Asset>() {
        @Override
        public Asset createFromParcel(Parcel in) {
            return new Asset(in);
        }

        @Override
        public Asset[] newArray(int size) {
            return new Asset[size];
        }
    };
}
