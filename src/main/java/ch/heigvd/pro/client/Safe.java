package ch.heigvd.pro.client;

import ch.heigvd.pro.client.crypto.Crypto;

import javax.crypto.*;
import java.util.ArrayList;
import java.util.List;

public class Safe {
    private List<Entry> entryList;
    private transient char[] safePassword;

    public Safe() {
        entryList = new ArrayList<Entry>();
    }

    public void addEntry(Entry entry) {
        this.entryList.add(entry);
    }

    // TODO : Deep copy
    public List<Entry> getEntryList() {
        return entryList;
    }

    public char[] getSafePassword() {
        return safePassword;
    }

    public void setSafePassword(char[] safePassword) {
        this.safePassword = safePassword;
    }

    /**
     * This function encrypt all the password inside the Safe using the Safe key.
     */
    public void encryptPassword() {
        SecretKey aesKey = Crypto.generateKey(this.safePassword, "salt".toCharArray(), 256, 100);

        for (Entry entry : this.entryList) {
            Crypto.encryptAES(entry, aesKey);
        }
    }

    /**
     * This function decrypt all the encrypted password inside the Safe using the Safe key.
     */
    public void decryptPassword() {
        SecretKey aesKey = Crypto.generateKey(this.safePassword, "salt".toCharArray(), 256, 100);

        for (Entry entry : this.entryList) {
            Crypto.decryptAES(entry, aesKey);
        }
    }
}
