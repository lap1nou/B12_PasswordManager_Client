package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.IStorePasswordDriver;
import ch.heigvd.pro.client.file.ServerDriver;
import ch.heigvd.pro.client.structure.Entry;
import ch.heigvd.pro.client.structure.Folder;
import ch.heigvd.pro.client.structure.Safe;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.crypto.BadPaddingException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private JPanel westPanel;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuItemExit;
    private JMenu menuTools;
    private JMenuItem menuItemPassGen;
    private JMenu menuHelp;
    private JMenuItem menuItemAbout;
    public JTree userTree;
    private JPanel centerPanel;
    private JTable entryTable;
    private JMenu profile;
    private JMenuItem menuItemNewGroup;
    private JMenuItem menuItemProfile;
    private JMenu Groups;
    private JMenuItem menuItemShowGroups;
    private JScrollPane treeScroll;
    private JTree groupsTree;
    private JPopupMenu rightClick;
    private JFrame frame;

    private Safe safe;
    private IStorePasswordDriver parameterOnlineOffline;

    private int selectedFolderNumber;
    private int selectedEntryNumber;
    private int selectedSafeNumber;

    public HomePageGUI(IStorePasswordDriver parameterOnlineOffline) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        this.safe = parameterOnlineOffline.getSafe(0);
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
        EntryPopup entryPopup = new EntryPopup(entryTable);

        if (parameterOnlineOffline instanceof ServerDriver) {
            profile.setVisible(true);
        } else {
            profile.setVisible(false);
        }

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
         * Popup when we are doing a right click in entries
         */
        entryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && !userTree.isSelectionEmpty()) {
                    entryPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        /*
         * Exit the program
         */
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        /*
         * Menu that generate passsword
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
         * Menu group
         */
        menuItemShowGroups.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if (parameterOnlineOffline instanceof ServerDriver) {
                            ManageGroup manageGroup = new ManageGroup((ServerDriver) parameterOnlineOffline);
                            manageGroup.addWindowListener(new WindowAdapter() {
                                public void windowClosing(WindowEvent e) {
                                    HomePageGUI.this.setEnabled(true);
                                    manageGroup.dispose();
                                }
                            });

                            manageGroup.getCancelButton().addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    HomePageGUI.this.setEnabled(true);
                                    manageGroup.dispose();
                                }
                            });
                        } else {
                            HomePageGUI.this.setEnabled(true);
                            JOptionPane.showMessageDialog(null, "This option is only available online");
                        }
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
         * User profile
         */
        menuItemProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        UserProfile profile = new UserProfile(parameterOnlineOffline);
                        profile.addWindowListener(new WindowAdapter() {
                            public void windowClosing(WindowEvent e) {
                                HomePageGUI.this.setEnabled(true);
                            }
                        });

                    }
                });
                setEnabled(false);
            }
        });

        userTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                refreshTable();
            }
        });

        entryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                // Source : https://stackoverflow.com/questions/32574450/double-click-event-on-jlist-element
                if (mouseEvent.getClickCount() >= 2) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            EntryGUI entryView = new EntryGUI(parameterOnlineOffline.getSafe(selectedSafeNumber), selectedFolderNumber, entryTable.getSelectedRow(), HomePageGUI.this, (IStorePasswordDriver) parameterOnlineOffline);
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

        entryTable.getSelectionModel().addListSelectionListener(new ListAction());
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
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(northPanel, BorderLayout.NORTH);
        menuBar = new JMenuBar();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        northPanel.add(menuBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        menuFile = new JMenu();
        menuFile.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        menuFile.setText("File");
        menuFile.setToolTipText("");
        menuBar.add(menuFile);
        menuFile.setBorder(BorderFactory.createTitledBorder(""));
        menuItemExit = new JMenuItem();
        menuItemExit.setText("Exit");
        menuFile.add(menuItemExit);
        menuTools = new JMenu();
        menuTools.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        menuTools.setText("Tools");
        menuBar.add(menuTools);
        menuTools.setBorder(BorderFactory.createTitledBorder(""));
        menuItemPassGen = new JMenuItem();
        menuItemPassGen.setText("Password Generator");
        menuTools.add(menuItemPassGen);
        profile = new JMenu();
        profile.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        profile.setText("Profle");
        menuBar.add(profile);
        menuItemProfile = new JMenuItem();
        menuItemProfile.setText("My profile");
        profile.add(menuItemProfile);
        Groups = new JMenu();
        Groups.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        Groups.setText("Groups");
        Groups.setToolTipText("");
        menuBar.add(Groups);
        Groups.setBorder(BorderFactory.createTitledBorder(""));
        menuItemShowGroups = new JMenuItem();
        menuItemShowGroups.setText("Show groups");
        Groups.add(menuItemShowGroups);
        menuItemNewGroup = new JMenuItem();
        menuItemNewGroup.setEnabled(false);
        menuItemNewGroup.setText("New Group");
        menuItemNewGroup.setVisible(false);
        Groups.add(menuItemNewGroup);
        menuHelp = new JMenu();
        menuHelp.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        menuHelp.setText("Help");
        menuBar.add(menuHelp);
        menuItemAbout = new JMenuItem();
        menuItemAbout.setText("About Us");
        menuHelp.add(menuItemAbout);
        westPanel = new JPanel();
        westPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(westPanel, BorderLayout.WEST);
        final Spacer spacer1 = new Spacer();
        westPanel.add(spacer1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(76, 11), null, 0, false));
        treeScroll = new JScrollPane();
        westPanel.add(treeScroll, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 221), null, 0, false));
        userTree = new JTree();
        userTree.setAutoscrolls(false);
        userTree.setEditable(false);
        userTree.setRootVisible(false);
        treeScroll.setViewportView(userTree);
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        centerPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        entryTable = new JTable();
        entryTable.setPreferredScrollableViewportSize(new Dimension(450, 400));
        entryTable.setRowHeight(30);
        entryTable.setToolTipText("");
        entryTable.setUpdateSelectionOnSort(true);
        scrollPane1.setViewportView(entryTable);
        final Spacer spacer2 = new Spacer();
        centerPanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public class ListAction implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            selectedEntryNumber = entryTable.getSelectedRow();
        }
    }

    /**
     * Initialise the JTree with Folder and Entry in the Safe.
     */
    public void InitGroupTree() {
        // Source : https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Passwords");

        for (int i = 0; i < parameterOnlineOffline.getSafeSize(); i++) {
            DefaultMutableTreeNode safeRoot = new DefaultMutableTreeNode(parameterOnlineOffline.getSafe(i).getSafeName());

            for (Folder folder : parameterOnlineOffline.getSafe(i).getFolderList()) {
                DefaultMutableTreeNode folderRoot = new DefaultMutableTreeNode(folder.getName());
                safeRoot.add(folderRoot);
            }
            root.add(safeRoot);
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);

        userTree.setModel(treeModel);
    }

    /**
     * This class is used to define function for the contextual menu of the JTree
     * Source: http://esus.com/displaying-a-popup-menu-when-right-clicking-on-a-jtree-node/
     */
    private class FolderPopup extends JPopupMenu {
        public FolderPopup(JTree tree) {
            JMenuItem addPassword = new JMenuItem("Add password");
            JMenuItem deleteFolder = new JMenuItem("Delete folder");
            JMenuItem addFolder = new JMenuItem("Add folder");
            JMenuItem editFolder = new JMenuItem("Edit folder name");
            JMenuItem addSafeGroup = new JMenuItem("Add a safe group");

            addPassword.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            addPassword();
                        }
                    });
                    setEnabled(false);
                }
            });

            addFolder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                FolderCreation folderCreation = new FolderCreation(HomePageGUI.this, parameterOnlineOffline);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            editFolder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                String folderName = JOptionPane.showInputDialog("Enter the new Folder name");

                                if (folderName != null && !folderName.equals("") && selectedFolderNumber >= 0) {
                                    parameterOnlineOffline.editFolder(folderName.toCharArray(), selectedFolderNumber, 0);

                                    // Editing into the JTree, source: https://stackoverflow.com/questions/6663358/renaming-the-jtree-node-manually-in-java
                                    DefaultTreeModel treeModel = (DefaultTreeModel) userTree.getModel();
                                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
                                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(selectedSafeNumber);
                                    DefaultMutableTreeNode folderNode = (DefaultMutableTreeNode) child.getChildAt(selectedFolderNumber);

                                    folderNode.setUserObject(folderName);
                                    treeModel.nodeChanged(folderNode);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            parameterOnlineOffline.saveSafe();
                            refreshTable();
                        }
                    });
                }
            });

            deleteFolder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {

                            DefaultTreeModel treeModel = (DefaultTreeModel) userTree.getModel();
                            int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove that folder ?", "Remove folder", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

                            if (answer == YES_OPTION && selectedFolderNumber >= 0) {
                                try {
                                    parameterOnlineOffline.deleteFolder(selectedFolderNumber, selectedSafeNumber);

                                    // Removing from the JTree
                                    treeModel.removeNodeFromParent((DefaultMutableTreeNode) userTree.getSelectionPath().getLastPathComponent());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                parameterOnlineOffline.saveSafe();

                                refreshTable();
                            }

                        }
                    });
                }
            });

            addSafeGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                AddSafe addSafe = new AddSafe(HomePageGUI.this, (ServerDriver) parameterOnlineOffline);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

            add(addPassword);
            add(new JSeparator());
            add(addFolder);
            add(editFolder);
            add(deleteFolder);

            if (HomePageGUI.this.parameterOnlineOffline instanceof ServerDriver) {
                add(addSafeGroup);
            }
        }
    }

    /**
     * This class is used to define function for the contextual menu of the JTable
     * Source: http://esus.com/displaying-a-popup-menu-when-right-clicking-on-a-jtree-node/
     */
    private class EntryPopup extends JPopupMenu {
        public EntryPopup(JTable table) {
            JMenuItem addPassword = new JMenuItem("Add password");
            JMenuItem deleteEntry = new JMenuItem("Delete entry");

            addPassword.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            addPassword();
                        }
                    });
                    setEnabled(false);
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
                    ImageIcon myPicture = new ImageIcon(donnees.get(parm1).getIcon());
                    return new ImageIcon(myPicture.getImage().getScaledInstance(24, 24, Image.SCALE_FAST));
                case 1:
                    return CharBuffer.wrap(donnees.get(parm1).getEntryName());
                case 2:
                    return CharBuffer.wrap(donnees.get(parm1).getUsername());
                case 3:
                    return CharBuffer.wrap(donnees.get(parm1).getTarget());
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

        @Override
        public Class<?> getColumnClass(int i) {
            switch (i) {
                case 0:
                    return ImageIcon.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                case 3:
                    return String.class;
                default:
                    break;
            }
            return null;
        }
    }

    // Source: https://coderanch.com/t/513754/java/JTable-selection-listener-doesn-listen
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
        DefaultMutableTreeNode selectedSafe = new DefaultMutableTreeNode();

        try {
            root = (DefaultMutableTreeNode) userTree.getSelectionPath().getPathComponent(0);
            selectedSafe = (DefaultMutableTreeNode) userTree.getSelectionPath().getPathComponent(1);
            selectedFolder = (DefaultMutableTreeNode) userTree.getSelectionPath().getPathComponent(2);
        } catch (Exception e) {

        }

        selectedSafeNumber = root.getIndex(selectedSafe);

        if (selectedFolder != null) {
            selectedFolderNumber = selectedSafe.getIndex(selectedFolder);

            if (selectedFolderNumber != -1) {
                CustomTableModel myModel = new CustomTableModel(parameterOnlineOffline.getSafe(selectedSafeNumber).getFolderList().get(selectedFolderNumber).getEntrylist(), new String[]{"", "Entry name", "User Name", "Target"});
                entryTable.setModel(myModel);
            }
        }

    }

    /**
     * Delete the selected entry.
     */
    public void deleteSelectedEntry() {
        // Source : https://www.youtube.com/watch?v=c0gpJj-IAmE this video helped me a bit
        DefaultTreeModel treeModel = (DefaultTreeModel) userTree.getModel();

        int tmpEntryNumber = selectedEntryNumber;
        int tmpFolderNumber = selectedFolderNumber;

        int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove that entry ?", "Remove entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (answer == YES_OPTION) {
            try {
                parameterOnlineOffline.deleteEntry(tmpFolderNumber, tmpEntryNumber, 0);

                refreshTable();

                // Removing from the JTree
                treeModel.removeNodeFromParent((DefaultMutableTreeNode) userTree.getSelectionPath().getLastPathComponent());
            } catch (Exception e) {
                e.printStackTrace();
            }

            parameterOnlineOffline.saveSafe();
            InitGroupTree();

            // Restoring selected folder postion
            userTree.setSelectionRow(tmpFolderNumber + 1);
        }
    }

    public void addPassword() {
        if (selectedFolderNumber >= 0) {
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
    }

    /**
     * Create a folder on the JTree and in the Safe
     *
     * @param folderName the folder name
     * @param folderType the folder type: group folder or personal folder
     * @param groupId    the groupid
     */
    public void createFolder(String folderName, boolean folderType, int groupId) {
        if (!folderName.equals("")) {
            try {
                if (!folderType) {
                    parameterOnlineOffline.createFolder(folderName, selectedSafeNumber);
                } else {
                    ((ServerDriver) parameterOnlineOffline).createGroupFolder(folderName, groupId, selectedSafeNumber);
                }

                addFolderInTree(folderName);

                parameterOnlineOffline.saveSafe();
                refreshTable();
            } catch (Exception e) {
                //parameterOnlineOffline.saveSafe();
                //refreshTable();
                e.printStackTrace();
            }

        }
    }

    /**
     * Add a Safe node into the JTree
     * Source: https://stackoverflow.com/questions/7928839/adding-and-removing-nodes-from-a-jtree + https://stackoverflow.com/questions/30245837/jtree-wont-update
     *
     * @param safeName the name of the node
     */
    public void addSafeInTree(String safeName) {

        DefaultTreeModel treeModel = (DefaultTreeModel) userTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        treeModel.insertNodeInto(new DefaultMutableTreeNode(safeName), root, root.getChildCount());
    }

    /**
     * Add a Folder node into the JTree
     * Source: https://stackoverflow.com/questions/7928839/adding-and-removing-nodes-from-a-jtree + https://stackoverflow.com/questions/30245837/jtree-wont-update
     *
     * @param folderName the name of the node
     */
    public void addFolderInTree(String folderName) {
        // Adding into the JTree, source: https://stackoverflow.com/questions/7928839/adding-and-removing-nodes-from-a-jtree
        // + https://stackoverflow.com/questions/30245837/jtree-wont-update
        DefaultTreeModel treeModel = (DefaultTreeModel) userTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        DefaultMutableTreeNode safeNode = (DefaultMutableTreeNode) root.getChildAt(selectedSafeNumber);

        treeModel.insertNodeInto(new DefaultMutableTreeNode(folderName), safeNode, safeNode.getChildCount());
    }

    public int getSelectedSafeNumber() {
        return selectedSafeNumber;
    }
}
