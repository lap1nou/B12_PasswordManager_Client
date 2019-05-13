package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.password.PasswordChecker;
import ch.heigvd.pro.client.structure.Safe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class CreateMasterKeyGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JTextArea textArea1;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatField;
    private JComboBox comboBox1;
    private JButton helpButton;
    private JButton cancelButton;
    private JButton confirmButton;
    private JPanel centerPanel;
    private JFormattedTextField fileNameField;

    public CreateMasterKeyGUI() {

        // Frame initialisations
        setTitle("Create Master Key");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(500, 350);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                File passwordDB = new File("");
                Safe safe = new Safe();
                FileDriver fileDrive = new FileDriver(safe, passwordDB);
                int scorePassword = PasswordChecker.checkStrong(passwordField.getPassword());
                //scoreProgress.setValue(scorePassword);

                passwordDB = new File(fileNameField.getText() + ".json");
                try {
                    passwordDB.createNewFile();
                    safe.setSafePassword(passwordField.getPassword());
                    fileDrive.saveSafe();
                    HomePageGUI myHomePageGUI = new HomePageGUI(safe, fileNameField.getText());
                    dispose();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Listeners
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
