/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.controller;

/**
 *
 * @author USER-PC
 */
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class WellnessController {
    private Connection dbConnection;

    public WellnessController(Connection conn) {
        this.dbConnection = conn;
    }
    
    public void showAppointmentDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Book Appointment", true);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        dialog.setSize(300, 200);

        JTextField counselorField = new JTextField(10);
        JTextField dateField = new JTextField(10);
        JTextField timeField = new JTextField(10);

        dialog.add(new JLabel("Counselor:"));
        dialog.add(counselorField);
        dialog.add(new JLabel("Date (YYYY-MM-DD):"));
        dialog.add(dateField);
        dialog.add(new JLabel("Time (HH:MM):"));
        dialog.add(timeField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String counselor = counselorField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            if (validateAppointmentInput(counselor, date, time)) {
                saveAppointment(counselor, date, time);
                dialog.dispose();
            }
        });
        dialog.add(new JLabel());
        dialog.add(saveButton);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    public void showCounselorDialog(JFrame parent, boolean isEdit) {
        JDialog dialog = new JDialog(parent, isEdit ? "Edit Counselor" : "Add Counselor", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setSize(300, 200);

        JTextField nameField = new JTextField(10);
        JTextField specializationField = new JTextField(10);
        JTextField availabilityField = new JTextField(10);

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Specialization:"));
        dialog.add(specializationField);
        dialog.add(new JLabel("Availability:"));
        dialog.add(availabilityField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String specialization = specializationField.getText();
            String availability = availabilityField.getText();
            if (validateCounselorInput(name, specialization, availability)) {
                if (isEdit) {
                    updateCounselor(name, specialization, availability);
                } else {
                    saveCounselor(name, specialization, availability);
                }
                dialog.dispose();
            }
        });
        dialog.add(new JLabel());
        dialog.add(saveButton);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    public void showFeedbackDialog(JFrame parent, boolean isEdit) {
        JDialog dialog = new JDialog(parent, isEdit ? "Edit Feedback" : "Submit Feedback", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(300, 150);

        JTextField ratingField = new JTextField(5);
        JTextField commentsField = new JTextField(20);

        dialog.add(new JLabel("Rating (1-5):"));
        dialog.add(ratingField);
        dialog.add(new JLabel("Comments:"));
        dialog.add(commentsField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String rating = ratingField.getText();
            String comments = commentsField.getText();
            if (validateFeedbackInput(rating, comments)) {
                if (isEdit) {
                    updateFeedback(rating, comments);
                } else {
                    saveFeedback(rating, comments);
                }
                dialog.dispose();
            }
        });
        dialog.add(new JLabel());
        dialog.add(saveButton);

        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    public void refreshAppointments(JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Counselor");
        model.addColumn("Date");
        model.addColumn("Time");
        model.addColumn("Status");
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT counselor, date, time, status FROM Appointments")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("counselor"), rs.getString("date"), 
                    rs.getString("time"), rs.getString("status")});
            }
            table.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching appointments: " + e.getMessage());
        }
    }

    public void refreshCounselors(JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Specialization");
        model.addColumn("Availability");
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, specialization, availability FROM Counselors")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("name"), rs.getString("specialization"), 
                    rs.getString("availability")});
            }
            table.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching counselors: " + e.getMessage());
        }
    }

    public void refreshFeedback(JTable table) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Rating");
        model.addColumn("Comments");
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT rating, comments FROM Feedback")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("rating"), rs.getString("comments")});
            }
            table.setModel(model);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching feedback: " + e.getMessage());
        }
    }
    
    public void deleteCounselor(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) table.getValueAt(selectedRow, 0);
            try (PreparedStatement pstmt = dbConnection.prepareStatement(
                    "DELETE FROM Counselors WHERE name = ?")) {
                pstmt.setString(1, name);
                pstmt.executeUpdate();
                refreshCounselors(table);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error deleting counselor: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a counselor to delete.");
        }
    }

    public void deleteFeedback(JTable table) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String rating = (String) table.getValueAt(selectedRow, 0);
            try (PreparedStatement pstmt = dbConnection.prepareStatement(
                    "DELETE FROM Feedback WHERE rating = ?")) {
                pstmt.setString(1, rating);
                pstmt.executeUpdate();
                refreshFeedback(table);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error deleting feedback: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select feedback to delete.");
        }
    }

    private boolean validateAppointmentInput(String counselor, String date, String time) {
        if (counselor.isEmpty() || date.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required.");
            return false;
        }
        return true;
    }

    private boolean validateCounselorInput(String name, String specialization, String availability) {
        if (name.isEmpty() || specialization.isEmpty() || availability.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required.");
            return false;
        }
        return true;
    }

    private boolean validateFeedbackInput(String rating, String comments) {
        if (rating.isEmpty() || comments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required.");
            return false;
        }
        try {
            int rate = Integer.parseInt(rating);
            if (rate < 1 || rate > 5) {
                JOptionPane.showMessageDialog(null, "Rating must be between 1 and 5.");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Rating must be a number.");
            return false;
        }
        return true;
    }

    private void saveAppointment(String counselor, String date, String time) {
        try (PreparedStatement pstmt = dbConnection.prepareStatement(
                "INSERT INTO Appointments (counselor, date, time, status) VALUES (?, ?, ?, 'Scheduled')")) {
            pstmt.setString(1, counselor);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Appointment booked successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saving appointment: " + e.getMessage());
        }
    }

    private void saveCounselor(String name, String specialization, String availability) {
        try (PreparedStatement pstmt = dbConnection.prepareStatement(
                "INSERT INTO Counselors (name, specialization, availability) VALUES (?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, specialization);
            pstmt.setString(3, availability);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Counselor added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saving counselor: " + e.getMessage());
        }
    }

    private void updateCounselor(String name, String specialization, String availability) {
        JOptionPane.showMessageDialog(null, "Update functionality to be implemented.");
    }

    private void saveFeedback(String rating, String comments) {
        try (PreparedStatement pstmt = dbConnection.prepareStatement(
                "INSERT INTO Feedback (rating, comments) VALUES (?, ?)")) {
            pstmt.setString(1, rating);
            pstmt.setString(2, comments);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Feedback submitted successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saving feedback: " + e.getMessage());
        }
    }

    private void updateFeedback(String rating, String comments) {
        JOptionPane.showMessageDialog(null, "Update functionality to be implemented.");
    }
}
    

