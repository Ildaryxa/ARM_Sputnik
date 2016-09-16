package crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ildar on 29.06.2016.
 */
public class HashText {
    public static String getHash(String pas, String salt){
        String passHash = pas + salt;
        return sha512(passHash);
    }

    private static String sha512(String passHash) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("algorithm sha-512 non found");
            System.exit(-1);
        }
        md.update(passHash.getBytes());
        byte byteData[] = md.digest();
        StringBuffer hashCodeBuffer = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            hashCodeBuffer.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return hashCodeBuffer.toString();
    }
}
