package ch.heigvd.pro.client.file;

import ch.heigvd.pro.client.structure.Safe;

import java.io.File;

public interface IStorePasswordDriver {
    // Paramètres dans le constructeur
    // Virer file
    Safe loadSafe();

    // Virer file
    void saveSafe();
}