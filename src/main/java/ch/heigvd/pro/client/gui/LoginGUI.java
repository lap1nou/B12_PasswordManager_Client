package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.structure.Safe;

import javax.swing.*;
import java.awt.*;
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
    private JLabel databaseLabel;
    private JTextField databaseTextField;
    private JLabel textSelection;
    private JRadioButton onlineRadioButton;
    private JRadioButton offRadioButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton browseButton;
    private JPanel centerPanel;
    private Color oldForegroundLabelAccount;

    private Safe safe = new Safe();

    public LoginGUI() {

        // Frame initialisation
        setTitle("Login");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(400, 250);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initialisation mode online/offline
        ButtonGroup group = new ButtonGroup();
        group.add(offRadioButton);
        group.add(onlineRadioButton);
        databaseLabel.setVisible(false);
        databaseTextField.setVisible(false);
        browseButton.setVisible(false);

        // Listeners
        labelAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (onlineRadioButton.isSelected()) {
                    RegisterGUI myRegisterGUI = new RegisterGUI();

                } else if (offRadioButton.isSelected()) {
                    HomePageGUI newHomePageUI = new HomePageGUI(safe);
                }
                dispose();
            }
        });

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FileDriver test = new FileDriver();
                File passwordDB = new File(databaseTextField.getText());

                safe = test.loadSafe(passwordDB);
                safe.setSafePassword(passwordField.getPassword());

                if (safe.isPasswordCorrect()) {
                    HomePageGUI myHomePageGUI = new HomePageGUI(safe);
                    dispose();
                } else {
                    System.out.println("Error");
                }
            }
        });

        onlineRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                usernameLabel.setVisible(true);
                usernameTextField.setVisible(true);
                usernameTextField.setText("");
                passwordLabel.setText("Password:");
                passwordField.setText("");
                databaseLabel.setVisible(false);
                databaseTextField.setVisible(false);
                browseButton.setVisible(false);
                loginButton.setText("Login");
                labelAccount.setText("Click here to create a new account");
            }
        });

        offRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameLabel.setVisible(false);
                usernameTextField.setVisible(false);
                passwordLabel.setText("Master Password:");
                passwordField.setText("");
                databaseLabel.setVisible(true);
                databaseTextField.setVisible(true);
                databaseTextField.setText("");
                browseButton.setVisible(true);
                loginButton.setText("OK");
                labelAccount.setText("Click here to create a new database");
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int ret = chooser.showOpenDialog(null);
                //chooser.setFileFilter(new FileTypeFilter(".txt", ".docx", ".pdf"));
                if (ret == JFileChooser.APPROVE_OPTION) {
                    String filename = chooser.getSelectedFile().getAbsolutePath();
                    databaseTextField.setText(filename);
                }
            }
        });

        labelAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                oldForegroundLabelAccount = labelAccount.getForeground();
                labelAccount.setForeground(Color.BLUE);
            }
        });

        labelAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                labelAccount.setForeground(oldForegroundLabelAccount);

            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
