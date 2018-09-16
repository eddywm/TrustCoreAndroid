package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.annotations.NonNull;
import trust.core.C;
import trust.core.entity.address.Address;
import trust.core.util.Convert;
import trust.core.util.Numbers;

public class TransactionUnsigned implements Parcelable {
    private int type;
    private Asset asset;

    private Address recipientAddress;
    private BigDecimal value = BigDecimal.ZERO;

    private byte[] data;
    private Gas gas;
    private long nonce;
    private boolean shouldMaxAmount;
    private int chainTag;

    public TransactionUnsigned(int type, @NonNull Asset asset) {
        this.type = type;
        this.asset = asset;
        gas(Gas.builder()
            .priceInWei(C.GasPriceConfiguration.DEFAULT, true)
            .limit(C.GasLimitConfiguration.TOKEN_DEFAULT, true)
            .create());
    }

    private TransactionUnsigned(Parcel in) {
        type = in.readInt();
        asset = in.readParcelable(Asset.class.getClassLoader());
        recipientAddress = in.readParcelable(Address.class.getClassLoader());
        value = new BigDecimal(in.readString());
        data = in.createByteArray();
        gas = in.readParcelable(Gas.class.getClassLoader());
        nonce = in.readLong();
        shouldMaxAmount = in.readInt() == 1;
        chainTag = in.readInt();
    }

    public int type() {
        return type;
    }

    public Asset asset() {
        return asset;
    }

    public Unit unit() {
        return type == C.TransferType.TOKEN ? asset.contract.unit : asset.account.coin.unit();
    }

    public Address contractAddress() {
        return asset.contract.address;
    }

    public TransactionUnsigned recipientAddress(Address recipientAddress) {
        this.recipientAddress = recipientAddress;
        return this;
    }

    public Address recipientAddress() {
        return recipientAddress;
    }

    public Address toAddress() {
        return type == C.TransferType.TOKEN ? asset.contract.address : recipientAddress;
    }

    public TransactionUnsigned value(BigDecimal input) {
        this.value = input;
        return this;
    }

    public TransactionUnsigned value(String input) {
        this.value = Convert.fromWei(Numbers.hexToBigInteger(input), unit().decimals);
        return this;
    }

    public BigDecimal value() {
        return value;
    }

    public BigDecimal subunitAmount() {
        return value.multiply(BigDecimal.TEN.pow(unit().decimals));
    }

    public TransactionUnsigned data(byte[] data) {
        this.data = data;
        return this;
    }

    public byte[] data() {
        if (type == C.TransferType.TOKEN) {
            return createTokenTransferData(recipientAddress.checksum(), subunitAmount());
        } else {
            return data == null ? new byte[0] : data;
        }
    }

    public TransactionUnsigned gas(Gas gas) {
        this.gas = gas;
        return this;
    }

    public Gas gas() {
        return gas;
    }

    public Account from() {
        return asset.account;
    }

    public TransactionUnsigned nonce(long nonce) {
        this.nonce = nonce;
        return this;
    }

    public long nonce() {
        return nonce;
    }

    public TransactionUnsigned shouldMaxAmount(boolean shouldMaxAmount) {
        this.shouldMaxAmount = shouldMaxAmount;
        return this;
    }

    public boolean shouldMaxAmount() {
        return shouldMaxAmount;
    }

    public boolean canAttachData() {
        return type != C.TransferType.TOKEN;
    }

    public TransactionUnsigned chainTag(int chainTag) {
        this.chainTag = chainTag;
        return this;
    }

    public int chainTag() {
        return chainTag;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeParcelable(asset, flags);
        dest.writeParcelable(recipientAddress, flags);
        dest.writeString(value.toString());
        dest.writeByteArray(data);
        dest.writeParcelable(gas, flags);
        dest.writeLong(nonce);
        dest.writeInt(shouldMaxAmount ? 1 : 0);
        dest.writeInt(chainTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionUnsigned> CREATOR = new Creator<TransactionUnsigned>() {
        @Override
        public TransactionUnsigned createFromParcel(Parcel in) {
            return new TransactionUnsigned(in);
        }

        @Override
        public TransactionUnsigned[] newArray(int size) {
            return new TransactionUnsigned[size];
        }
    };

    public TransactionUnsigned calculateGas(BigInteger gasPrice, BigInteger gasLimit) {
        BigInteger minLimit = BigInteger.valueOf(C.GasLimitConfiguration.MIN);
        BigInteger defaultLimit = C.GasLimitConfiguration.getDefault(type);
        Boolean isLimitDefault = false;
        if (gasLimit.compareTo(minLimit) < 0 || gasLimit.compareTo(BigInteger.valueOf(C.GasLimitConfiguration.MAX)) > 0) {
            gasLimit = defaultLimit;
            isLimitDefault = true;
        }
        boolean isPriceDefault = false;
        if (gasPrice.longValue() < C.GasPriceConfiguration.MIN) {
            gasPrice = BigInteger.valueOf(C.GasPriceConfiguration.DEFAULT);
            isPriceDefault = true;
        }
        gas(Gas.builder().limit(gasLimit, isLimitDefault).priceInWei(gasPrice, isPriceDefault).create());
        return this;
    }

    public static byte[] createTokenTransferData(String to, BigDecimal tokenAmount) {
        List<Type> params = Arrays.asList(new Type[] {new org.web3j.abi.datatypes.Address(to), new Uint256(tokenAmount.toBigInteger())});
        List<TypeReference<?>> returnTypes = Collections.<TypeReference<?>>singletonList(new TypeReference<Bool>() {});
        Function function = new Function("transfer", params, returnTypes);
        String encodedFunction = FunctionEncoder.encode(function);
        return Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(encodedFunction));
    }
}
