/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.controller;

/**
 *
 * @author vunen
 */

import com.bcwellness.db.DBConnection;
import com.bcwellness.view.AppointmentPanel;
import com.bcwellness.view.FeedbackManagementPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class WellnessController {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection conn = DBConnection.getConnection()) {
                JFrame frame = new JFrame("Wellness Management System");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 600);
                frame.setLayout(new BorderLayout());

                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.addTab("Feedback Management", new FeedbackManagementPanel());
                tabbedPane.addTab("Appointment Panel", new AppointmentPanel());

                // Future modules can be added here as new tabs

                frame.add(tabbedPane, BorderLayout.CENTER);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,
                        "Database connection failed: " + e.getMessage(),
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

