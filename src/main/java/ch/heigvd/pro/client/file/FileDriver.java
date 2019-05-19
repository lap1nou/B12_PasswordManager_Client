package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Folder;
import ch.heigvd.pro.client.structure.Safe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.BadPaddingException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Source : https://www.tutorialspoint.com/gson/gson_object_serialization.htm
public class FileDriver implements IStorePasswordDriver {

    private File file;
    private Safe safe;

    public FileDriver(Safe safe, File file) {
        this.file = file;
        this.safe = safe;
    }

    /**
     * Load JSON from a file and return the Safe object of the JSON data.
     *
     * @return a Safe object, unserialized from the JSON file
     */
    public Safe loadSafe() {
        try {
            FileReader fr = new FileReader(file);
            GsonBuilder builder = new GsonBuilder();
            Gson parser = builder.create();

            return parser.fromJson(fr, Safe.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Save a Safe object into a JSON format.
     */
    public void saveSafe() {
        safe.encryptPassword();
        try {
            FileWriter fw = new FileWriter(file, false);
            GsonBuilder builder = new GsonBuilder();
            Gson parser = builder.create();

            fw.write(parser.toJson(safe));
            fw.close();

            safe.decryptPassword();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Safe login(char[] username, char[] password) throws Exception {
        return null;
    }

    @Override
    public void createUser(char[] username, char[] email, char[] password) throws Exception {

    }

    @Override
    public void createFolder(String folderName) throws Exception {
        safe.getFolderList().add(new Folder(folderName, new ArrayList<Entry>()));
    }

    @Override
    public void addEntry(Entry newEntry, int selectedFolderNumber) throws Exception {
        this.safe.getFolderList().get(selectedFolderNumber).addEntry(newEntry);
    }

    @Override
    public void editEntry(Entry actualEntry, Entry editedEntry) throws Exception {
        actualEntry.setUsername(editedEntry.getUsername());
        actualEntry.setEntryName(editedEntry.getEntryName());
        actualEntry.setClearPassword(editedEntry.getClearPassword());
        actualEntry.setTarget(editedEntry.getTarget());
        actualEntry.setNotes(editedEntry.getNotes());
        actualEntry.setIcon(editedEntry.getIcon());
    }

    @Override
    public void deleteEntry(int selectedFolderNumber, int indexOfEntryToRemove) throws Exception {
        safe.getFolderList().get(selectedFolderNumber).removeEntry(indexOfEntryToRemove);
    }

    @Override
    public void createGroupe(char[] groupName) throws Exception {

    }

    public Safe getSafe() {
        return safe;
    }

    public void setSafe(Safe safe) {
        this.safe = safe;
    }
}
