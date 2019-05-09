package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.password.PasswordGenerator;

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
    private JButton okButton;
    private JButton generatePassword;
    private JFrame frame;

    private String alphabetLowercase = "abcdefghijklmnopqrstuvwxyz";
    private String alphabetUppercase = "ABCDEFGHIJKLMNOPQRSTUVWXY";
    private String numeric = "0123456789";
    private String specialCharacter = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public PasswordGeneratorGUI() {

        // Frame initialisation
        setTitle("Password Generator");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(450, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);


        generatePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    String parameterPassword = "";

                    if(lowerCaseAZCheckBox.isSelected()){
                        parameterPassword += alphabetLowercase;
                    }
                    if(upperCaseAZCheckBox.isSelected()){
                        parameterPassword += alphabetUppercase;
                    }
                    if(numbers09CheckBox.isSelected()){
                        parameterPassword += numeric;
                    }
                    if(specialCheckBox.isSelected()){
                        parameterPassword += specialCharacter;
                    }

                    PasswordGenerator passwordGenerator = new PasswordGenerator(parameterPassword.toCharArray(),Integer.parseInt(length.getText()));

                    CharBuffer charBuffer = CharBuffer.wrap(passwordGenerator.generatePassword());

                    passwordField.setValue(charBuffer.toString());
                } catch(NumberFormatException nfe){
                    JOptionPane.showMessageDialog(frame,
                            "The length value must be a positive number !",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                } catch(IllegalArgumentException iae){
                    JOptionPane.showMessageDialog(frame,
                            "You need to choose minimum 1 element",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        /*
         * source : https://stackoverflow.com/questions/24702434/copy-text-to-clipboard-from-a-jtextfield-with-press-of-a-button
         */
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                StringSelection stringSelection = new StringSelection (passwordField.getText());
                Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
                clpbrd.setContents (stringSelection, null);
            }
        });
    }

    // Getters
    public JButton getCancelButton() {
        return cancelButton;
    }
}