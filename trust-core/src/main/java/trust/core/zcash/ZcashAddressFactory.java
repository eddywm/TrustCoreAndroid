package trust.core.zcash;

import trust.core.blockchain.address.AddressFactory;
import trust.core.entity.address.Address;

public class ZcashAddressFactory implements AddressFactory {

    public static final AddressFactory INSTANCE = new ZcashAddressFactory();

    @Override
    public Address create(String address) {
        return null;
    }

    @Override
    public boolean validate(String address) {
        return false;
    }

    @Override
    public Address empty() {
        return null;
    }
}
