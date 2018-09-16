package trust.core.entity;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;

public class Gas implements Parcelable {
    @Nonnull
    private BigInteger price;
    private final boolean isPriceDefault;
    @Nonnull
    private BigInteger limit;
    private final boolean isLimitDefault;

    private Gas(@NonNull BigInteger price, boolean isPriceDefault, @NonNull BigInteger limit, boolean isLimitDefault) {
        this.price = price;
        this.isPriceDefault = isPriceDefault;
        this.limit = limit;
        this.isLimitDefault = isLimitDefault;
    }

    private Gas(Parcel in) {
        price = new BigInteger(in.readString());
        isPriceDefault = in.readInt() == 1;
        limit = new BigInteger(in.readString());
        isLimitDefault = in.readInt() == 1;
    }

    @Nonnull
    public BigInteger priceInWei() {
        return price;
    }

    @Nonnull
    public BigDecimal priceInGwei() {
        return Convert.fromWei(new BigDecimal(price), Convert.Unit.GWEI);
    }

    public boolean isPriceDefault() {
        return isPriceDefault;
    }

    @Nonnull
    public BigInteger limit() {
        return limit;
    }

    public boolean isLimitDefault() {
        return isLimitDefault;
    }

    public static Builder builder() {
        return new Builder();
    }

    public BigInteger calculateNetworkFeeInWei() {
        return price.multiply(limit);
    }

    public static class Builder {
        @Nullable
        private BigInteger price;
        @Nullable
        private BigInteger limit;
        private boolean isLimitDefault;
        private boolean isPriceDefault;

        Builder() {}

        public Builder limit(@Nullable String gasLimit, boolean isDefault) {
            BigInteger num;
            try {
                num = new BigInteger(gasLimit);
            } catch (Exception ex) {
                isDefault = true;
                num = BigInteger.ZERO;
            }
            limit(num, isDefault);
            return this;
        }

        public Builder limit(@Nullable BigInteger gasLimit, boolean isDefault) {
            this.limit = gasLimit;
            this.isLimitDefault = isDefault;
            return this;
        }

        public Builder limit(long gasLimit, boolean isDefault) {
            return limit(BigInteger.valueOf(gasLimit), isDefault);
        }

        public Builder priceInWei(@Nullable BigInteger gasPrice, boolean isDefault) {
            this.price = gasPrice;
            this.isPriceDefault = isDefault;
            return this;
        }

        public Builder priceInWei(long gasPrice, boolean isDefault) {
            return priceInWei(new BigInteger(Long.toString(gasPrice)), isDefault);
        }

        public Builder priceInGwei(String gasPrice, boolean isDefault) {
            BigDecimal num;
            try {
                num = new BigDecimal(gasPrice);
            } catch (Exception ex) {
                num = BigDecimal.ZERO;
                isDefault = true;
            }
            return priceInWei(Convert.toWei(num, Convert.Unit.GWEI).toBigInteger(), isDefault);
        }

        public Gas create() {
            if (price == null) {
                price = BigInteger.ZERO;
            }
            if (limit == null) {
                limit = BigInteger.ZERO;
            }
            return new Gas(price, isPriceDefault, limit, isLimitDefault);
        }
    }

    public static final Creator<Gas> CREATOR = new Creator<Gas>() {
        @Override
        public Gas createFromParcel(Parcel in) {
            return new Gas(in);
        }

        @Override
        public Gas[] newArray(int size) {
            return new Gas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(price.toString());
        parcel.writeInt(isPriceDefault ? 1 : 0);
        parcel.writeString(limit.toString());
        parcel.writeInt(isLimitDefault ? 1 : 0);
    }
}
