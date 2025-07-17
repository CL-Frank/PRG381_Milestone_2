/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.bcwellness.view;

import com.bcwellness.controller.*;
import com.bcwellness.model.*;
import com.bcwellness.db.DBConnection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author frank
 */
public class AppointmentPanel extends javax.swing.JPanel {

    private AppointmentController controller;
    private int selectedAppointmentID = -1;
    private List<Counselor> counselorList = new ArrayList<>();

    /**
     * Creates new form AppointmentPanel
     */
    public AppointmentPanel() {
        initComponents();
        
//        tblAppointments.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                appointmentTableMouseClicked(evt);
//            }
//        });

        tblAppointments.getSelectionModel().addListSelectionListener(e -> {
            // Only trigger when selection is final (not adjusting)
            if (!e.getValueIsAdjusting()) {
                populateFieldsFromSelectedRow();
            }
        });
        

        controller = new AppointmentController();
        
        dpDate.getSettings().setDateRangeLimits(LocalDate.now().plusDays(1), null);

        dpTime.getSettings().setAllowKeyboardEditing(false);
        dpTime.getSettings().use24HourClockFormat();
        LocalTime open = LocalTime.of(8, 0);
        LocalTime close = LocalTime.of(16, 0);
        LocalTime current = open;
        dpTime.getSettings().setVetoPolicy(time -> {
            return !(time.isBefore(open) || time.isAfter(close));
        });
        
        loadAppointments();
        loadCounselorsIntoDropdown();
        loadStatusesIntoDropdown();

        
    }

    private void populateFieldsFromSelectedRow() {
        int row = tblAppointments.getSelectedRow();
        if (row == -1) {
            return;
        }

        try {
            selectedAppointmentID = (int) tblAppointments.getValueAt(row, 0);
            System.out.println("Selected ID: " + selectedAppointmentID);
            if (row == -1) {
                return;
            }

            String student = (String) tblAppointments.getValueAt(row, 2);
            LocalDate date = (LocalDate) tblAppointments.getValueAt(row, 3);
            LocalTime time = (LocalTime) tblAppointments.getValueAt(row, 4);
            String status = (String) tblAppointments.getValueAt(row, 5);
            String counselorName = (String) tblAppointments.getValueAt(row, 1);

            txtStudent1.setText(student);
            dpDate.setDate(date);
            dpTime.setTime(time);
            drpdwnStatus.setSelectedItem(status);
            drpdwnCounselor.setSelectedItem(counselorName);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointment details.");
        }
    }

    private void loadAppointments() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblAppointments.getModel();
            model.setRowCount(0);
            List<Appointment> appointments = controller.getAppointments();

            //Adding Appointments to table, using counsellor name
            for (Appointment a : appointments) {
                model.addRow(new Object[]{
                    a.getId(), a.getCounselorName(), a.getStudentName(), a.getDate(), a.getTime(), a.getStatus()
                });
            }
            // Hide ID column

            tblAppointments.getColumnModel().getColumn(0).setWidth(0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Loading Appointments", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadCounselorsIntoDropdown() {
        try {
            //Should instead call the controller when its made
            CounsellorController counsellorController = new CounsellorController();
            counselorList = counsellorController.getAllCounselors();

            drpdwnCounselor.removeAllItems();
            drpdwnCounselor.addItem("Select Councellor");
            for (Counselor c : counselorList) {
                drpdwnCounselor.addItem(c.getName());  // Display name only
            }
            drpdwnCounselor.setSelectedIndex(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load counselors.");
            e.printStackTrace();
        }
    }

    private void loadStatusesIntoDropdown() {
        drpdwnStatus.removeAllItems();
        drpdwnStatus.addItem("Select Status");
        drpdwnStatus.addItem("Scheduled");
        drpdwnStatus.addItem("Completed");
        drpdwnStatus.addItem("Cancelled");

        drpdwnStatus.setSelectedIndex(0);
    }

    private void clearForm() {
        drpdwnCounselor.setSelectedIndex(0);
        txtStudent1.setText("");
        dpDate.clear();
        dpTime.clear();
        drpdwnStatus.setSelectedIndex(0);
        tblAppointments.clearSelection();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAppointments = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        btnAddAppointment = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtStudent1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        drpdwnStatus = new javax.swing.JComboBox<>();
        drpdwnCounselor = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        dpDate = new com.github.lgooddatepicker.components.DatePicker();
        dpTime = new com.github.lgooddatepicker.components.TimePicker();

        setBackground(new java.awt.Color(102, 204, 255));
        setPreferredSize(new java.awt.Dimension(800, 500));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Appointments");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 40, -1, 34));

        tblAppointments.setAutoCreateRowSorter(true);
        tblAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Counsellor", "Student", "Date", "Time", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAppointments.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tblAppointments.setGridColor(new java.awt.Color(204, 204, 204));
        tblAppointments.setRowHeight(35);
        tblAppointments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAppointments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAppointments.setShowGrid(true);
        tblAppointments.getTableHeader().setReorderingAllowed(false);
        tblAppointments.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(tblAppointments);
        tblAppointments.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblAppointments.getColumnModel().getColumnCount() > 0) {
            tblAppointments.getColumnModel().getColumn(0).setResizable(false);
        }

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, -1, 269));

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 400, -1, -1));

        btnAddAppointment.setText("New Appointment");
        btnAddAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAppointmentActionPerformed(evt);
            }
        });
        add(btnAddAppointment, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 400, -1, -1));

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, -1, -1));

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 400, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Date");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));
        add(txtStudent1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 160, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Student");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Time");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Status");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, -1, -1));

        drpdwnStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pending", "Scheduled", "Completed", "Item 4" }));
        add(drpdwnStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 150, 30));

        drpdwnCounselor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(drpdwnCounselor, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 150, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Counsellor");
        add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));
        add(dpDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, -1, -1));
        add(dpTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAppointmentActionPerformed
        // TODO add your handling code here:
        try {
            String student = txtStudent1.getText().trim();
            LocalDate date = dpDate.getDate();
            LocalTime time = dpTime.getTime();
            String status = (String) drpdwnStatus.getSelectedItem();
            String counselorName = (String) drpdwnCounselor.getSelectedItem();

            // Validate fields
            if (student.isEmpty() || date == null || time == null
                    || status.equals("Select Status") || counselorName.equals("Select Counselor")) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.");
                return;
            }

            int counselorID = -1;
            for (Counselor c : counselorList) {
                if (c.getName().equals(counselorName)) {
                    counselorID = c.getId();
                    break;
                }
            }

            if (counselorID == -1) {
                JOptionPane.showMessageDialog(this, "Counselor not found.");
                return;
            }

            // Create Appointment (ID is 0 or ignored since auto-generated)
            Appointment a = new Appointment(0, student, counselorID, counselorName, date, time, status);

            controller.addAppointment(a);
            JOptionPane.showMessageDialog(this, "Appointment added successfully!");

            loadAppointments();  // Refresh table
            clearForm();         // Reset fields
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding appointment.");
            e.printStackTrace();
        }


    }//GEN-LAST:event_btnAddAppointmentActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblAppointments.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please Select Row To Delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {

                int id = (int) tblAppointments.getValueAt(selectedRow, 0);

                controller.deleteAppointment(id);

                loadAppointments();
                clearForm();
                JOptionPane.showMessageDialog(this, "Appointment Deleted");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error Deleting Appointment", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        try {
            if (selectedAppointmentID == -1) {
                JOptionPane.showMessageDialog(this, "No appointment selected to update.");
                return;
            }

            String student = txtStudent1.getText();
            LocalDate date = dpDate.getDate();
            LocalTime time = dpTime.getTime();
            String status = (String) drpdwnStatus.getSelectedItem();
            String counselorName = (String) drpdwnCounselor.getSelectedItem();

            if (student.isEmpty() || date == null || time == null || drpdwnCounselor.getSelectedIndex() == 0 || drpdwnStatus.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            int counselorID = -1;
            for (Counselor c : counselorList) {
                if (c.getName().equals(counselorName)) {
                    counselorID = c.getId();
                    break;
                }
            }

            if (counselorID == -1) {
                JOptionPane.showMessageDialog(this, "Counselor not found.");
                return;
            }
            Appointment updated = new Appointment(
                    selectedAppointmentID, student, counselorID, counselorName,
                    date, time, status
            );

            controller.updateAppointment(updated);
            JOptionPane.showMessageDialog(this, "Appointment updated successfully.");
            System.out.println(updated.toString());

            loadAppointments(); // refresh table
            clearForm();        // reset fields and ID
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating appointment.");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAppointment;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private com.github.lgooddatepicker.components.DatePicker dpDate;
    private com.github.lgooddatepicker.components.TimePicker dpTime;
    private javax.swing.JComboBox<String> drpdwnCounselor;
    private javax.swing.JComboBox<String> drpdwnStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAppointments;
    private javax.swing.JTextField txtStudent1;
    // End of variables declaration//GEN-END:variables
}
