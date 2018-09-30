package trust.core.zcash;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class ZcashUtil {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] BLAKE2B(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("BLAKE2B-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Couldn't find a BLAKE2B-256 provider", e);
        }
    }


}
