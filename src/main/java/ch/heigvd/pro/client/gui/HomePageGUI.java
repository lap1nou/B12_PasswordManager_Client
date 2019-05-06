package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Folder;
import ch.heigvd.pro.client.structure.Safe;

import javax.crypto.BadPaddingException;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class HomePageGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemExit;
    private JMenu menuTools;
    private JMenuItem menuItemPassGen;
    private JMenu menuHelp;
    private JMenuItem menuItemAbout;
    private JTree groupTree;
    private JPanel centerPanel;
    private JTable entryTable;
    private JMenuItem menuItemNewEntry;
    private JMenuItem menuItemNewGroup;

    private Safe safe;
    private int folderNumber;

    public HomePageGUI(Safe safe) {
        this.safe = safe;

        try {
            safe.decryptPassword();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        // Frame initialisations
        setTitle("Home Page");
        add(mainPanel);
        InitGroupTree();
        InitEntryTable();
        setLocationRelativeTo(null);
        setSize(650, 700);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Listeners
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        menuItemNewEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EntryGUI newEntry = new EntryGUI(safe, folderNumber);
                        newEntry.addWindowListener(new WindowAdapter() {
                            public void windowClosing(WindowEvent e) {
                                HomePageGUI.this.setEnabled(true);
                            }
                        });

                        newEntry.getCancelButton().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                HomePageGUI.this.setEnabled(true);
                                newEntry.dispose();
                            }
                        });
                    }
                });
                setEnabled(false);
            }
        });

        menuItemPassGen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        PasswordGeneratorGUI passwordGenerator = new PasswordGeneratorGUI();
                        passwordGenerator.addWindowListener(new WindowAdapter() {
                            public void windowClosing(WindowEvent e) {
                                HomePageGUI.this.setEnabled(true);
                            }
                        });

                        passwordGenerator.getCancelButton().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                HomePageGUI.this.setEnabled(true);
                                passwordGenerator.dispose();
                            }
                        });
                    }
                });
                setEnabled(false);
            }
        });

        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "ImPass 1.0\n" + "This program is open source and not certified\n");
            }
        });

        menuItemNewGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupName = JOptionPane.showInputDialog("Enter the new group name");
            }
        });

        groupTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                // TODO : Fix bug if we press on the root node
                String selectedFolder = treeSelectionEvent.getPath().getPathComponent(1).toString();

                int i = 0;

                for (Folder folder : safe.getFolderList()) {
                    if (folder.getName().equals(selectedFolder)) {
                        CustomTableModel myModel = new CustomTableModel(folder.getEntrylist(), new String[]{"Entry name", "User Name", "Target", "Date"});
                        entryTable.setModel(myModel);
                        folderNumber = i;
                    }
                    i++;
                }
            }
        });
    }

    public void InitGroupTree() {
        // Source : https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Passwords");

        for (Folder folder : safe.getFolderList()) {
            DefaultMutableTreeNode folderRoot = new DefaultMutableTreeNode(folder.getName());
            root.add(folderRoot);

            for (Entry entry : folder.getEntrylist()) {
                CharBuffer tmpBuffer = CharBuffer.wrap(entry.getTarget());
                folderRoot.add(new DefaultMutableTreeNode(tmpBuffer));
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        groupTree.setModel(treeModel);
    }


    void InitEntryTable() {
        //CustomTableModel myModel = new CustomTableModel(safe.getFolderList().get(0).getEntrylist(), new String[]{"User Name", "Target", "E-mail", "Date"});
        //entryTable.setModel(myModel);
    }

    private class CustomTableModel extends AbstractTableModel {
        String titres[];
        List<Entry> donnees;

        public CustomTableModel(
                List<Entry> donnees, String[] titres) {
            this.donnees = donnees;
            this.titres = titres;
        }

        public int getColumnCount() {
            return 4;
        }

        public Object getValueAt(int parm1, int parm2) {
            switch (parm2) {
                case 0:
                    return CharBuffer.wrap(donnees.get(parm1).getEntryName());
                case 1:
                    return CharBuffer.wrap(donnees.get(parm1).getUsername());
                case 2:
                    return CharBuffer.wrap(donnees.get(parm1).getTarget());
                case 3:
                    return donnees.get(parm1).getRegisterDate();
                default:
                    break;
            }
            return null;
        }

        public int getRowCount() {
            return donnees.size();
        }

        public String getColumnName(int col) {
            return titres[col];
        }

    }
}
