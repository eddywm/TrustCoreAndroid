package trust.core.blockchain.signer;

import org.bitcoinj.core.ECKey;

import io.reactivex.Single;
import trust.core.blockchain.Slip;

public interface Signer extends Slip {
    Single<byte[]> sign(ECKey ecKey, byte[] message, boolean isHashed);
}
