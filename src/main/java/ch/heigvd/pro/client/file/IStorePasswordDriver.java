package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Safe;

public interface IStorePasswordDriver {

    Safe loadSafe();

    void saveSafe();

    Safe login(char[] username, char[] password) throws Exception;

    void createUser(char[] username, char[] email, char[] password) throws Exception;

    void createFolder(char[] folderName) throws Exception;

    void addEntry(Entry newEntry, int idFolder) throws Exception;

    void editEntry(Entry entry) throws Exception;

    void deleteEntry(int idPassword) throws Exception;

    void createGroupe(char[] groupName) throws Exception;

    Safe getSafe();

    void setSafe(Safe safe);

}