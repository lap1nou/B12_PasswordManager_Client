package ch.heigvd.pro.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms.*;


public class Crypto {
    public static String fiveAnonimitySHA1(String password) {
        MessageDigest mDigest = null;

        try {
            mDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //System.out.println(password);
        //System.out.println(DigestUtils.sha1Hex(mDigest.digest(password.getBytes())));
        return null;
    }
}