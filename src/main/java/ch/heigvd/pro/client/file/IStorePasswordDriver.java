package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.Safe;

import java.io.File;

public interface IStorePasswordDriver {
    Safe loadSafe(File file);

    void saveSafe(Safe safe, File file);
}