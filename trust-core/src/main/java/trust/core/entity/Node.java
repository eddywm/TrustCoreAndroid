package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Node implements Parcelable {

    public static final String ETH_TOKEN_SYMBOL = "ERC";

    public static final Node ETH_NODE = new Node("https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk",
            "https://etherscan.io/tx/",
            "0x000000000000000000000000000000000000003c",
            "ethereum");
    public static final Node CLO_NODE = new Node("https://clo-geth.0xinfra.com",
            "https://explorer.callisto.network/tx/",
            "0x0000000000000000000000000000000000000334",
            "callisto");
    public static final Node GO_NODE = new Node("https://rpc.gochain.io",
            "https://explorer.gochain.io/tx/",
            "0x00000000000000000000000000000000000017ac",
            "gochain");
    public static final Node ETC_NODE = new Node("https://etc-rpc.binancechain.io/",
            "https://gastracker.io/tx/",
            "0x000000000000000000000000000000000000003d",
            "classic");
    public static final Node POA_NODE = new Node("https://poa.infura.io/llyrtzQ3YhkdESt2Fzrk",
            "https://poaexplorer.com/txid/search/",
            "0x00000000000000000000000000000000000000b2",
            "poa");

    public static final Node VET_NODE = new Node("https://vechain-rpc.binancechain.io",
            "https://poaexplorer.com/txid/search/",
            "0x0000000000000000000000000000000000000332",
            "vechain");

    public static final Node ZEC_NODE = new Node("https://chain.so/api/v2/get_info/ZEC",
            "https://chain.so/tx/ZEC/",
            "0x0000000000000000000000000000000000000123",
            "zec");

    public static final Node[] NODES = new Node[] {
            ETH_NODE,
            ETC_NODE,
            POA_NODE,
            CLO_NODE,
            GO_NODE,
            VET_NODE,
            ZEC_NODE,
    };

    public final String rpcUri;
    public final String externalExplorerUrl;
    public final String coinAddress;
    public final String endpointPath;

    public Node(
            String rpcUri,
            String externalExplorerUrl,
            String coinAddress,
            String endpointPath) {
        this.rpcUri = rpcUri;
        this.externalExplorerUrl = externalExplorerUrl;
        this.coinAddress = coinAddress;
        this.endpointPath = endpointPath;
    }

    protected Node(Parcel in) {
        rpcUri = in.readString();
        externalExplorerUrl = in.readString();
        coinAddress = in.readString();
        endpointPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(rpcUri);
        dest.writeString(externalExplorerUrl);
        dest.writeString(coinAddress);
        dest.writeString(endpointPath);
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        @Override
        public Node createFromParcel(Parcel in) {
            return new Node(in);
        }

        @Override
        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}
