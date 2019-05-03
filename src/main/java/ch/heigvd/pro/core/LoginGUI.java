package ch.heigvd.pro.core;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LoginGUI extends JFrame {
    public JPanel mainPanel;
    private JPanel westPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JButton loginButton;
    private JButton cancelButton;
    private JButton forgetPasswordButton;
    private JFormattedTextField usernameTextField;
    private JPasswordField passwordField;
    private JLabel labelAccount;
    private JLabel keyFileLabel;
    private JTextField keyFileTextField;
    private JLabel textSelection;
    private JRadioButton usernamePasswordRadioButton;
    private JRadioButton keyFileRadioButton;
    private JLabel usernameLabel;
    private JLabel masterFileldPassword;
    private JButton browseButton;

    public LoginGUI() {

        //Frame initialisation
        setTitle("Login");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(400, 200);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        ButtonGroup group = new ButtonGroup();
        group.add(keyFileRadioButton);
        group.add(usernamePasswordRadioButton);

        // Listeners
        labelAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                RegisterGUI myRegisterGUI = new RegisterGUI();
                dispose();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomePageGUI myHomePageGUI = new HomePageGUI();
                dispose();
            }
        });


        usernamePasswordRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyFileTextField.setEnabled(false);
            }
        });

        keyFileRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameTextField.setEnabled(false);
                passwordField.setEnabled(false);
                forgetPasswordButton.setEnabled(false);
                labelAccount.setEnabled(false);
                

            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(new File("c:\\"));
                chooser.showOpenDialog(null);
                // chooser.setFileFilter(new FileTypeFilter(".txt", ".docx", ".pdf"));
                File f = chooser.getSelectedFile();
                String filename = f.getAbsolutePath();
                keyFileTextField.setText(filename);

            }
        });
    }
}
