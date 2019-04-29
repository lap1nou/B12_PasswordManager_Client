package ch.heigvd.pro.core;

import javax.swing.*;

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
    }
}
