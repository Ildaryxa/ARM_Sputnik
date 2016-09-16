package crypt;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by ildar on 29.06.2016.
 */
public class Salt {
    public static String generator(){
        return new BigInteger(100, new SecureRandom()).toString();
    }
}
