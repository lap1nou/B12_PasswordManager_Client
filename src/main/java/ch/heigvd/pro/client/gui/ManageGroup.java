package ch.heigvd.pro.client.gui;

import ch.heigvd.pro.client.file.IStorePasswordDriver;
import ch.heigvd.pro.client.file.ServerDriver;
import ch.heigvd.pro.client.structure.Group;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URLDecoder;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

public class ManageGroup extends JFrame {
    private JPanel panel1;
    private JButton addGroupButton;
    private JButton deleteGroupButton;
    private JButton addMemberButton;
    private JButton joinAGroupButton;
    private JButton promoteButton;
    private JTable userTable;
    private JTable groupTable;
    private JScrollPane scrollPane;
    private JButton cancelButton;

    private ServerDriver serverDriver;

    // TODO: Error with newly created group
    public ManageGroup(ServerDriver serverDriver) {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("javaIcone.png")));
        this.serverDriver = serverDriver;

        // Frame initialisation
        setTitle("Manage group");
        add(panel1);
        setLocationRelativeTo(null);
        setSize(600, 650);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        ManageGroup.GroupPopup groupPopup = new ManageGroup.GroupPopup(groupTable, serverDriver);

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

        addMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (groupTable.getSelectedRow() != -1) {
                        String groupToken = serverDriver.generateGroupToken(serverDriver.getUser().getGroups().get(groupTable.getSelectedRow()).getIdGroup());
                        JOptionPane.showMessageDialog(null, "The token has been copied into the clipboard !");

                        StringSelection stringSelection = new StringSelection(groupToken);
                        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clpbrd.setContents(stringSelection, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        promoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        joinAGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String groupToken = JOptionPane.showInputDialog("Enter the invite token");

                // Source: https://stackoverflow.com/questions/11544568/decoding-a-base64-string-in-java
                String tmp = new String(Base64.decodeBase64(URLDecoder.decode(groupToken)));

                int groupId = Integer.valueOf(tmp.split(",")[0]);

                refreshGroupTable();

                try {
                    serverDriver.joinGroup(groupId, groupToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        groupTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && scrollPane.isValidateRoot()) {
                    groupPopup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
            }
        });
    }

    /**
     * Delete the selected group from the server and locally, a user must be admin of the group in order to successfully delete it.
     */
    private void deleteSelectedGroup() {
        try {
            serverDriver.renewToken();
            serverDriver.deleteGroup(groupTable.getSelectedRow());
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
        if (folderName != null && !folderName.equals("")) {
            try {
                serverDriver.createGroup(folderName.toCharArray());
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

    /**
     * Custom class built for easily adding Group object directly into the JTable.
     * Source: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data
     */
    private class CustomTableModelGroup extends AbstractTableModel {
        String titres[];
        List<Group> group;

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
        ManageGroup.CustomTableModelGroup myModel = new ManageGroup.CustomTableModelGroup(serverDriver.getUser().getGroups(),
                new String[]{"Group name", "Right"});
        groupTable.setModel(myModel);
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
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 11, new Insets(10, 10, 10, 10), -1, -1));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(2, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        addGroupButton = new JButton();
        addGroupButton.setText("Add group");
        panel1.add(addGroupButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        promoteButton = new JButton();
        promoteButton.setText("Promote");
        panel1.add(promoteButton, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Group");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Members");
        panel1.add(label2, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        panel1.add(scrollPane, new GridConstraints(1, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        groupTable = new JTable();
        scrollPane.setViewportView(groupTable);
        cancelButton = new JButton();
        cancelButton.setText("Cancel");
        panel1.add(cancelButton, new GridConstraints(2, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addMemberButton = new JButton();
        addMemberButton.setText("Add member");
        panel1.add(addMemberButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteGroupButton = new JButton();
        deleteGroupButton.setText("Delete group");
        panel1.add(deleteGroupButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        joinAGroupButton = new JButton();
        joinAGroupButton.setText("Join a group");
        panel1.add(joinAGroupButton, new GridConstraints(2, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(1, 7, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        userTable = new JTable();
        scrollPane1.setViewportView(userTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

}
