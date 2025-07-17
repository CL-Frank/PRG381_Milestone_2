package com.bcwellness.view;

import com.bcwellness.controller.CounsellorController;
import com.bcwellness.model.Counselor;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CounselorManagementUI extends JPanel {

    private static final Logger logger = Logger.getLogger(CounselorManagementUI.class.getName());

    private CounsellorController controller;

    private JTextField txtName, txtEmail, txtPhone, txtSpecialization;
    // UI Components
    private JTextField txtName, txtEmail, txtPhone, txtSpecialization, txtSurname;
    private JComboBox<String> cmbAvailability;
    private JButton btnAdd, btnUpdate, btnDelete, btnViewAll;
    private JTextArea txtOutput;
    private JTable counselorTable;
    private DefaultTableModel tableModel;

    public CounselorManagementUI() {
        controller = new CounsellorController();
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        // Input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        txtName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtSpecialization = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new JLabel("Surname:"));
        txtSurname = new JTextField();
        inputPanel.add(txtSurname);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(txtEmail);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(txtPhone);
        inputPanel.add(new JLabel("Specialization:"));
        inputPanel.add(txtSpecialization);
        inputPanel.add(new JLabel("Availability:"));
        String[] availabilityOptions = {"Mon-Fri", "Mon-Thurs", "Tues-Fri", "Tues-Thurs", "Wed-Fri"};
        cmbAvailability = new JComboBox<>(availabilityOptions);
        inputPanel.add(cmbAvailability);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnViewAll = new JButton("View All");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewAll);

        // Table
        String[] columnNames = {"ID", "Name", "Surname", "Email", "Phone", "Specialization", "Availability"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        counselorTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(counselorTable);

        // Output log
        txtOutput = new JTextArea(5, 40);
        txtOutput.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(txtOutput);

        // Combine everything
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(outputScrollPane, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        btnAdd.addActionListener(this::handleAdd);
        btnUpdate.addActionListener(this::handleUpdate);
        btnDelete.addActionListener(this::handleDelete);
        btnViewAll.addActionListener(this::handleViewAll);

        counselorTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = counselorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFieldsFromTable(selectedRow);
                }
            }
        });
    }

    private void populateFieldsFromTable(int row) {
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtEmail.setText((String) tableModel.getValueAt(row, 3));
        txtPhone.setText((String) tableModel.getValueAt(row, 4));
        txtSpecialization.setText((String) tableModel.getValueAt(row, 5));
    }

    private void handleAdd(ActionEvent e) {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String specialization = txtSpecialization.getText().trim();

        Counselor c = new Counselor(name, "surname", email, phone, specialization, "Available");
        if (controller.addCounselor(c)) {
            txtOutput.append("Counselor added.\n");
            clearFields();
            handleViewAll(null);
        } else {
            txtOutput.append("Failed to add counselor.\n");
        }
    }

    private void handleUpdate(ActionEvent e) {
        int row = counselorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a counselor to update.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String specialization = txtSpecialization.getText().trim();

        Counselor c = new Counselor(id, name, "surname", email, phone, specialization, "Available");

        if (controller.updateCounselor(c)) {
            txtOutput.append("Counselor updated.\n");
            handleViewAll(null);
        } else {
            txtOutput.append("Update failed.\n");
        }
    }

    private void handleDelete(ActionEvent e) {
        int row = counselorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a counselor to delete.");
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "Delete " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteCounselor(id)) {
                txtOutput.append("Deleted successfully.\n");
                handleViewAll(null);
            } else {
                txtOutput.append("Delete failed.\n");
            }
        }
    }

    private void handleViewAll(ActionEvent e) {
        try {
            tableModel.setRowCount(0); // Clear table
            List<Counselor> list = controller.getAllCounselors();
            for (Counselor c : list) {
                tableModel.addRow(new Object[]{
                        c.getId(), c.getName(), c.getSurname(),
                        c.getEmail(), c.getPhone(),
                        c.getSpecialization(), c.getAvailability()
                });
            }
        } catch (Exception ex) {
            txtOutput.append("Error loading data.\n");
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private void clearFields() {
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtSpecialization.setText("");
    }

    private void addCounselor() {
        try {
        Counselor c = new Counselor(0,
        txtName.getText(),
        txtSurname.getText(),
        txtEmail.getText(),
        txtPhone.getText(),
        txtSpecialization.getText(),
        (String) cmbAvailability.getSelectedItem());
            if (counselorDAO.addCounselor(c)) {
                txtOutput.append("Counselor added successfully.\n");
                clearFields();
                loadAllCounselors();
            } else {
                txtOutput.append("Failed to add counselor.\n");
            }
        } catch (Exception ex) {
            logger.severe(() -> "Error adding counselor: " + ex.getMessage());
            txtOutput.append("Error adding counselor: " + ex.getMessage() + "\n");
        }
    }

 private void updateCounselor() {
        int selectedRow = counselorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a counselor to update.");
            return;
        }

        Counselor c = tableModel.getCounselorAt(selectedRow);
        c.setName(txtName.getText());
        c.setEmail(txtEmail.getText());
        c.setPhone(txtPhone.getText());
        c.setSpecialization(txtSpecialization.getText());

        if (counselorDAO.updateCounselor(c)) {
            txtOutput.append("Counselor updated successfully.\n");
            loadAllCounselors();
        } else {
            txtOutput.append("Failed to update counselor.\n");
        }
    }
     private void deleteCounselor() {
        int selectedRow = counselorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a counselor to delete.");
            return;
        }

        Counselor c = tableModel.getCounselorAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete " + c.getName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (counselorDAO.deleteCounselor(c.getId())) {
                    txtOutput.append("Counselor deleted successfully.\n");
                    loadAllCounselors();
                } else {
                    txtOutput.append("Failed to delete counselor.\n");
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

   private void loadAllCounselors() {
        try {
            List<Counselor> counselors = counselorDAO.getAllCounselors();
            tableModel.setCounselors(counselors);
        } catch (Exception ex) {
            logger.severe("Error loading counselors: " + ex.getMessage());
            txtOutput.append("Error loading counselors.\n");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // </editor-fold>
@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */  // </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
 try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new CounselorManagementUI().setVisible(true));
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
