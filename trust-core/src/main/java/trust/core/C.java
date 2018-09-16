package trust.core;

import java.math.BigInteger;

public abstract class C {

    public static final int N = 1 << 9;
    public static final int P = 1;

    private static final int ETHER_DECIMALS = 18;
    public static final int GWEI_DECIMALS = 9;

    public static abstract class TransferType {
        public static final int COIN = 1;
        public static final int TOKEN = 2;
        public static final int DAPP = 3;
        public static final int SDK = 4;

        public static CharSequence toString(int type) {
            switch (type) {
                default:
                case COIN: {
                    return "COIN";
                }
                case TOKEN: {
                    return "TOKEN";
                }
                case DAPP: {
                    return "DAPP";
                }
            }
        }
    }

    public static abstract class ConfigureTransaction {
        public static BigInteger LIMIT_MAX = new BigInteger("6370515");
        public static BigInteger FEE_MAX = BigInteger.TEN.pow(ETHER_DECIMALS - 1);
        public static BigInteger PRICE_MAX = FEE_MAX.divide(BigInteger.valueOf(GasLimitConfiguration.MIN));

        public static int validateNetworkFee(BigInteger networkFee) {
            return networkFee.compareTo(FEE_MAX) > 0 ? 1 : 0;
        }
    }

    public static abstract class GasPriceConfiguration {
        private static final BigInteger WEI_IN_GWEI = BigInteger.TEN.pow(GWEI_DECIMALS);
        private static final long WEI_IN_GWEI_LONG = WEI_IN_GWEI.longValue();
        public static final long DEFAULT = new BigInteger("16").multiply(WEI_IN_GWEI).longValue();
        public static final long MIN = new BigInteger("1").multiply(WEI_IN_GWEI).longValue();
        private static final long MIDDLE = new BigInteger("40").multiply(WEI_IN_GWEI).longValue();

        private static final double PER_PERCENTAGE = (MIDDLE - MIN) * 0.01;

        public static long percentToValue(int percent) {
            long wei = (long) (PER_PERCENTAGE * percent) + MIN;
            return (wei / WEI_IN_GWEI_LONG) * WEI_IN_GWEI_LONG;
        }

        public static int valueToPercent(long value) {
            return (int) ((value - MIN) / PER_PERCENTAGE);
        }

        public static int validate(BigInteger value) {
            return value.compareTo(BigInteger.valueOf(MIN)) < 0
                    ? -1
                    : value.compareTo(ConfigureTransaction.PRICE_MAX) > 0 ? 1 : 0;
        }
    }

    public static abstract class GasLimitConfiguration {
        public static final long COIN_DEFAULT = 90000;
        public static final long TOKEN_DEFAULT = 144000;
        public static final long DAPP_DEFAULT = 600000;
        public static final long MIN = 21000;
        public static final long MAX = 300000;

        private static final double PER_PERCENTAGE = (MAX - MIN) * 0.01;

        public static long percentToValue(int percent) {
            return (long) (PER_PERCENTAGE * percent) + MIN;
        }

        public static int valueToPercent(long value) {
            return (int) ((value - MIN) / PER_PERCENTAGE);
        }

        public static int validate(BigInteger value) {
            return value.compareTo(BigInteger.valueOf(MIN)) < 0
                    ? -1
                    : value.compareTo(ConfigureTransaction.LIMIT_MAX) > 0 ? 1 : 0;
        }

        public static BigInteger getDefault(int type) {
            switch (type) {
                case TransferType.TOKEN: {
                    return BigInteger.valueOf(C.GasLimitConfiguration.TOKEN_DEFAULT);
                }
                case TransferType.DAPP: {
                    return BigInteger.valueOf(C.GasLimitConfiguration.DAPP_DEFAULT);
                }
                case TransferType.COIN:
                default: {
                    return BigInteger.valueOf(C.GasLimitConfiguration.COIN_DEFAULT);
                }
            }
        }
    }
}
