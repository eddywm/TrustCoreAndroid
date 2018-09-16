package trust.core.entity.address;

import android.os.Parcelable;

public interface Address extends Parcelable {

    String checksum();

    String value();

    String hexValue();
}