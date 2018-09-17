package trust.core.blockchain;

import trust.core.blockchain.address.AddressFactory;
import trust.core.blockchain.address.EthereumAddressFactory;
import trust.core.entity.Node;
import trust.core.entity.Unit;
import trust.core.entity.address.Address;

import static trust.core.entity.Node.CLO_NODE;
import static trust.core.entity.Node.ETC_NODE;
import static trust.core.entity.Node.ETH_NODE;
import static trust.core.entity.Node.GO_NODE;
import static trust.core.entity.Node.POA_NODE;
import static trust.core.entity.Node.VET_NODE;

public enum Slip44 {

    ETH(60, "Ethereum", "ETH", 18, 1, EthereumAddressFactory.INSTANCE, ETH_NODE),
    CLO(820, "Ethereum Classic", "CLO", 18, 820, EthereumAddressFactory.INSTANCE, ETC_NODE),
    GO(6060,"Callisto", "GO", 18, 60, EthereumAddressFactory.INSTANCE, CLO_NODE),
    ETC(61, "GoChain", "ETC", 18, 61, EthereumAddressFactory.INSTANCE, GO_NODE),
    POA(178, "POA Network", "POA", 18, 99, EthereumAddressFactory.INSTANCE, POA_NODE),
    VET(818, "VeChain", "VET", 18, 39, EthereumAddressFactory.INSTANCE, VET_NODE);

    private final int coin;
    private final String name;
    private final Unit unit;
    private final int chainId;
    private final AddressFactory addressFactory;
    private final Node node;

    Slip44(int coin, String name, String symbol, int decimals, int chainId, AddressFactory addressFactory, Node node) {
        this.coin = coin;
        this.name = name;
        this.unit = new Unit(decimals, symbol);
        this.chainId = chainId;
        this.addressFactory = addressFactory;
        this.node = node;
    }

    public int type() {
        return coin;
    }

    public Unit unit() {
        return unit;
    }

    public String coinName() {
        return name;
    }

    public int chainId() {
        return chainId;
    }

    public Node node() {
        return node;
    }

    public Address toAddress(String address) {
        return addressFactory.create(address);
    }

    public boolean isValid(String address) {
        return addressFactory.validate(address);
    }

    public boolean available(Slip44[] maintainCoins) {
        for (Slip44 maintainCoin : maintainCoins) {
            if (maintainCoin == this) {
                return true;
            }
        }
        return false;
    }

    public Address emptyAddress() {
        return addressFactory.empty();
    }

    public static Slip44 find(int type) {
        for (Slip44 item : values()) {
            if (item.type() == type) {
                return item;
            }
        }
        return null;
    }
}
