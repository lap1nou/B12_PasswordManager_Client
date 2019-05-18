package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.IStorePasswordDriver;
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

import java.awt.*;
import java.awt.event.*;
import java.nio.CharBuffer;
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
    private JTree userTree;
    private JPanel centerPanel;
    private JTable entryTable;
    private JMenuItem menuItemNewFolder;
    private JButton removeButton;
    private JMenu profile;
    private JMenuItem menuItemNewGroup;
    private JMenuItem menuItemProfile;
    private JMenu Groups;
    private JMenuItem menuItemShowGroups;
    private JTree groupsTree;
    private JPopupMenu rightClick;
    private JFrame frame;

    private Safe safe;
    private IStorePasswordDriver parameterOnlineOffline;

    private int selectedFolderNumber;
    private int selectedEntryNumber;

    // TODO Restore JTree state using this as an inspiration: https://community.oracle.com/thread/1479458
    public HomePageGUI(IStorePasswordDriver parameterOnlineOffline) {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        this.safe = parameterOnlineOffline.getSafe();
        this.parameterOnlineOffline = parameterOnlineOffline;

        try {
            safe.decryptPassword();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        /*
         * Initialize Frame
         */
        setTitle("Home Page");
        add(mainPanel);
        InitGroupTree();
        setLocationRelativeTo(null);
        setSize(650, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        FolderPopup folderPopup = new FolderPopup(userTree);

        /*
         * Popup when we are doing a right click to folders
         */
        userTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && !userTree.isSelectionEmpty()) {
                    folderPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        /*
         * Exit the menu
         */
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        /*
         * Menu that will generate the passsword
         */
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

        /*
         * Menu about
         */
        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "ImPass 1.0\n" + "This program is open source and not certified\n");
            }
        });

        /*
         * Menu for creating new folder
         */
        menuItemNewFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String folderName = JOptionPane.showInputDialog("Enter the new Folder name");
                    if (!folderName.equals("")) {
                        parameterOnlineOffline.createFolder(folderName);

                        InitGroupTree();
                        refreshTable();
                    }
                } catch (Exception ex) {
                    // TODO: Remove this
                    // Temporary while Julien is fixing folder requests
                    InitGroupTree();
                    refreshTable();

                    ex.printStackTrace();
                }
            }
        });

        menuItemNewGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String groupName = JOptionPane.showInputDialog("Enter the group name");
                    if (groupName != null) {
                        ((ServerDriver) parameterOnlineOffline).createGroupe(groupName.toCharArray());
                        JOptionPane.showMessageDialog(frame,
                                "The group was created",
                                "New Group",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame,
                            ex.getMessage(),
                            "Error : New Group",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        /*
        menuItemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                safe.encryptPassword();

                FileDriver test = new FileDriver(safe, new File((String) parameterOnlineOffline));
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
*/

        userTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                refreshTable();
            }
        });

        /*
         * Remove an entry
         */
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deleteSelectedEntry();
            }
        });

        entryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // Source : https://stackoverflow.com/questions/32574450/double-click-event-on-jlist-element
                if (mouseEvent.getClickCount() >= 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            EntryGUI entryView = new EntryGUI(safe, selectedFolderNumber, entryTable.getSelectedRow(), HomePageGUI.this, (IStorePasswordDriver) parameterOnlineOffline);
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

    /**
     * Initialise the JTree with Folder and Entry in the Safe.
     */
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

        userTree.setModel(treeModel);
    }

    /**
     * Source: http://esus.com/displaying-a-popup-menu-when-right-clicking-on-a-jtree-node/
     */
    private class FolderPopup extends JPopupMenu {
        public FolderPopup(JTree tree) {
            JMenuItem addPassword = new JMenuItem("Add password");
            JMenuItem deleteFolder = new JMenuItem("Delete folder");
            JMenuItem deleteEntry = new JMenuItem("Delete entry");

            addPassword.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            EntryGUI newEntry = new EntryGUI(safe, selectedFolderNumber, -1, HomePageGUI.this, parameterOnlineOffline);

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

            deleteFolder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                }
            });

            deleteEntry.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            HomePageGUI.this.deleteSelectedEntry();
                        }
                    });
                }
            });

            add(addPassword);
            add(new JSeparator());
            add(deleteFolder);
            add(deleteEntry);

        }
    }

    /**
     * Custom class built for easily adding Entry object directly into the JTable.
     * Source: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
     */
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

    /**
     * Refresh the state of the JTable, reload all the entries of the selected folder to display them.
     */
    public void refreshTable() {
        // Source: https://www.youtube.com/watch?v=e7tr2VG2rag
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode selectedFolder = new DefaultMutableTreeNode();
        DefaultMutableTreeNode selectedEntry = new DefaultMutableTreeNode();

        try {
            root = (DefaultMutableTreeNode) userTree.getSelectionPath().getPathComponent(0);
            selectedFolder = (DefaultMutableTreeNode) userTree.getSelectionPath().getPathComponent(1);
            selectedEntry = (DefaultMutableTreeNode) userTree.getSelectionPath().getPathComponent(2);
        } catch (Exception e) {

        }

        if (selectedFolder != null) {
            selectedFolderNumber = root.getIndex(selectedFolder);

            if (selectedFolderNumber != -1) {
                CustomTableModel myModel = new CustomTableModel(safe.getFolderList().get(selectedFolderNumber).getEntrylist(), new String[]{"Entry name", "User Name", "Target", "Date"});
                entryTable.setModel(myModel);
            }
        }

        if (selectedEntry != null) {
            selectedEntryNumber = selectedFolder.getIndex(selectedEntry);

        }
    }

    /**
     * Delete the selected entry.
     */
    public void deleteSelectedEntry() {
        // Source : https://www.youtube.com/watch?v=c0gpJj-IAmE this video helped me a bit
        DefaultTreeModel treeModel = (DefaultTreeModel) userTree.getModel();

        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove that entry ?", "Remove entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (answer == YES_OPTION) {
            try {
                parameterOnlineOffline.deleteEntry(selectedFolderNumber, selectedEntryNumber);

                // Removing from the JTree
                treeModel.removeNodeFromParent((DefaultMutableTreeNode) userTree.getSelectionPath().getLastPathComponent());
            } catch (Exception e) {
                e.printStackTrace();
            }

            parameterOnlineOffline.saveSafe();

            refreshTable();
            InitGroupTree();
        }
    }
}
