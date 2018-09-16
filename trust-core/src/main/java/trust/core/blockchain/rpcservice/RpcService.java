package trust.core.blockchain.rpcservice;

import io.reactivex.Single;
import trust.core.blockchain.Slip;
import trust.core.blockchain.Slip44;
import trust.core.entity.Account;
import trust.core.entity.Asset;
import trust.core.entity.BlockInfo;
import trust.core.entity.Gas;
import trust.core.entity.Transaction;
import trust.core.entity.TransactionUnsigned;
import trust.core.entity.Value;

public interface RpcService extends Slip {

    Single<BlockInfo> getBlockNumber(Slip44 coin);

    Single<Gas> estimateGas(TransactionUnsigned transactionUnsigned);

    Single<Long> estimateNonce(Account account);

    Single<Value> getAccountBalance(Account account);

    Single<Value> getContractBalance(Asset asset);

    Single<byte[]> encodeTransaction(TransactionUnsigned transactionUnsigned, byte[] signature);

    Single<String> sendTransaction(Account account, byte[] signedMessage);

    Single<Transaction> findTransaction(Account account, String hash);
}
