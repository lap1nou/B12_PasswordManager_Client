package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.IStorePasswordDriver;
import ch.heigvd.pro.client.password.PasswordChecker;
import ch.heigvd.pro.client.password.PasswordGenerator;
import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Safe;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;

public class EntryGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel centerPanel;
    private JTextArea notesField;
    private JFormattedTextField usernameField;
    private JFormattedTextField entryNameField;
    private JPasswordField passwordField;
    private JPasswordField RetypePasswordField;
    private JFormattedTextField targetField;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton showButton;
    private JButton autoGenerateButton;
    private JProgressBar scoreProgress;
    private JLabel imageLabel;
    private JButton browseButton;
    private JButton deleteButton;

    private String iconFilename = "default.png";

    // TODO: The OK button is useless, remove it or remove cancel button
    public EntryGUI(Safe safe, int selectedFolderNumber, int entryNumber, HomePageGUI homepage, IStorePasswordDriver serverDriver) {
        /*
         * Edit entry
         */
        $$$setupUI$$$();
        if (entryNumber != -1) {
            if (selectedFolderNumber != -1) {
                this.iconFilename = safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber).getIcon();

                ImageIcon myPicture = new ImageIcon(iconFilename);
                imageLabel.setIcon(new ImageIcon(myPicture.getImage().getScaledInstance(24, 24, Image.SCALE_FAST)));

                CharBuffer charBuffer = CharBuffer.wrap(safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber).getEntryName());
                entryNameField.setText(charBuffer.toString());

                charBuffer = CharBuffer.wrap(safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber).getTarget());
                targetField.setText(charBuffer.toString());

                charBuffer = CharBuffer.wrap(safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber).getUsername());
                usernameField.setText(charBuffer.toString());

                charBuffer = CharBuffer.wrap(safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber).getClearPassword());
                passwordField.setText(charBuffer.toString());

                charBuffer = CharBuffer.wrap(safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber).getClearPassword());
                RetypePasswordField.setText(charBuffer.toString());

                charBuffer = CharBuffer.wrap(safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber).getNotes());
                notesField.setText(charBuffer.toString());

                // TODO Fix bug where notes is disappearing because of that line
                // Wiping sensible data
                //Arrays.fill(charBuffer.array(), (char) 0);
            }
        }

        /*
         * Initialize frame
         */
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        setTitle("Entry");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(600, 450);
        setResizable(false);
        //pack();
        SwingUtilities.getRootPane(saveButton).setDefaultButton(saveButton);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        scoreProgress.setValue(0);
        scoreProgress.setStringPainted(true);
        setVisible(true);

        // Source: https://stackoverflow.com/questions/19538040/java-progressbar-opened-after-calculating
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int scorePassword = PasswordChecker.checkStrong(passwordField.getPassword());

                    if (scorePassword != scoreProgress.getValue()) {
                        /*
                        if (scorePassword == -1) {
                            leakedLabel.setVisible(true);
                        } else {
                            leakedLabel.setVisible(false);
                        }*/

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
         * On click on button show it will show the password
         */
        showButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                passwordField.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                passwordField.setEchoChar('*');
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                homepage.deleteSelectedEntry();
                homepage.setEnabled(true);
                dispose();
            }
        });

        /*
         * Show password
         */
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (passwordField.getPassword().length != 0) {
                    RetypePasswordField.setEditable(true);
                }

                if (RetypePasswordField.getPassword().length != 0) {
                    RetypePasswordField.setText("");
                }
            }
        });

        /*
         *
         */
        /*RetypePasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                char[] pass1 = passwordField.getPassword();
                char[] pass2 = RetypePasswordField.getPassword();
                boolean different = false;

                // Check same length
                if (pass1.length == pass2.length) {
                    // CHeck same length and same chars
                    for (int i = 0; i < pass1.length; ++i) {
                        if (pass1[i] != pass2[i]) {
                            different = true;
                        }
                    }
                } else {
                    different = true;
                }

                if (different) {
                    JOptionPane.showMessageDialog(null, "Passwords must be the same");
                    passwordField.setText("");
                    RetypePasswordField.setText("");
                }
                RetypePasswordField.setEditable(false);

                // TODO : Clean with Array.fill()
                // Clean password arrays
                for (int i = 0; i < pass1.length; i++) {
                    pass1[i] = '\0';
                }

                for (int i = 0; i < pass2.length; i++) {
                    pass2[i] = '\0';
                }
            }
        });*/

        /*
         * Create/Edit the entry
         */
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try {
                    // Verify that all fields are filled
                    if (!usernameField.getText().isEmpty()
                            && passwordField.getPassword().length != 0
                            && RetypePasswordField.getPassword().length != 0
                            && !entryNameField.getText().isEmpty() && entryNumber == -1) { // Create

                        // Create a new Entry
                        Entry newEntry = new Entry(0, entryNameField.getText().toCharArray(),
                                usernameField.getText().toCharArray(), targetField.getText().toCharArray(),
                                passwordField.getPassword(), notesField.getText().toCharArray(), new Date());

                        newEntry.setIcon(iconFilename);

                        try {
                            // Add the entry
                            serverDriver.addEntry(newEntry, selectedFolderNumber);

                            JOptionPane.showMessageDialog(null,
                                    "The entry has been created",
                                    "Created entry",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(EntryGUI.this,
                                    e.getMessage(),
                                    "Entry error",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } else if (entryNumber != -1) { // Edit
                        Entry actualEntry = safe.getFolderList().get(selectedFolderNumber).getEntrylist().get(entryNumber);
                        Entry editedEntry = new Entry(notesField.getText().toCharArray(), actualEntry.getPassword(), actualEntry.getSalt(), iconFilename, actualEntry.getId(), entryNameField.getText().toCharArray(), actualEntry.getIv(), targetField.getText().toCharArray(), usernameField.getText().toCharArray());
                        editedEntry.setClearPassword(passwordField.getPassword());

                        serverDriver.editEntry(actualEntry, editedEntry);
                    }

                    serverDriver.saveSafe();

                    // Saving current selected folder
                    int tmp = selectedFolderNumber;

                    // Refreshing JTree and JTable
                    homepage.InitGroupTree();
                    homepage.refreshTable();
                    homepage.setEnabled(true);
                    homepage.userTree.setSelectionRow(tmp + 1);

                    dispose();

                } catch (
                        Exception e) {
                    e.printStackTrace();
                }

            }
        });

        /*
         * Generate password
         */
        autoGenerateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PasswordGenerator passwordGenerator = new PasswordGenerator("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"#$%&')(*+,-_./".toCharArray(), 15);
                char[] generatedPassword = passwordGenerator.generatePassword();
                passwordField.setText(String.valueOf(generatedPassword));
                RetypePasswordField.setText(String.valueOf(generatedPassword));
                Arrays.fill(generatedPassword, (char) 0);
            }
        });

        /*
         * Get icon picture
         */
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                int ret = chooser.showOpenDialog(null);

                if (ret == JFileChooser.APPROVE_OPTION) {
                    String filename = chooser.getSelectedFile().getAbsolutePath();

                    ImageIcon myPicture = new ImageIcon(filename);
                    setIconFilename(filename);
                    imageLabel.setIcon(new ImageIcon(myPicture.getImage().getScaledInstance(24, 24, Image.SCALE_FAST)));

                }
            }
        });
    }

    private void createUIComponents() {
        // Source: https://stackoverflow.com/questions/10051638/updating-an-image-contained-in-a-jlabel-problems
        ImageIcon myPicture = new ImageIcon(iconFilename);
        imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(myPicture.getImage().getScaledInstance(24, 24, Image.SCALE_FAST)));
    }

    // Getters
    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setIconFilename(String iconFilename) {
        this.iconFilename = iconFilename;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        westPanel = new JPanel();
        westPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(westPanel, BorderLayout.WEST);
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
        saveButton = new JButton();
        saveButton.setText("Save");
        southPanel.add(saveButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Delete");
        southPanel.add(deleteButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayoutManager(8, 5, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Entry Name:");
        centerPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        centerPanel.add(spacer2, new GridConstraints(7, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Notes:");
        centerPanel.add(label2, new GridConstraints(5, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        notesField = new JTextArea();
        notesField.setText("");
        centerPanel.add(notesField, new GridConstraints(6, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        usernameField = new JFormattedTextField();
        centerPanel.add(usernameField, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("User Name:");
        centerPanel.add(label3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        entryNameField = new JFormattedTextField();
        centerPanel.add(entryNameField, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordField = new JPasswordField();
        centerPanel.add(passwordField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Password:");
        centerPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Target:");
        centerPanel.add(label5, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        targetField = new JFormattedTextField();
        centerPanel.add(targetField, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Retype Password:");
        centerPanel.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        RetypePasswordField = new JPasswordField();
        RetypePasswordField.setEditable(false);
        RetypePasswordField.setText("");
        centerPanel.add(RetypePasswordField, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        autoGenerateButton = new JButton();
        autoGenerateButton.setText("Auto Generate");
        centerPanel.add(autoGenerateButton, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageLabel.setText("");
        centerPanel.add(imageLabel, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseButton = new JButton();
        browseButton.setText("Browse image");
        centerPanel.add(browseButton, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        showButton = new JButton();
        showButton.setText("Show");
        centerPanel.add(showButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scoreProgress = new JProgressBar();
        centerPanel.add(scoreProgress, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Strengh:");
        centerPanel.add(label7, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
