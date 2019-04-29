package ch.heigvd.pro.core;

import javax.swing.*;

public class newGroupGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JFormattedTextField formattedTextField1;
    private JButton cancelButton;
    private JButton createButton1;
    private JPanel northPanel;
    private  JMenuBar menuBar;
    private JMenu menuFile;
    private JMenu menuTools;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemExit;
    private JMenu menuHelp;
    private JMenuItem menuItemPassGen;
    private JMenuItem menuItemAbout;

    public newGroupGUI(String title) {
        super(title);
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(500, 300);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
