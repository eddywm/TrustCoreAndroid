package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AssetInfo implements Parcelable {
    public final boolean isEnabled;
    public final boolean isAddedManually;
    public final String itemId;
    public final String annotation;
    public final String coverUri;
    public final String externalUri;
    public final String category;

    public AssetInfo(boolean isEnabled, boolean isAddedManually) {
        this(isEnabled, isAddedManually, null, null, null, null, null);
    }

    public AssetInfo(
            boolean isEnabled,
            boolean isAddedManually,
            String itemId,
            String annotation,
            String coverUri,
            String externalUri,
            String category) {
        this.isEnabled = isEnabled;
        this.isAddedManually = isAddedManually;
        this.itemId = itemId;
        this.annotation = annotation;
        this.coverUri = coverUri;
        this.externalUri = externalUri;
        this.category = category;
    }

    protected AssetInfo(Parcel in) {
        itemId = in.readString();
        annotation = in.readString();
        coverUri = in.readString();
        externalUri = in.readString();
        category = in.readString();
        isEnabled = in.readByte() != 0;
        isAddedManually = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(annotation);
        dest.writeString(coverUri);
        dest.writeString(externalUri);
        dest.writeString(category);
        dest.writeByte((byte) (isEnabled ? 1 : 0));
        dest.writeByte((byte) (isAddedManually ? 1 : 0));
    }

    public static final Creator<AssetInfo> CREATOR = new Creator<AssetInfo>() {
        @Override
        public AssetInfo createFromParcel(Parcel in) {
            return new AssetInfo(in);
        }

        @Override
        public AssetInfo[] newArray(int size) {
            return new AssetInfo[size];
        }
    };
}
