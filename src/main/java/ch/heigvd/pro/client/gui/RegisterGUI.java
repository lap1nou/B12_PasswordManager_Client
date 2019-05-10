package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.ServerDriver;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.UnsupportedEncodingException;

public class RegisterGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JButton cancelButton;
    private JButton registerButton;
    private JPanel westPanel;
    private JPanel eastPanel;
    private JPanel centerPanel;
    private JFormattedTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField passwordRepeatField;
    private JFormattedTextField emailField;
    private Color oldForegroundLabel;
    private JFrame frame;

    public RegisterGUI() {

        // Frame initialisation
        setTitle("Register");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(300, 350);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Listeners
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(passwordField.getPassword().length != 0) {
                    passwordRepeatField.setEditable(true);
                }

                if(passwordRepeatField.getPassword().length != 0) {
                    passwordRepeatField.setText("");
                }
            }
        });

        passwordRepeatField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                char[] pass1 = passwordField.getPassword();
                char[] pass2 = passwordRepeatField.getPassword();
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
                    passwordField.setText("");
                    passwordRepeatField.setText("");

                }
                passwordRepeatField.setEditable(false);

                // Clean password arrays
                for (int i = 0; i < pass1.length; i++) {
                    pass1[i] = '\0';
                }
                for (int i = 0; i < pass2.length; i++) {
                    pass2[i] = '\0';
                }
            }
        });

        /**
         * On click on button register
         */
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ServerDriver newUser = new ServerDriver();
                try {
                    JSONObject created = newUser.createUser(usernameField.getText(), emailField.getText(), passwordField.getText());
                    if(created.get("errorCode").equals(0)){
                        JOptionPane.showMessageDialog(frame,
                                created.get("message"),
                                "Succed",
                                JOptionPane.INFORMATION_MESSAGE);
                        newUser = null;
                        LoginGUI myLoginGUI = new LoginGUI();
                        myLoginGUI.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                created.get("message"),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.toString(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
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
