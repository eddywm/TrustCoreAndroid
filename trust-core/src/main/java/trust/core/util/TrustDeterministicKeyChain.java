package trust.core.util;

import com.google.common.collect.ImmutableList;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;

import trust.core.blockchain.Slip44;

public class TrustDeterministicKeyChain extends DeterministicKeyChain {

    private static final int PURPOSE = 44;

    public TrustDeterministicKeyChain(final DeterministicSeed seed) {
        super(seed);
    }

    public DeterministicKey getKeyByPath(Slip44 coin) {
        // ETH44_ACCOUNT_ZERO_PATH = m/44'/COIN TYPE'/0'/0/0
        return super.getKeyByPath(ImmutableList.of(
                new ChildNumber(PURPOSE, true),
                new ChildNumber(coin.type(), true),
                ChildNumber.ZERO_HARDENED,
                ChildNumber.ZERO,
                ChildNumber.ZERO), true);
    }
}
