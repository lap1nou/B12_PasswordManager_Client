package ch.heigvd.pro.core;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JPanel centerPanel;

    public CreateMasterKeyGUI() {

        // Frame initialisations
        setTitle("Create Master Key");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(500, 300);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Listeners
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
