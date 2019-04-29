package ch.heigvd.pro.core;

import javax.swing.*;

public class PasswordGeneratorGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JCheckBox upperCaseAZCheckBox;
    private JFormattedTextField formattedTextField1;
    private JPanel centerPanel;
    private JCheckBox lowerCaseAZCheckBox;
    private JCheckBox numbers09CheckBox;
    private JCheckBox specialCheckBox;
    private JPasswordField passwordField1;
    private JButton showButton;
    private JButton generateButton;

    public PasswordGeneratorGUI() {

        // Frame initialisation
        setTitle("Password Generator");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(450, 250);
        //setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}