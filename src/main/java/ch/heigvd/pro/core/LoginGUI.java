package ch.heigvd.pro.core;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGUI extends JFrame {
    public JPanel mainPanel;
    private JPanel westPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JButton loginButton;
    private JButton cancelButton;
    private JButton forgetPasswordButton;
    private JFormattedTextField formattedTextField1;
    private JPasswordField passwordField1;
    private JLabel label;

    public LoginGUI() {
        setTitle("Login");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(400, 200);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                RegisterGUI myRegisterGUI = new RegisterGUI();
                myRegisterGUI.setVisible(true);
                dispose();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomePageGUI myHomePageGUI = new HomePageGUI();
                myHomePageGUI.setVisible(true);
                dispose();
            }
        });
    }
}
