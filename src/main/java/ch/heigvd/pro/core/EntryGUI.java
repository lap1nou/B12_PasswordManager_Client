package ch.heigvd.pro.core;

import javax.swing.*;
import java.awt.event.*;

public class EntryGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel centerPanel;
    private JTextArea textArea1;
    private JFormattedTextField formattedTextField1;
    private JFormattedTextField formattedTextField2;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JFormattedTextField formattedTextField3;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton showButton;
    private JButton autoGenerateButton;
    private JProgressBar progressBar1;

    public EntryGUI() {

        // Frame initialisations
        setTitle("Entry");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(600, 450);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        progressBar1.setValue(50);
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
    }

    // Getters
    public JButton getCancelButton() {
        return cancelButton;
    }
}
