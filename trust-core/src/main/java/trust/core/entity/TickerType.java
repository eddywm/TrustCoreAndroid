package trust.core.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;

import trust.core.entity.address.Address;

public interface TickerType extends Parcelable {
    @NonNull
    String getPrice();
    String getSymbol();
    @NonNull
    Double percentChange24h();
    @NonNull
    String getCurrencyCode();
    @NonNull
    Address getContract();
    String getImage();
}
