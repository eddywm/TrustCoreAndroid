package trust.core.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class Value implements Parcelable {

    private static final int NONE_ROUND = -1;

    public static final int MINUS_ONLY = -1;
    public static final int NO_SIGN = 0;
    public static final int ALL_SIGN = 1;

    private final String raw;

    public Value(String in) {
        raw = in;
    }

    public Value(BigDecimal in) {
        this(in.toString());
    }

    public Value(BigInteger in) {
        this(in.toString());
    }

    public Value(double in) {
        this(Double.toString(in));
    }

    public Value(long in) {
        this(Long.toString(in));
    }

    protected Value(Parcel in) {
        raw = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Value) {
            Value otherVal = ((Value) obj);
            return otherVal.raw != null && otherVal.raw.equalsIgnoreCase(raw);
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(raw);
    }

    public String rawString() {
        return raw;
    }

    public BigDecimal bigDecimal() {
        return new BigDecimal(raw);
    }

    protected BigDecimal convert() {
        return bigDecimal();
    }

    public String shortFormat() {
        return shortFormat(null, MINUS_ONLY);
    }

    public String shortFormat(String none, int showSignMode) {
        return format(2, ',', none, showSignMode);
    }

    public String mediumFormat() {
        return mediumFormat(null, MINUS_ONLY);
    }

    public String mediumFormat(String none, int showSignMode) {
        return format(4, ',', none, showSignMode);
    }

    public String fullFormat() {
        return fullFormat(null, MINUS_ONLY);
    }

    public String fullFormat(String none, int showSignMode) {
        return format(NONE_ROUND, ',', none, showSignMode);
    }

    public String format(int decimalPlace, char groupDivider, String none, int showSignMode) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
        formatter.setMaximumFractionDigits(Integer.MAX_VALUE);

        BigDecimal value = convert();

        String result;
        String resultValue;
        if (decimalPlace == NONE_ROUND) {
            resultValue = value.abs().toString();
            result = formatter.format(value.abs());
        } else {
            String fullPlainString = value.stripTrailingZeros().abs().toPlainString();
            resultValue = value.toBigInteger().abs().toString();
            result = formatter.format(value.toBigInteger().abs());

            String fractionString = fullPlainString.substring(resultValue.length());
            decimalPlace = Math.min(decimalPlace, fractionString.length() - 1);
            if (decimalPlace > 0) {
                fractionString = fractionString.substring(0, decimalPlace + 1);
                resultValue = resultValue + fractionString;
                result = formatter.format(new BigDecimal(resultValue));//result + fractionString;
            }
        }

        BigDecimal bigDecimalResult = new BigDecimal(resultValue);
        if (bigDecimalResult.compareTo(BigDecimal.ZERO) == 0) {
            if (TextUtils.isEmpty(none)) {
                if (decimalPlace > 0) {
                    Character[] zeros = new Character[decimalPlace];
                    Arrays.fill(zeros, '0');
                    result = "0." + TextUtils.join("", zeros);
                }
            } else {
                result = none; // -- || "N/A";
            }
        }
        if (!result.equals(none)) {
            switch (showSignMode) {
                case ALL_SIGN: {
                    result = (value.compareTo(BigDecimal.ZERO) >= 0 ? "+" : "-") + result;
                }
                break;
                case MINUS_ONLY: {
                    result = (value.compareTo(BigDecimal.ZERO) < 0 ? "-" : "") + result;
                }
                break;
            }
        }
        return result;
    }

    public static final Creator<Value> CREATOR = new Creator<Value>() {
        @Override
        public Value createFromParcel(Parcel in) {
            return new Value(in);
        }

        @Override
        public Value[] newArray(int size) {
            return new Value[size];
        }
    };
}
