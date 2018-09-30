package trust.core.zcash

import trust.core.blockchain.address.AddressFactory
import trust.core.entity.address.Address

class ZcashAddressFactory : AddressFactory {


    override fun create(address: String): Address {
        return ZcashAddress(address)
    }

    override fun validate(address: String): Boolean {
        return ZcashAddress.isAddress(address)
    }

    override fun empty(): Address {
        return ZcashAddress.EMPTY
    }

    companion object {
        val INSTANCE: AddressFactory = ZcashAddressFactory()
    }


}