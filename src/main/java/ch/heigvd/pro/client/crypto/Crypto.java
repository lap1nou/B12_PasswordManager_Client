package ch.heigvd.pro.client.crypto;

import ch.heigvd.pro.client.Entry;
import ch.heigvd.pro.client.Password;
import ch.heigvd.pro.client.Utils;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import java.util.Base64;

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

    /**
     * Decrypt an Entry of a Safe with the given key using AES.
     *
     * @param entry  the entry to decrypt
     * @param aesKey the key to use
     */
    public static void decryptAES(Entry entry, SecretKey aesKey) {
        Cipher aesCipher = null;

        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            aesCipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(entry.getPassword().getIv()));

            byte[] result = aesCipher.doFinal(Base64.getDecoder().decode(entry.getPassword().toString()));

            char[] charResult = Utils.byteToCharArray(result);

            entry.setClearPassword(charResult);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypt an Entry of a Safe with the given key using AES.
     * Source : https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption
     *
     * @param entry  the entry to encrypt
     * @param aesKey the key to use
     */
    public static void encryptAES(Entry entry, SecretKey aesKey) {
        Cipher aesCipher = null;

        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

            AlgorithmParameters aesParams = aesCipher.getParameters();

            byte[] iv = aesParams.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] test = aesCipher.doFinal(Utils.charToByteArray(entry.getClearPassword()));

            entry.setPassword(new Password(Base64.getEncoder().encodeToString(test), iv));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compute a SecretKey to encrypt/decrypt Entry, the key is computed by using a KDF function. The KDF function is
     * going to use multiple parameters in order to "derive" a key. Since we need a password with a lot of bits for AES,
     * the KDF is going to produce a SecretKey from a smaller number of bits (the given password).
     *
     * @param password           the password to use for the key
     * @param salt               the salt to use for the key
     * @param numberOfBits       the number of bits of the key
     * @param numberOfIterations the number of iterations to do to compute the key
     * @return a SecretKey
     */
    public static SecretKey generateKey(char[] password, char[] salt, int numberOfBits, int numberOfIterations) {
        SecretKeyFactory aesKeyFactory;
        SecretKey aesKey = null;

        try {
            aesKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec aesKeySpec = new PBEKeySpec(password, "salt".getBytes(), numberOfIterations, numberOfBits);
            SecretKey tmp = aesKeyFactory.generateSecret(aesKeySpec);
            aesKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return aesKey;
    }

}