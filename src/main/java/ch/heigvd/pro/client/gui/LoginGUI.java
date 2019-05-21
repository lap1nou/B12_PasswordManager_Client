package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.file.ServerDriver;
import ch.heigvd.pro.client.structure.Safe;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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

                        if (databaseTextField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                            JOptionPane.showMessageDialog(frame, "All fields must be filled");
                            return;
                        }
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
                    ex.printStackTrace();
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        westPanel = new JPanel();
        westPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(westPanel, BorderLayout.WEST);
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        imageLabel = new JLabel();
        imageLabel.setText("");
        northPanel.add(imageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        onlineRadioButton = new JRadioButton();
        onlineRadioButton.setSelected(true);
        onlineRadioButton.setText("Online");
        northPanel.add(onlineRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        offRadioButton = new JRadioButton();
        offRadioButton.setText("Offline");
        northPanel.add(offRadioButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(eastPanel, BorderLayout.EAST);
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayoutManager(2, 6, new Insets(10, 10, 10, 10), -1, -1));
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        loginButton = new JButton();
        loginButton.setText("Login");
        southPanel.add(loginButton, new GridConstraints(0, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        forgetPasswordButton = new JButton();
        forgetPasswordButton.setMargin(new Insets(0, 0, 0, 0));
        forgetPasswordButton.setText("Forget Password");
        southPanel.add(forgetPasswordButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelAccount = new JLabel();
        labelAccount.setText("Click here to create a new account");
        southPanel.add(labelAccount, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        southPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        southPanel.add(spacer2, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        usernameLabel = new JLabel();
        usernameLabel.setText("Username :");
        centerPanel.add(usernameLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        centerPanel.add(spacer3, new GridConstraints(3, 1, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        usernameTextField = new JFormattedTextField();
        centerPanel.add(usernameTextField, new GridConstraints(0, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordLabel = new JLabel();
        passwordLabel.setText("Password:");
        centerPanel.add(passwordLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setText("");
        centerPanel.add(passwordField, new GridConstraints(1, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        databaseLabel = new JLabel();
        databaseLabel.setText("Database :");
        centerPanel.add(databaseLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseButton = new JButton();
        browseButton.setText("Browse");
        centerPanel.add(browseButton, new GridConstraints(2, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        databaseTextField = new JTextField();
        databaseTextField.setEditable(false);
        centerPanel.add(databaseTextField, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer4 = new Spacer();
        centerPanel.add(spacer4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        centerPanel.add(spacer5, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
