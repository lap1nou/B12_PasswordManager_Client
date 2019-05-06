package ch.heigvd.pro.client.gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

    public HomePageGUI() {

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
                        EntryGUI newEntry = new EntryGUI();
                        newEntry.addWindowListener(new WindowAdapter() {
                            public void windowClosing(WindowEvent e)
                            {
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
                            public void windowClosing(WindowEvent e)
                            {
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
    }

    public void InitGroupTree()
    {
        // https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("User");
        //create the child nodes
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Exemple1");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Exemple2");
        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        //create the tree by passing in the root node
        groupTree.setModel(treeModel);
    }


    void InitEntryTable() {
        Object[][] data = {
                {"Compte Facebook", "Jean-Kevin", "www.facebook.com", "Test"}
        };

        String[]  title = {"Entry Name", "User Name", "URL", "Notes"};
        CustomTableModel myModel = new CustomTableModel(data,title);
        entryTable.setModel(myModel);

    }

    private class CustomTableModel extends AbstractTableModel {
        Object donnees[][];
        String titres[];
        public CustomTableModel(
                Object donnees[][], String[] titres) {
            this.donnees = donnees;
            this.titres = titres;
        }
        public int getColumnCount(){
            return donnees[0].length;
        }
        public Object getValueAt(int parm1, int parm2){
            return donnees[parm1][parm2];
        }
        public int getRowCount() {
            return donnees.length;
        }
        public String getColumnName(int col){
            return titres[col];
        }
    }
}
