package ch.heigvd.pro.client.crypto;

import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.*;

import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import java.util.Arrays;
import java.util.Base64;

public class Crypto {
    public static final int K_ANONIMITY_CONSTANT = 5;
    public static final int KEY_LENGTH = 256;
    public static final int SALT_BYTE_LENGTH = 32;
    public static final int NUMBER_OF_ITERATIONS = 100;

    /**
     * Returns the first five characters of the SHA1 of the given password in upper case.
     *
     * @param password the password to compute the hash of
     * @return the first five characters of the SHA1, of password in upper case
     */
    public static String fiveAnonimitySHA1(char[] password) {
        char[] passwordTmp = password.clone();
        String result = DigestUtils.sha1Hex(Utils.charToByteArray(passwordTmp)).substring(0, K_ANONIMITY_CONSTANT).toUpperCase();

        // Cleaning password copy
        Arrays.fill(passwordTmp, (char) 0);

        return result;
    }

    /**
     * Returns the all the substring from the characters number five of the SHA1 of the given password in upper case.
     *
     * @param password the password to compute the hash of
     * @return the substring from the characters number five of the SHA1, of password in upper case
     */
    public static String restOfFiveAnonimitySHA1(char[] password) {
        char[] passwordTmp = password.clone();
        String result = DigestUtils.sha1Hex(Utils.charToByteArray(passwordTmp)).substring(K_ANONIMITY_CONSTANT).toUpperCase();

        // Cleaning password copy
        Arrays.fill(passwordTmp, (char) 0);

        return result;
    }

    /**
     * Decrypt an Entry of a Safe with the given key using AES.
     *
     * @param entry  the entry to decrypt
     * @param aesKey the key to use
     */
    public static void decryptAES(Entry entry, SecretKey aesKey) throws BadPaddingException {
        Cipher aesCipher = null;

        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            aesCipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(entry.getIv()));

            byte[] decryptedPassword = aesCipher.doFinal(Base64.getDecoder().decode(Utils.charToByteArray(entry.getPassword())));
            byte[] decryptedTarget = aesCipher.doFinal(Base64.getDecoder().decode(Utils.charToByteArray(entry.getTarget())));
            byte[] decryptedUsername = aesCipher.doFinal(Base64.getDecoder().decode(Utils.charToByteArray(entry.getUsername())));

            char[] decryptedPasswordChar = Utils.byteToCharArray(decryptedPassword);
            char[] decryptedTargetChar = Utils.byteToCharArray(decryptedTarget);
            char[] decryptedUsernameChar = Utils.byteToCharArray(decryptedUsername);

            entry.setClearPassword(decryptedPasswordChar);
            entry.setTarget(decryptedTargetChar);
            entry.setUsername(decryptedUsernameChar);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
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

            byte[] encryptedPassword = aesCipher.doFinal(Utils.charToByteArray(entry.getClearPassword()));
            byte[] encryptedTarget = aesCipher.doFinal(Utils.charToByteArray(entry.getTarget()));
            byte[] encryptedUsername = aesCipher.doFinal(Utils.charToByteArray(entry.getUsername()));

            entry.setIv(iv);
            entry.setPassword(Base64.getEncoder().encodeToString(encryptedPassword).toCharArray());
            entry.setTarget(Utils.byteToCharArray(Base64.getEncoder().encode(encryptedTarget)));
            entry.setUsername(Utils.byteToCharArray(Base64.getEncoder().encode(encryptedUsername)));

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

    public static byte[] generateSalt(int numberOfByte) {
        SecureRandom randomValue = new SecureRandom();

        byte[] salt = new byte[numberOfByte];
        randomValue.nextBytes(salt);

        return salt;
    }

}