package ch.heigvd.pro.client.structure;

import java.util.List;

public class Folder {
    private String name;
    private List<Entry> entrylist;

    public Folder(String name, List<Entry> entrylist) {
        this.name = name;
        this.entrylist = entrylist;
    }

    public List<Entry> getEntrylist() {
        return entrylist;
    }

    public String getName() {
        return name;
    }

    public void addEntry(Entry newEntry) {
        this.entrylist.add(newEntry);
    }

    public void removeEntry(int index) {
        this.entrylist.remove(index);
    }
}
