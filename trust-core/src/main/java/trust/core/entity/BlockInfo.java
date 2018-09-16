package trust.core.entity;

import java.math.BigInteger;

public class BlockInfo {
    public final String id;
    public final BigInteger number;

    public BlockInfo(String id, BigInteger number) {
        this.id = id;
        this.number = number;
    }
}
