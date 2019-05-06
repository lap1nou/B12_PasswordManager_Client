package ch.heigvd.pro.gui;

// inspiration : https://www.youtube.com/watch?v=G1Zo3UKzB4A

import javax.swing.*;

public class GUIManager {
    public void start() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginGUI myLoginGUI = new LoginGUI();
            }
        });
    }
}
