package ch.heigvd.pro.client.gui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JButton cancelButton;

    public PasswordGeneratorGUI() {

        // Frame initialisation
        setTitle("Password Generator");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(450, 400);
        setResizable(false);
        //pack();
        passwordField1.setText("test");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Listeners
        showButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                passwordField1.setEchoChar((char)0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                passwordField1.setEchoChar('*');
            }
        });
    }

    // Getters
    public JButton getCancelButton() {
        return cancelButton;
    }
}