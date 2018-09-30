package trust.core.zcash;

import trust.core.blockchain.address.AddressFactory;
import trust.core.entity.address.Address;

public class ZcashAddressFactory implements AddressFactory {

    public static final AddressFactory INSTANCE = new ZcashAddressFactory();


    @Override
    public Address create(String address) {
        return new ZcashAddress(address);
    }

    @Override
    public boolean validate(String address) {
        return ZcashAddress.isAddress(address);
    }

    @Override
    public Address empty() {
        return ZcashAddress.EMPTY;
    }


}
