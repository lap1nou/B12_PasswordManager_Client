package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.password.PasswordChecker;
import ch.heigvd.pro.client.structure.Safe;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        textArea1 = new JTextArea();
        textArea1.setBackground(new Color(-12828863));
        textArea1.setEditable(false);
        Font textArea1Font = this.$$$getFont$$$(null, Font.BOLD, -1, textArea1.getFont());
        if (textArea1Font != null) textArea1.setFont(textArea1Font);
        textArea1.setForeground(new Color(-522496));
        textArea1.setText("!!! Warning !!! You are about to create the master key of your password database.\nThe passphrase you will choose has to be strong enough and safetly stored.\nIf you lose your master password, you definitly lose access to your database. ");
        northPanel.add(textArea1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(eastPanel, BorderLayout.EAST);
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayoutManager(1, 4, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        southPanel.add(cancelButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        southPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        confirmButton = new JButton();
        confirmButton.setText("Confirm");
        southPanel.add(confirmButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        helpButton = new JButton();
        helpButton.setText("Help");
        southPanel.add(helpButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        westPanel = new JPanel();
        westPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(westPanel, BorderLayout.WEST);
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayoutManager(6, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Master Passphrase:");
        centerPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        centerPanel.add(spacer2, new GridConstraints(5, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setText("");
        centerPanel.add(passwordField, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Repeat Passphrase:");
        centerPanel.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordRepeatField = new JPasswordField();
        centerPanel.add(passwordRepeatField, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Encryption algorithm:");
        centerPanel.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        comboBox1 = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("AES");
        defaultComboBoxModel1.addElement("ChaCha20");
        defaultComboBoxModel1.addElement("Kamelia");
        comboBox1.setModel(defaultComboBoxModel1);
        centerPanel.add(comboBox1, new GridConstraints(4, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("File Name:");
        centerPanel.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        fileNameField = new JFormattedTextField();
        centerPanel.add(fileNameField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        scoreProgress = new JProgressBar();
        centerPanel.add(scoreProgress, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseButton = new JButton();
        browseButton.setText("Browse");
        centerPanel.add(browseButton, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        centerPanel.add(spacer3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        leakedLabel = new JLabel();
        leakedLabel.setEnabled(true);
        leakedLabel.setForeground(new Color(-4521979));
        leakedLabel.setText("Password leaked !");
        leakedLabel.setVisible(false);
        centerPanel.add(leakedLabel, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
