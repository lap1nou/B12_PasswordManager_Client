package ch.heigvd.pro.client.structure;

import javax.crypto.*;
import java.util.ArrayList;
import java.util.List;

public class Safe {
    private List<Folder> folderList;
    private transient char[] safePassword;
    private int idUser;

    public Safe() {
        folderList = new ArrayList<Folder>();
    }

    public Safe(Safe safe) {
        this.folderList = safe.getFolderList();
        this.safePassword = safe.getSafePassword();
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
        for (Folder folder : this.folderList) {
            for (Entry entry : folder.getEntrylist()) {
                entry.encryptEntry(this.safePassword);
            }
        }
    }

    /**
     * This function decrypt all the encrypted password inside the Safe using the Safe key.
     */
    public void decryptPassword() throws BadPaddingException {

        for (Folder folder : this.folderList) {
            for (Entry entry : folder.getEntrylist()) {
                entry.decryptEntry(this.safePassword);
            }
        }
    }

    public char[] getSafePassword() {
        return safePassword;
    }

    public boolean isPasswordCorrect() {
        try {
            decryptPassword();
            encryptPassword();
        } catch (BadPaddingException e) {
            return false;
        }

        return true;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
