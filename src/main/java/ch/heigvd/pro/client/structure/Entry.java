package ch.heigvd.pro.client.structure;

import ch.heigvd.pro.client.Utils;
import ch.heigvd.pro.client.crypto.Crypto;
import ch.heigvd.pro.client.password.PasswordChecker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
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

    public Entry(char[] notes, char[] encryptedPassword, char[] salt , String icon, int id, char[] entryname, byte[] iv, char[] target, char[] username) {
        this.entryName = entryname;
        this.id = id;
        this.username = username;
        this.target = target;
        this.encryptedPassword = encryptedPassword;
        this.salt = salt;
        this.icon = icon;
        this.notes = notes;
        this.iv = iv;
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

    public int getIdPassword() {
        return id;
    }

    public void setIdPassword(int id) {
        this.id = id;
    }

    public String JSONentry(){
        return "{\"target\": \"" + CharBuffer.wrap(target) + "\",\"username\": \"" + CharBuffer.wrap(username) +
                "\",\"password\": \"" + CharBuffer.wrap(encryptedPassword) + "\",\"iv\": " + Arrays.toString(iv) +
                ",\"icon\": \"" + icon + "\",\"note\": \"" + CharBuffer.wrap(notes) +
                "\",\"title\": \"" + CharBuffer.wrap(entryName) + "\",\"salt\": \"" + CharBuffer.wrap(salt)  + "\" }";
    }
}