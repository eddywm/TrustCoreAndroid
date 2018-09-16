package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

import trust.core.blockchain.Slip44;

public class Wallet implements Parcelable {

    public final static int UNKNOWN = -1;
    public final static int KEY_STORE = 0;
    public final static int WATCH = 1;
    public final static int MNEMONIC = 2;

    public final String id;
    public final Account[] accounts;
    public final int type;
    public final String name;
    public final boolean isMainWallet;

    public Wallet(String id, Account account, int type, boolean isMainWallet) {
        this(id, new Account[] {account}, type, isMainWallet);
    }

    public Wallet(String id, Account[] accounts, int type, boolean isMainWallet) {
        this(id, accounts, type, "", isMainWallet);
    }

    public Wallet(String id, Account[] accounts, int type, String name, boolean isMainWallet) {
        this.id = id;
        this.accounts = accounts;
        this.type = type;
        this.name = name;
        this.isMainWallet = isMainWallet;
    }

    protected Wallet(Parcel in) {
        id = in.readString();
        accounts = in.createTypedArray(Account.CREATOR);
        type = in.readInt();
        name = in.readString();
        isMainWallet = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeTypedArray(accounts, flags);
        dest.writeInt(type);
        dest.writeString(name);
        dest.writeByte((byte) (isMainWallet ? 1 : 0));
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    public Account account(Slip44 coin) {
        for (Account account : accounts) {
            if (account.coin == coin) {
                return account;
            }
        }
        return null;
    }

    public Account defaultAccount() {
        Account account = account(Slip44.ETH);
        return account == null ? accounts[0] : account;
    }
}
