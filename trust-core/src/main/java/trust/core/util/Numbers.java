package trust.core.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;

public class Numbers {

    @Nullable
    public static Integer hexToInteger(String input) {
        try {
            return Integer.decode(input);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Nullable
    public static BigInteger hexToBigInteger(String input) {
        if (TextUtils.isEmpty(input)) {
            return null;
        }
        try {
            boolean isHex = containsHexPrefix(input);
            if (isHex) {
                input = cleanHexPrefix(input);
            }
            return new BigInteger(input, isHex ? 16 : 10);
        } catch (NullPointerException | NumberFormatException ex) {
            return null;
        }
    }

    @Nonnull
    public static BigInteger hexToBigInteger(String input, BigInteger def) {
        BigInteger value = hexToBigInteger(input);
        return value == null ? def : value;
    }

    @Nullable
    public static BigDecimal hexToBigDecimal(String input) {
        return new BigDecimal(hexToBigInteger(input));
    }

    @Nonnull
    public static BigDecimal hexToBigDecimal(String input, BigDecimal def) {
        return new BigDecimal(hexToBigInteger(input, def.toBigInteger()));
    }

    private static boolean containsHexPrefix(String input) {
        return input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    private static String cleanHexPrefix(String input) {
        return input.substring(2);
    }

    @Nullable
    public static byte[] hexToByteArray(String input) {
        return TextUtils.isEmpty(input) ? null : Numeric.hexStringToByteArray(input);
    }

    public static Integer hexToInteger(String input, int def) {
        Integer value = hexToInteger(input);
        return value == null ? def : value;
    }

    public static String hexToDecimal(String value) {
        BigInteger result = hexToBigInteger(value);
        return result == null ? null : result.toString(10);
    }
}
