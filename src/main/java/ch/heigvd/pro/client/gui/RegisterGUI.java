package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.ServerDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        setTitle("Register");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(300, 350);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        /**
         * On click on button register
         */
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {

                    // Check that all field are completed
                    if(usernameField.getText().length() == 0 || emailField.getText().length() == 0|| passwordField.getPassword().length == 0|| passwordRepeatField.getPassword().length == 0){
                        throw new Exception("Please fill in all the fields");
                    }

                    // Check if the passsword and confirm password are same
                    if(!checkPasswords()){
                        throw new Exception("Passwords must be the same");
                    }

                    ServerDriver newUser = new ServerDriver();
                    newUser.createUser(usernameField.getText().toCharArray(), emailField.getText().toCharArray(), passwordField.getText().toCharArray());
                    JOptionPane.showMessageDialog(frame,
                            "User was successfuly created",
                            "Succed",
                            JOptionPane.INFORMATION_MESSAGE);
                    LoginGUI myLoginGUI = new LoginGUI();
                    myLoginGUI.setVisible(true);
                    dispose();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame,
                            e.getMessage(),
                            "Register error",
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

    /**
     * Methode who will check if passwordFiled and passwordRepeadFiled are correct
     * @return true if it's the same and false if not
     */
    private boolean checkPasswords(){
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

        // Clean password arrays
        for (int i = 0; i < pass1.length; i++) {
            pass1[i] = '\0';
        }
        for (int i = 0; i < pass2.length; i++) {
            pass2[i] = '\0';
        }

        if(different) {
            return false;
        }

        return true;
    }
}
