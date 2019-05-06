package ch.heigvd.pro.client.structure;

import ch.heigvd.pro.client.crypto.Crypto;

import javax.crypto.*;
import java.util.ArrayList;
import java.util.List;

public class Safe {
    private List<Folder> folderList;
    private transient char[] safePassword;
    private transient boolean encrypted = true;

    public Safe() {
        folderList = new ArrayList<Folder>();
    }

    public void addFolder(Folder folder) {
        this.folderList.add(folder);
    }

    // TODO : Deep copy
    public List<Folder> getFolderList() {
        return folderList;
    }

    public void setSafePassword(char[] safePassword) {
        this.safePassword = safePassword;
    }

    /**
     * This function encrypt all the password inside the Safe using the Safe key.
     */
    public void encryptPassword() {
        if (!encrypted) {
            encrypted = true;
            SecretKey aesKey = Crypto.generateKey(this.safePassword, "salt".toCharArray(), 256, 100);

            for (Folder folder : this.folderList) {
                for (Entry entry : folder.getEntrylist()) {
                    Crypto.encryptAES(entry, aesKey);
                }
            }
        }
    }

    /**
     * This function decrypt all the encrypted password inside the Safe using the Safe key.
     */
    public void decryptPassword() throws BadPaddingException {
        if (encrypted) {
            encrypted = false;
            SecretKey aesKey = Crypto.generateKey(this.safePassword, "salt".toCharArray(), 256, 100);

            for (Folder folder : this.folderList) {
                for (Entry entry : folder.getEntrylist()) {
                    Crypto.decryptAES(entry, aesKey);
                }
            }
        }
    }

    public char[] getSafePassword() {
        return safePassword;
    }
}
