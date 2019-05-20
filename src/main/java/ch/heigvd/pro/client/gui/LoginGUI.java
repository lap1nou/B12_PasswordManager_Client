package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.file.ServerDriver;
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
    private JRadioButton onlineRadioButton;
    private JRadioButton offRadioButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton browseButton;
    private JPanel centerPanel;
    private JLabel imageLabel;
    private Color oldForegroundLabelAccount;
    private JFrame frame;

    private Safe safe = new Safe();

    public LoginGUI() {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));

        /*
         * Initialize Frame
         */
        setTitle("Login");
        Icon icon = new ImageIcon(getClass().getClassLoader().getResource("Logo.png"));
        imageLabel.setIcon(icon);
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(550, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        SwingUtilities.getRootPane(loginButton).setDefaultButton(loginButton);

        // Initialisation mode online/offline
        ButtonGroup group = new ButtonGroup();
        group.add(offRadioButton);
        group.add(onlineRadioButton);
        databaseLabel.setVisible(false);
        databaseTextField.setVisible(false);
        browseButton.setVisible(false);

        /*
         * On click on create account / create file
         */
        labelAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (onlineRadioButton.isSelected()) {
                    RegisterGUI myRegisterGUI = new RegisterGUI();

                } else if (offRadioButton.isSelected()) {
                    CreateMasterKeyGUI newMasterGUI = new CreateMasterKeyGUI();
                }

                dispose();
            }
        });

        /*
         * Action on login button
         */
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (onlineRadioButton.isSelected()) {

                        ServerDriver serverConnection = new ServerDriver();
                        safe = serverConnection.login(usernameTextField.getText().toCharArray(), passwordField.getText().toCharArray());
                        safe.setSafePassword(passwordField.getPassword());

                        serverConnection.setSafe(safe);

                        HomePageGUI myHomePageGUI = new HomePageGUI(serverConnection);

                    } else if (offRadioButton.isSelected()) {

                        File passwordDB = new File(databaseTextField.getText());
                        FileDriver test = new FileDriver(safe, passwordDB);

                        safe = test.loadSafe();
                        safe.setSafePassword(passwordField.getPassword());

                        test.setSafe(safe);

                        // TODO: Compare password hash
                        if (safe.isPasswordCorrect()) {
                            HomePageGUI myHomePageGUI = new HomePageGUI(test);
                        } else {
                            throw new Exception("The password is not correct");
                        }

                    }

                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getMessage(),
                            "Login error",
                            JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        /*
         * Change value on click on radio button (online)
         */
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
                forgetPasswordButton.setVisible(true);
                labelAccount.setText("Click here to create a new account");
            }
        });

        /*
         * Change value on click on radio button (offline)
         */
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
                forgetPasswordButton.setVisible(false);
                labelAccount.setText("Click here to create a new database");
            }
        });

        /*
         * Get the password file
         */
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

        /*
         *  TO ADD COMMENT
         */
        labelAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                oldForegroundLabelAccount = labelAccount.getForeground();
                labelAccount.setForeground(Color.BLUE);
            }
        });

        /*
         *  Set color on labelAccount when the mouse is on it
         */
        labelAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                labelAccount.setForeground(oldForegroundLabelAccount);

            }
        });

        /*
         * Button forget password
         */
        forgetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(frame,
                        "Please contact us to get a new password \n email: info@impass.ch \n phone: 078 123 45 67",
                        "Forget password",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
}
