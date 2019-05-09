package ch.heigvd.pro.client.structure;

import ch.heigvd.pro.client.Utils;
import ch.heigvd.pro.client.crypto.Crypto;
import ch.heigvd.pro.client.password.PasswordChecker;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;
import java.util.Date;

public class Entry {
    private int id;
    private char[] entryName;
    private char[] username;
    private char[] target;
    private transient char[] clearPassword;
    private char[] encryptedPassword;
    private byte[] iv;
    private char[] salt;
    private int strength;
    private String icon;
    private char[] notes;
    private Date registerDate;

    public Entry(int id, char[] entryname, char[] username, char[] target, char[] clearPassword, char[] notes, Date registerDate) {
        this.entryName = entryname;
        this.id = id;
        this.username = username;
        this.target = target;
        this.clearPassword = clearPassword;
        this.strength = PasswordChecker.checkStrong(clearPassword);
        this.registerDate = registerDate;
        this.salt = Utils.byteToCharArray(Crypto.generateSalt(Crypto.SALT_BYTE_LENGTH));
        this.notes = notes;
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

    public Date getRegisterDate() {
        return registerDate;
    }

    public char[] getEntryName() {
        return entryName;
    }

    public void setEntryName(char[] entryName) {
        this.entryName = entryName;
    }

    public char[] getNotes() {
        return notes;
    }

    public void setNotes(char[] notes) {
        this.notes = notes;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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