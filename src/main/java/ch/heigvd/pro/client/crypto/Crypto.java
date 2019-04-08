package ch.heigvd.pro.client.crypto;

import org.apache.commons.codec.digest.DigestUtils;

public class Crypto {
    public static final int kAnonimityConstant = 5;

    /**
     * Returns the first five characters of the SHA1 of the given password in upper case.
     *
     * @param password the password to compute the hash of
     * @return the first five characters of the SHA1, of password in upper case
     */
    public static String fiveAnonimitySHA1(String password) {
        return DigestUtils.sha1Hex(password.getBytes()).substring(0, kAnonimityConstant).toUpperCase();
    }

    /**
     * Returns the all the substring from the characters number five of the SHA1 of the given password in upper case.
     *
     * @param password the password to compute the hash of
     * @return the substring from the characters number five of the SHA1, of password in upper case
     */
    public static String restOfFiveAnonimitySHA1(String password) {
        return DigestUtils.sha1Hex(password.getBytes()).substring(kAnonimityConstant).toUpperCase();
    }
    
}