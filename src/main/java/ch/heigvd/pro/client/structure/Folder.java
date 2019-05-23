package ch.heigvd.pro.client.structure;

import javax.crypto.BadPaddingException;
import java.util.List;

public class Folder {
    private int idFolder;
    private String name;
    private List<Entry> entrylist;
    private boolean groupFolder;

    public Folder(String name, List<Entry> entrylist) {
        this.name = name;
        this.entrylist = entrylist;
    }

    public Folder(String name, List<Entry> entrylist, int idFolder) {
        this.name = name;
        this.entrylist = entrylist;
        this.idFolder = idFolder;
    }

    public List<Entry> getEntrylist() {
        return entrylist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEntry(Entry newEntry) {
        this.entrylist.add(newEntry);
    }

    public int getId() {
        return idFolder;
    }

    public void removeEntry(int index) {
        this.entrylist.remove(index);
    }

    public boolean isGroupFolder() {
        return groupFolder;
    }

    public void setGroupFolder(boolean groupFolder) {
        this.groupFolder = groupFolder;
    }

    public void encryptFolder(char[] safePassword) {
        for (Entry entry : entrylist) {
            entry.encryptEntry(safePassword);
        }
    }

    public void decryptFolder(char[] safePassword) throws BadPaddingException {
        for (Entry entry : entrylist) {
            entry.decryptEntry(safePassword);
        }
    }
}
