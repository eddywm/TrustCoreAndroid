package trust.core.blockchain.address;


import trust.core.entity.address.Address;

public interface AddressFactory {

    Address create(String address);

    boolean validate(String address);

    Address empty();
}
