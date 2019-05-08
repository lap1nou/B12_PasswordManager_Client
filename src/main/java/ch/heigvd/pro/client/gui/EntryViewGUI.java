package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.structure.Safe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.CharBuffer;

public class EntryViewGUI extends JFrame {
    private JTextField usernameField;
    private JTextField targetField;
    private JButton saveButton;
    private JLabel imageLabel;
    private JPanel mainPanel;
    private JTextArea notesArea;
    private JButton showButton;
    private JButton generateButton;
    private JPasswordField passwordField;
    private JButton browseButton;
    private JButton closeButton;

    private String iconFilename;

    public EntryViewGUI(Safe safe, int folderNumber, int entryNumber, String iconFilename) {
        this.iconFilename = iconFilename;

        setTitle("Entry edit/view");
        add(mainPanel);
        setLocationRelativeTo(null);
        setSize(650, 700);
        setVisible(true);

        CharBuffer charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getTarget());
        targetField.setText(charBuffer.toString());

        charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getUsername());
        usernameField.setText(charBuffer.toString());

        charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getClearPassword());
        passwordField.setText(charBuffer.toString());

        charBuffer = CharBuffer.wrap(safe.getFolderList().get(folderNumber).getEntrylist().get(entryNumber).getNotes());
        notesArea.setText(charBuffer.toString());

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser chooser = new JFileChooser();
                int ret = chooser.showOpenDialog(null);

                if (ret == JFileChooser.APPROVE_OPTION) {
                    String filename = chooser.getSelectedFile().getAbsolutePath();

                    ImageIcon myPicture = new ImageIcon(filename);
                    imageLabel.setIcon(myPicture);
                }
            }
        });

        showButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                passwordField.setEchoChar((char) 0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                passwordField.setEchoChar('*');
            }
        });
    }

    private void createUIComponents() {
        // Source: https://stackoverflow.com/questions/10051638/updating-an-image-contained-in-a-jlabel-problems
        ImageIcon myPicture = new ImageIcon(iconFilename);
        imageLabel = new JLabel();
        imageLabel.setIcon(myPicture);
    }

    public JButton getCloseButton() {
        return closeButton;
    }

}
