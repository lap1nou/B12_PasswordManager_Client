package ch.heigvd.pro.client;

import java.util.ArrayList;
import java.util.List;

public class Safe {
    private List<Entry> entryList;

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
}
