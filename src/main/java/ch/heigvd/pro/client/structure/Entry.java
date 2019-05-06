package ch.heigvd.pro.client.structure;

import ch.heigvd.pro.client.crypto.Crypto;
import ch.heigvd.pro.client.password.PasswordChecker;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;
import java.util.Date;

public class Entry {
    private int id;
    private char[] username;
    private char[] target;
    private transient char[] clearPassword;
    private char[] encryptedPassword;
    private byte[] iv;
    private char[] salt;
    private char[] email;
    private int strength;
    private Date registerDate;

    public Entry(int id, char[] username, char[] target, char[] clearPassword, char[] email, char[] salt, Date registerDate) {
        this.id = id;
        this.username = username;
        this.target = target;
        this.clearPassword = clearPassword;
        this.strength = PasswordChecker.checkStrong(clearPassword);
        this.email = email;
        this.registerDate = registerDate;
        this.salt = salt;
    }

    public char[] getUsername() {
        return username;
    }

    public void setUsername(char[] username) {
        this.username = username;
    }

    public char[] getPassword() {
        return encryptedPassword;
    }

    public void setPassword(char[] password) {
        this.encryptedPassword = password;
    }

    public char[] getEmail() {
        return email;
    }

    public void setTarget(char[] target) {
        this.target = target;
    }

    public char[] getClearPassword() {
        return clearPassword;
    }

    public char[] getTarget() {
        return target;
    }

    public void setClearPassword(char[] clearPassword) {
        this.clearPassword = clearPassword;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public byte[] getIv() {
        return iv;
    }

    public void encryptEntry(char[] safePassword) {
        SecretKey aesKey = Crypto.generateKey(safePassword, this.salt, Crypto.KEY_LENGTH, Crypto.NUMBER_OF_ITERATIONS);

        Crypto.encryptAES(this, aesKey);
    }

    public void decryptEntry(char[] safePassword) throws BadPaddingException {
        SecretKey aesKey = Crypto.generateKey(safePassword, this.salt, Crypto.KEY_LENGTH, Crypto.NUMBER_OF_ITERATIONS);

        Crypto.decryptAES(this, aesKey);
    }
}