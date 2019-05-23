package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Safe;
import ch.heigvd.pro.client.structure.User;

public interface IStorePasswordDriver {

    Safe loadSafe();

    void saveSafe();

    Safe login(char[] username, char[] password) throws Exception;

    void createUser(char[] username, char[] email, char[] password) throws Exception;

    void createFolder(String folderName, int safeIndex) throws Exception;

    void deleteFolder(int index, int safeIndex) throws Exception;

    void editFolder(char[] folderName, int index, int safeIndex) throws Exception;

    void addEntry(Entry newEntry, int idFolder, int safeIndex) throws Exception;

    void editEntry(Entry actualEntry, Entry editedEntry, int safeIndex) throws Exception;

    void deleteEntry(int selectedFolderNumber, int indexOfEntryToRemove, int safeIndex) throws Exception;

    void createGroup(char[] groupName) throws Exception;

    Safe getSafe(int safeIndex);

    void addSafe(Safe safe);

    int getSafeSize();

    User getUserInformation() throws Exception;

}