package ch.heigvd.pro.client;

import ch.heigvd.pro.client.gui.GUIManager;

import javax.swing.*;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUIManager newGUIMan = new GUIManager();
        newGUIMan.start();
    }
}

