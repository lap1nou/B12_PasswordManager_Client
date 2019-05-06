package ch.heigvd.pro;

// inspiration : https://www.youtube.com/watch?v=G1Zo3UKzB4A

import ch.heigvd.pro.gui.*;
import javax.swing.*;

public class Launcher {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        GUIManager newGUIMan = new GUIManager();
        newGUIMan.start();
    }
}
