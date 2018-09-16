package trust.core.entity;

public class WalletDescriptor {
    public final byte[] data;
    public final Account[] accounts;

    public WalletDescriptor(byte[] data, Account... accounts) {
        this.data = data;
        this.accounts = accounts;
    }
}
