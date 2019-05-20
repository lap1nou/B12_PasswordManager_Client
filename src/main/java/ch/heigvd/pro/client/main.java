package ch.heigvd.pro.client;

import ch.heigvd.pro.client.gui.GUIManager;


import javax.swing.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUIManager newGUIMan = new GUIManager();
        newGUIMan.start();
    }
}

