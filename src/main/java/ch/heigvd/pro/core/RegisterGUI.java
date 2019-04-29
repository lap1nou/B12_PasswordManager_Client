package ch.heigvd.pro.core;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel southPanel;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField formattedTextField2;
    private JFormattedTextField formattedTextField3;
    private JFormattedTextField formattedTextField4;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JFormattedTextField formattedTextField5;
    private JButton cancelButton;
    private JButton registerButton;
    private JLabel label;

    public RegisterGUI() {

        // Frame initialisation
        setTitle("Register");
        add(mainPanel);
        setLocationRelativeTo(null);
        //setSize(500, 300);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Listeners
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                LoginGUI myLoginGUI = new LoginGUI();
                myLoginGUI.setVisible(true);
                dispose();
            }
        });
    }
}
