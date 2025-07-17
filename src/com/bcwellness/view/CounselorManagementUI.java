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

    // Input Panel with 2 rows x 6 columns (label + input = 1 pair = 2 columns)
    JPanel inputPanel = new JPanel(new GridLayout(3, 4, 5, 5));
    txtName = new JTextField();
    txtSurname = new JTextField();
    txtEmail = new JTextField();
    txtPhone = new JTextField();
    txtSpecialization = new JTextField();
    String[] availabilityOptions = {"Mon-Fri", "Mon-Thurs", "Tues-Fri", "Tues-Thurs", "Wed-Fri"};
    cmbAvailability = new JComboBox<>(availabilityOptions);

    // Corrected label-input order
    inputPanel.add(new JLabel("Name:"));            inputPanel.add(txtName);
    inputPanel.add(new JLabel("Surname:"));         inputPanel.add(txtSurname);
    inputPanel.add(new JLabel("Phone:"));           inputPanel.add(txtPhone);
    inputPanel.add(new JLabel("Specialization:"));  inputPanel.add(txtSpecialization);
    inputPanel.add(new JLabel("Availability:"));    inputPanel.add(cmbAvailability); // FIXED position
    inputPanel.add(new JLabel("Email:"));           inputPanel.add(txtEmail);        // FIXED position

    // Button panel
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

    // Output area
    txtOutput = new JTextArea(5, 40);
    txtOutput.setEditable(false);
    JScrollPane outputScrollPane = new JScrollPane(txtOutput);

    // Top panel = input + buttons
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(inputPanel, BorderLayout.NORTH);
    topPanel.add(buttonPanel, BorderLayout.SOUTH);

    // Final layout
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
        txtSurname.setText((String) tableModel.getValueAt(row, 2));
        txtEmail.setText((String) tableModel.getValueAt(row, 3));
        txtPhone.setText((String) tableModel.getValueAt(row, 4));
        txtSpecialization.setText((String) tableModel.getValueAt(row, 5));
        cmbAvailability.setSelectedItem(tableModel.getValueAt(row, 6));
    }

    private void handleAdd(ActionEvent e) {
        try {
            Counselor c = new Counselor(
                    txtName.getText().trim(),
                    txtSurname.getText().trim(),
                    txtEmail.getText().trim(),
                    txtPhone.getText().trim(),
                    txtSpecialization.getText().trim(),
                    (String) cmbAvailability.getSelectedItem()
            );
            if (controller.addCounselor(c)) {
                txtOutput.append("Counselor added successfully.\n");
                clearFields();
                handleViewAll(null);
            } else {
                txtOutput.append("Failed to add counselor.\n");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error adding counselor", ex);
            txtOutput.append("Error adding counselor: " + ex.getMessage() + "\n");
        }
    }

    private void handleUpdate(ActionEvent e) {
        int row = counselorTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a counselor to update.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(row, 0);
            Counselor c = new Counselor(
                    id,
                    txtName.getText().trim(),
                    txtSurname.getText().trim(),
                    txtEmail.getText().trim(),
                    txtPhone.getText().trim(),
                    txtSpecialization.getText().trim(),
                    (String) cmbAvailability.getSelectedItem()
            );
            if (controller.updateCounselor(c)) {
                txtOutput.append("Counselor updated.\n");
                handleViewAll(null);
            } else {
                txtOutput.append("Update failed.\n");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error updating counselor", ex);
            txtOutput.append("Error updating counselor.\n");
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
        txtSurname.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtSpecialization.setText("");
        cmbAvailability.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Counselor Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new CounselorManagementUI());
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

