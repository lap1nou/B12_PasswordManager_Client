package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.password.PasswordChecker;
import ch.heigvd.pro.client.structure.Safe;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

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
    private JProgressBar scoreProgress;

    public CreateMasterKeyGUI() {

        /**
         * Initialize frame
         */
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        setTitle("Create Master Key");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(500, 350);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        /*
         * A revoir

        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                int scorePassword = PasswordChecker.checkStrong(passwordField.getPassword());
                scoreProgress.setValue(scorePassword);
                System.out.println(scorePassword);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                int scorePassword = PasswordChecker.checkStrong(passwordField.getPassword());
                scoreProgress.setValue(scorePassword);
                System.out.println(scorePassword);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                System.out.println("changedUpdate");
            }
        });
        */

        /**
         * Action the confirmation button
         */
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if(passwordField.getPassword().equals(passwordRepeatField.getPassword())){
                        throw new Exception("The passwords is not identicaly");
                    }
                    // Initialize all value
                    File passwordDB = new File("");
                    Safe safe = new Safe();
                    FileDriver fileDrive = new FileDriver(safe, passwordDB);

                    // Create the file and open the home directory
                    passwordDB = new File(fileNameField.getText() + ".json");
                    passwordDB.createNewFile();
                    safe.setSafePassword(passwordField.getPassword());
                    fileDrive.saveSafe();
                    HomePageGUI myHomePageGUI = new HomePageGUI(safe, fileNameField.getText());
                    dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Cancel the actuel page
         */
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI myLoginGUI = new LoginGUI();
                myLoginGUI.setVisible(true);
                dispose();
            }
        });
    }
}
