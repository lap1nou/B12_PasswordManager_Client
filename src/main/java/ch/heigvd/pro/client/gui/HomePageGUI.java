package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.FileDriver;
import ch.heigvd.pro.client.file.ServerDriver;
import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Folder;
import ch.heigvd.pro.client.structure.Safe;

import javax.crypto.BadPaddingException;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.*;
import java.io.File;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.JOptionPane.YES_OPTION;

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
    private JButton removeButton;

    private Safe safe;
    private int folderNumber;
    private String selectedFolderName;
    private String filename;

    // TODO Restore JTree state using this as an inspiration: https://community.oracle.com/thread/1479458
    public HomePageGUI(Safe safe, String filename) {
        this.safe = safe;
        this.filename = filename;

        try {
            safe.decryptPassword();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        setTitle("Home Page");
        add(mainPanel);
        InitGroupTree();
        setLocationRelativeTo(null);
        setSize(650, 700);
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
                        EntryGUI newEntry = new EntryGUI(safe, folderNumber,-1, HomePageGUI.this);
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

                safe.getFolderList().add(new Folder(groupName, new ArrayList<Entry>()));
                InitGroupTree();
                refreshTable();
            }
        });

        menuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                safe.encryptPassword();

                FileDriver test = new FileDriver(safe, new File(filename));
                test.saveSafe();

                try {
                    safe.decryptPassword();

                    // TODO What about just adding a node instead of refreshing all the JTree ?
                    // Refresh JTree
                    InitGroupTree();

                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

            }
        });

        groupTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                refreshTable();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Source : https://www.youtube.com/watch?v=c0gpJj-IAmE this video helped me a bit
                // Removing from the JTree
                selectedFolderName = groupTree.getSelectionPath().getPathComponent(1).toString();

                DefaultTreeModel treeModel = (DefaultTreeModel) groupTree.getModel();
                String nodeToRemoveString = groupTree.getSelectionPath().getLastPathComponent().toString();

                // Removing from the Safe
                int indexOfEntryToRemove = 0;
                for (Entry entry : safe.getFolderList().get(folderNumber).getEntrylist()) {
                    if (Arrays.equals(entry.getTarget(), nodeToRemoveString.toCharArray())) {
                        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove that entry ?", "Remove entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (answer == YES_OPTION) {
                            treeModel.removeNodeFromParent((DefaultMutableTreeNode) groupTree.getSelectionPath().getLastPathComponent());
                            safe.getFolderList().get(folderNumber).removeEntry(indexOfEntryToRemove);
                            refreshTable();
                            InitGroupTree();
                        }
                        break;
                    }
                    indexOfEntryToRemove++;
                }

            }
        });

        entryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // Source : https://stackoverflow.com/questions/32574450/double-click-event-on-jlist-element
                if (mouseEvent.getClickCount() >= 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            EntryGUI entryView = new EntryGUI(safe, folderNumber, entryTable.getSelectedRow(), HomePageGUI.this);
                            entryView.addWindowListener(new WindowAdapter() {
                                public void windowClosing(WindowEvent e) {
                                    HomePageGUI.this.setEnabled(true);
                                }
                            });

                            entryView.getCancelButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    HomePageGUI.this.setEnabled(true);
                                    entryView.dispose();
                                }
                            });
                        }
                    });
                    setEnabled(false);
                }
            }
        });
    }

    // TODO Restore JTree state using this as an inspiration: https://community.oracle.com/thread/1479458
    public HomePageGUI(ServerDriver user) {

        setTitle("Home Page");
        add(mainPanel);
        InitGroupTree();
        setLocationRelativeTo(null);
        setSize(650, 700);
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
                        EntryGUI newEntry = new EntryGUI(safe, folderNumber, -1,HomePageGUI.this);
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

                safe.getFolderList().add(new Folder(groupName, new ArrayList<Entry>()));
                InitGroupTree();
                refreshTable();
            }
        });

        menuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                safe.encryptPassword();

                FileDriver test = new FileDriver(safe, new File(filename));
                test.saveSafe();

                try {
                    safe.decryptPassword();

                    // TODO What about just adding a node instead of refreshing all the JTree ?
                    // Refresh JTree
                    InitGroupTree();

                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

            }
        });

        groupTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                refreshTable();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Source : https://www.youtube.com/watch?v=c0gpJj-IAmE this video helped me a bit
                // Removing from the JTree
                selectedFolderName = groupTree.getSelectionPath().getPathComponent(1).toString();

                DefaultTreeModel treeModel = (DefaultTreeModel) groupTree.getModel();
                String nodeToRemoveString = groupTree.getSelectionPath().getLastPathComponent().toString();

                // Removing from the Safe
                int indexOfEntryToRemove = 0;
                for (Entry entry : safe.getFolderList().get(folderNumber).getEntrylist()) {
                    if (Arrays.equals(entry.getTarget(), nodeToRemoveString.toCharArray())) {
                        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove that entry ?", "Remove entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                        if (answer == YES_OPTION) {
                            treeModel.removeNodeFromParent((DefaultMutableTreeNode) groupTree.getSelectionPath().getLastPathComponent());
                            safe.getFolderList().get(folderNumber).removeEntry(indexOfEntryToRemove);
                            refreshTable();
                            InitGroupTree();
                        }
                        break;
                    }
                    indexOfEntryToRemove++;
                }

            }
        });

        entryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // Source : https://stackoverflow.com/questions/32574450/double-click-event-on-jlist-element
                if (mouseEvent.getClickCount() >= 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            EntryGUI entryView = new EntryGUI(safe, folderNumber, entryTable.getSelectedRow(), HomePageGUI.this);
                            entryView.addWindowListener(new WindowAdapter() {
                                public void windowClosing(WindowEvent e) {
                                    HomePageGUI.this.setEnabled(true);
                                }
                            });

                            entryView.getCancelButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    HomePageGUI.this.setEnabled(true);
                                    entryView.dispose();
                                }
                            });
                        }
                    });
                    setEnabled(false);
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

    private class customAction implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
            refreshTable();
        }
    }

    public void refreshTable() {
        // TODO : Fix bug if we press on the root node
        String selectedFolder = "";
        try {
            selectedFolder = groupTree.getSelectionPath().getPathComponent(1).toString();
        } catch (NullPointerException e) {
            selectedFolder = selectedFolderName;
        }

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
}
