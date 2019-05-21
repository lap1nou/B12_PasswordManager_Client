package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.IStorePasswordDriver;
import ch.heigvd.pro.client.file.ServerDriver;
import ch.heigvd.pro.client.structure.Group;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class UserProfile extends JFrame {
    private JPanel main;
    private JTextField usernameField;
    private JTextField emailField;
    private JScrollPane scrollPane;
    private JTable groupTable;
    private JButton addGroupButton;
    private JButton deleteGroupButton;

    private IStorePasswordDriver serverDriver;

    // TODO: Edit group name
    public UserProfile(IStorePasswordDriver serverDriver) {
        this.serverDriver = serverDriver;

        // Frame initialisation
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        setTitle("Profile");
        add(main);
        setLocationRelativeTo(null);
        setSize(300, 350);
        setResizable(false);
        //pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        GroupPopup groupPopup = new GroupPopup(groupTable, serverDriver);

        refreshGroupTable();

        /*
         * Add group
         */
        addGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addGroup();
            }
        });

        /*
         * Delete group
         */
        deleteGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                deleteSelectedGroup();
            }
        });

        /*
         * Popup when we are doing a right click to folders
         */
        groupTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && scrollPane.isValidateRoot()) {
                    groupPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        try {
            usernameField.setText(((ServerDriver) serverDriver).getUser().getUsername());
            emailField.setText(((ServerDriver) serverDriver).getUser().getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom class built for easily adding Group object directly into the JTable.
     * Source: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
     */
    private class CustomTableModelGroup extends AbstractTableModel {
        String titres[];
        java.util.List<Group> group;

        public CustomTableModelGroup(
                List<Group> group, String[] titres) {
            this.group = group;
            this.titres = titres;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int parm1, int parm2) {
            switch (parm2) {
                case 0:
                    return group.get(parm1).getName();
                case 1:
                    return group.get(parm1).getRight();
                default:
                    break;
            }
            return null;
        }

        public int getRowCount() {
            return group.size();
        }

        public String getColumnName(int col) {
            return titres[col];
        }

    }

    /**
     * Source: http://esus.com/displaying-a-popup-menu-when-right-clicking-on-a-jtree-node/
     */
    class GroupPopup extends JPopupMenu {
        public GroupPopup(JTable groupTable, IStorePasswordDriver serverDriver) {
            JMenuItem addGroup = new JMenuItem("Add group");
            JMenuItem editGroup = new JMenuItem("Edit group");
            JMenuItem deleteGroup = new JMenuItem("Delete group");

            addGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            addGroup();
                        }
                    });
                    setEnabled(false);
                }
            });

            editGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                }
            });

            deleteGroup.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            deleteSelectedGroup();
                        }
                    });
                }
            });

            add(addGroup);
            add(new JSeparator());
            add(editGroup);
            add(new JSeparator());
            add(deleteGroup);

        }
    }

    /**
     * Refresh the JTable for update.
     */
    private void refreshGroupTable() {
        CustomTableModelGroup myModel = new UserProfile.CustomTableModelGroup(((ServerDriver) serverDriver).getUser().getGroups(),
                new String[]{"Group name", "Right"});
        groupTable.setModel(myModel);
    }

    /**
     * Delete the selected group from the server and locally, a user must be admin of the group in order to successfully delete it.
     */
    private void deleteSelectedGroup() {
        try {
            ((ServerDriver) serverDriver).renewToken();
            ((ServerDriver) serverDriver).deleteGroup(groupTable.getSelectedRow());
            refreshGroupTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Error : Deleting Group",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Add a group for the current user.
     */
    private void addGroup() {
        String folderName = JOptionPane.showInputDialog("Enter the new Group name");
        if (!folderName.equals("")) {
            try {
                serverDriver.createGroupe(folderName.toCharArray());
                refreshGroupTable();
                JOptionPane.showMessageDialog(null,
                        "The group was created",
                        "New Group",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshGroupTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        e.getMessage(),
                        "Error : New Group",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
