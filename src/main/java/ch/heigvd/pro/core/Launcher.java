package ch.heigvd.pro.core;

// inspiration : https://www.youtube.com/watch?v=G1Zo3UKzB4A

import javax.swing.*;

public class Launcher {
    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginGUI myLoginGUI = new LoginGUI();
                myLoginGUI.setVisible(true);
            }
        });
    }
}
