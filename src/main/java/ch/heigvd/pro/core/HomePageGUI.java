package ch.heigvd.pro.core;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class HomePageGUI extends JFrame {
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JTree groupTree;
    private JTable table1;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemExit;
    private JMenu menuTools;
    private JMenuItem menuItemPassGen;
    private JMenu menuHelp;
    private JMenuItem menuItemAbout;
    private JTree tree1;

    public HomePageGUI() {
        setTitle("Home Page");
        add(mainPanel);
        //TreeExample();
        setLocationRelativeTo(null);
        setSize(650, 700);
        //pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void TreeExample()
    {
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        //create the child nodes
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
        //add the child nodes to the root node
        root.add(vegetableNode);
        root.add(fruitNode);

        //create the tree by passing in the root node
        groupTree = new JTree(root);
        add(groupTree);
    }
}
