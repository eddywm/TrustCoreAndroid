package trust.core.blockchain.address;

import trust.core.entity.address.Address;
import trust.core.entity.address.EthereumAddress;

public class EthereumAddressFactory implements AddressFactory {

    public static final AddressFactory INSTANCE = new EthereumAddressFactory();

    @Override
    public Address create(String address) {
        return new EthereumAddress(address);
    }

    @Override
    public boolean validate(String address) {
        return EthereumAddress.isAddress(address);
    }

    @Override
    public Address empty() {
        return EthereumAddress.EMPTY;
    }
}
