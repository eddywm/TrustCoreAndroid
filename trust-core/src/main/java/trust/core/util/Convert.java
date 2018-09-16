package trust.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Convert {

    public static BigDecimal fromWei(BigInteger value, int decimal) {
        return new BigDecimal(value).divide(BigDecimal.TEN.pow(decimal));
    }
}
