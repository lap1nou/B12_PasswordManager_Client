package ch.heigvd.pro.core;

import javax.swing.*;

public class CreateMasterKeyGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JTextArea textArea1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JComboBox comboBox1;
    private JButton helpButton;
    private JButton cancelButton;
    private JButton confirmButton;

    public CreateMasterKeyGUI(String title) {
        super(title);
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(500, 300);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
