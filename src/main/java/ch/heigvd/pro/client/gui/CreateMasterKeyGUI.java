package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.password.PasswordChecker;
import ch.heigvd.pro.client.structure.Safe;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.Arrays;
import java.util.concurrent.Executors;

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
    private JButton browseButton;
    private JLabel leakedLabel;
    private JFrame frame;

    private String databasePath = "";

    public CreateMasterKeyGUI() {
        /*
         * Initialize frame
         */
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        setTitle("Create Master Key");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(500, 350);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        scoreProgress.setStringPainted(true);
        scoreProgress.setForeground(Color.RED);

        // Source: https://stackoverflow.com/questions/19538040/java-progressbar-opened-after-calculating
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int scorePassword = PasswordChecker.checkStrong(passwordField.getPassword());

                    if (scorePassword != scoreProgress.getValue()) {
                        if (scorePassword == -1) {
                            leakedLabel.setVisible(true);
                        } else {
                            leakedLabel.setVisible(false);
                        }

                        scoreProgress.setValue(scorePassword);
                        scoreProgress.setString(scoreProgress.getValue() + "%");

                        if (scoreProgress.getValue() < 25) {
                            scoreProgress.setForeground(Color.RED);
                        } else if (scoreProgress.getValue() <= 50) {
                            scoreProgress.setForeground(Color.MAGENTA);
                        } else if (scoreProgress.getValue() <= 75) {
                            scoreProgress.setForeground(Color.ORANGE);
                        } else {
                            scoreProgress.setForeground(Color.GREEN);
                        }
                    }
                }
            }
        });

        /*
         * Action the confirmation button
         */
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (!Arrays.equals(passwordField.getPassword(), passwordRepeatField.getPassword())) {
                        passwordField.setText("");
                        passwordRepeatField.setText("");

                        throw new Exception("Passwords are not identical");
                    }

                    // Create the File and the Safe
                    File passwordDB = new File(databasePath + fileNameField.getText() + ".json");
                    System.out.println(passwordDB.getPath());

                    passwordDB.createNewFile();
                    Safe safe = new Safe();

                    FileDriver fileDrive = new FileDriver(safe, passwordDB);
                    safe.setSafePassword(passwordField.getPassword());
                    fileDrive.setSafe(safe);

                    HomePageGUI myHomePageGUI = new HomePageGUI(fileDrive);

                    dispose();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            "Creation error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /*
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

        /**
         * Help button display some extra informations about the importance of a good masterpassword
         */
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "If you are not sure about what is a good master password\n" +
                        "you could visit this web site https://medium.com/edgefund/choosing-a-master-password-5d585b2ba568\n" +
                        "or searching on the web");
            }
        });

        /**
         * Browse a path where to create the database
         */
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser databaseFolderChooser = new JFileChooser();
                databaseFolderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                databaseFolderChooser.setAcceptAllFileFilterUsed(false);

                int ret = databaseFolderChooser.showOpenDialog(null);

                if (ret == JFileChooser.APPROVE_OPTION) {
                    CreateMasterKeyGUI.this.databasePath = databaseFolderChooser.getSelectedFile().getAbsolutePath() + "/";
                }
            }
        });
    }
}
