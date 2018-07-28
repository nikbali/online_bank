package system.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {
    public static String getHash(String x) throws NoSuchAlgorithmException {
        MessageDigest d = MessageDigest.getInstance("SHA-1");
        StringBuffer hashcode = new StringBuffer();
        d.reset();
        d.update(x.getBytes());
        byte [] digest = d.digest();
        for (int i = 0; i < digest.length; ++i)
        {
            hashcode.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1));
        }
        return hashcode.toString();
    }

}
