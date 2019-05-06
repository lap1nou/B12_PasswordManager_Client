package ch.heigvd.pro.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JButton cancelButton;
    private JButton registerButton;
    private JLabel label;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel centerPanel;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField formattedTextField2;
    private JFormattedTextField formattedTextField3;
    private JFormattedTextField formattedTextField4;
    private JFormattedTextField formattedTextField5;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private Color oldForegroundLabel;

    public RegisterGUI() {

        // Frame initialisation
        setTitle("Register");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(300, 400);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Listeners
        passwordField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(passwordField1.getPassword().length != 0) {
                    passwordField2.setEditable(true);
                }

                if(passwordField2.getPassword().length != 0) {
                    passwordField2.setText("");
                }
            }
        });

        passwordField2.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                char[] pass1 = passwordField1.getPassword();
                char[] pass2 = passwordField2.getPassword();
                boolean different = false;

                // Check same length
                if(pass1.length == pass2.length) {
                    // CHeck same length and same chars
                    for(int i = 0; i < pass1.length; ++i) {
                        if(pass1[i] != pass2[i]) {
                            different = true;
                        }
                    }
                } else {
                    different = true;
                }

                if(different) {
                    JOptionPane.showMessageDialog(null, "Passwords must be the same");
                    passwordField1.setText("");
                    passwordField2.setText("");

                }
                passwordField2.setEditable(false);

                // Clean password arrays
                for (int i = 0; i < pass1.length; i++) {
                    pass1[i] = '\0';
                }
                for (int i = 0; i < pass2.length; i++) {
                    pass2[i] = '\0';
                }
            }
        });

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                LoginGUI myLoginGUI = new LoginGUI();
                myLoginGUI.setVisible(true);
                dispose();
            }
        });

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                oldForegroundLabel = label.getForeground();
                label.setForeground(Color.BLUE);
            }
        });

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                label.setForeground(oldForegroundLabel);

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI myLoginGUI = new LoginGUI();
                myLoginGUI.setVisible(true);
                dispose();
            }
        });
    }
}
