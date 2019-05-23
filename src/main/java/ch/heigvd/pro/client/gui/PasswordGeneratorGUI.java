package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.password.PasswordChecker;
import ch.heigvd.pro.client.password.PasswordGenerator;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.CharBuffer;
import java.text.NumberFormat;

public class PasswordGeneratorGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JCheckBox upperCaseAZCheckBox;
    private JFormattedTextField length;
    private JFormattedTextField passwordField;
    private JPanel centerPanel;
    private JCheckBox lowerCaseAZCheckBox;
    private JCheckBox numbers09CheckBox;
    private JCheckBox specialCheckBox;
    private JButton copyButton;
    private JButton generateButton;
    private JButton cancelButton;
    private JButton generatePassword;
    private JProgressBar passwordProgressBar;
    private JLabel leakedLabel;
    private JFrame frame;

    private String alphabetLowercase = "abcdefghijklmnopqrstuvwxyz";
    private String alphabetUppercase = "ABCDEFGHIJKLMNOPQRSTUVWXY";
    private String numeric = "0123456789";
    private String specialCharacter = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public PasswordGeneratorGUI() {

        // Frame initialisation
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        setTitle("Password Generator");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(450, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        passwordProgressBar.setStringPainted(true);

        /**
         * generate password
         */
        generatePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String parameterPassword = "";

                    if (lowerCaseAZCheckBox.isSelected()) {
                        parameterPassword += alphabetLowercase;
                    }
                    if (upperCaseAZCheckBox.isSelected()) {
                        parameterPassword += alphabetUppercase;
                    }
                    if (numbers09CheckBox.isSelected()) {
                        parameterPassword += numeric;
                    }
                    if (specialCheckBox.isSelected()) {
                        parameterPassword += specialCharacter;
                    }

                    PasswordGenerator passwordGenerator = new PasswordGenerator(parameterPassword.toCharArray(), Integer.parseInt(length.getText()));

                    CharBuffer charBuffer = CharBuffer.wrap(passwordGenerator.generatePassword());

                    passwordField.setValue(charBuffer.toString());

                    passwordField.setValue(charBuffer.toString());

                    int passwordStrength = PasswordChecker.checkStrong(charBuffer.array());
                    passwordProgressBar.setValue(passwordStrength);
                    passwordProgressBar.setString(passwordProgressBar.getValue() + "%");

                    if (passwordStrength == -1) {
                        leakedLabel.setVisible(true);
                    } else {
                        leakedLabel.setVisible(false);
                    }

                    if (passwordProgressBar.getValue() < 25) {
                        passwordProgressBar.setForeground(Color.RED);
                    } else if (passwordProgressBar.getValue() <= 50) {
                        passwordProgressBar.setForeground(Color.MAGENTA);
                    } else if (passwordProgressBar.getValue() <= 75) {
                        passwordProgressBar.setForeground(Color.ORANGE);
                    } else {
                        passwordProgressBar.setForeground(Color.GREEN);
                    }

                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(frame,
                            "The length value must be a positive number !",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException iae) {
                    JOptionPane.showMessageDialog(frame,
                            "You need to choose minimum 1 element",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        /**
         * Button that will copy the password
         * source : https://stackoverflow.com/questions/24702434/copy-text-to-clipboard-from-a-jtextfield-with-press-of-a-button
         */
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                StringSelection stringSelection = new StringSelection(passwordField.getText());
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });
    }

    // Getters
    public JButton getCancelButton() {
        return cancelButton;
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
        eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(eastPanel, BorderLayout.EAST);
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayoutManager(1, 3, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        passwordProgressBar = new JProgressBar();
        southPanel.add(passwordProgressBar, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leakedLabel = new JLabel();
        leakedLabel.setForeground(new Color(-4520443));
        leakedLabel.setText("Password leaked");
        leakedLabel.setVisible(false);
        southPanel.add(leakedLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelButton = new JButton();
        cancelButton.setText("OK");
        southPanel.add(cancelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        westPanel = new JPanel();
        westPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(westPanel, BorderLayout.WEST);
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayoutManager(6, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        upperCaseAZCheckBox = new JCheckBox();
        upperCaseAZCheckBox.setText("UpperCase (A - Z)");
        centerPanel.add(upperCaseAZCheckBox, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lowerCaseAZCheckBox = new JCheckBox();
        lowerCaseAZCheckBox.setText("LowerCase (a - z)");
        centerPanel.add(lowerCaseAZCheckBox, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numbers09CheckBox = new JCheckBox();
        numbers09CheckBox.setText("Numbers (0 - 9)");
        centerPanel.add(numbers09CheckBox, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        specialCheckBox = new JCheckBox();
        specialCheckBox.setText("Special (@,#,,%,...)");
        specialCheckBox.setMnemonic(',');
        specialCheckBox.setDisplayedMnemonicIndex(13);
        centerPanel.add(specialCheckBox, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Password:");
        centerPanel.add(label1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        copyButton = new JButton();
        copyButton.setText("Copy");
        centerPanel.add(copyButton, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Password length:");
        centerPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        length = new JFormattedTextField();
        centerPanel.add(length, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        generatePassword = new JButton();
        generatePassword.setText("Generate");
        centerPanel.add(generatePassword, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JFormattedTextField();
        passwordField.setText("");
        centerPanel.add(passwordField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}