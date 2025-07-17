package com.bcwellness.view;

import com.bcwellness.dao.CounselorDAO;
import com.bcwellness.model.Counselor;
import com.bcwellness.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CounselorManagementUI extends JFrame {

    private static final Logger logger = Logger.getLogger(CounselorManagementUI.class.getName());
    private CounselorDAO counselorDAO;

    // UI Components
    private JTextField txtName, txtEmail, txtPhone, txtSpecialization, txtSurname;
    private JComboBox<String> cmbAvailability;
    private JButton btnAdd, btnUpdate, btnDelete, btnViewAll;
    private JTextArea txtOutput;
    private JTable counselorTable;
    private CounselorTableModel tableModel;
    /**
     * Creates new form CounselorManagementUI
     */
   public CounselorManagementUI() {
        initComponents(); // Load auto-generated UI (from visual editor)
        setupDatabase();
        setupUI();
        setupListeners();
    }
         private void setupDatabase() {
        try {
            Connection connection; // Use centralized DB utility
            connection = DBConnection.getConnection();
            counselorDAO = new CounselorDAO(connection);
        } catch (Exception ex) {
            logger.severe("Failed to connect to database.");
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
        private void setupUI() {
        setTitle("Counselor Management");
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
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

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnViewAll = new JButton("View All");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnViewAll);

        // Table for displaying counselors
        tableModel = new CounselorTableModel();
        counselorTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(counselorTable);

        // Output Area
        txtOutput = new JTextArea(5, 40);
        txtOutput.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(txtOutput);

        // Combine Panels
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.WEST);
        mainPanel.add(outputScrollPane, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }
        private void setupListeners() {
        btnAdd.addActionListener((ActionEvent e) -> {
            addCounselor();
        });

        btnUpdate.addActionListener((ActionEvent e) -> {
            updateCounselor();
        });

        btnDelete.addActionListener((ActionEvent e) -> {
            deleteCounselor();
        });

        btnViewAll.addActionListener((ActionEvent e) -> {
            loadAllCounselors();
        }); 
        counselorTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = counselorTable.getSelectedRow();
                if (selectedRow != -1) {
                    Counselor selected = tableModel.getCounselorAt(selectedRow);
                    populateFields(selected);
                }
            }
        });
    }
         private void populateFields(Counselor c) {
        txtName.setText(c.getName());
        txtEmail.setText(c.getEmail());
        txtPhone.setText(c.getPhone());
        txtSpecialization.setText(c.getSpecialization());
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
