/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.gui;

/**
 *
 * @author vunen
 */

import com.bcwellness.dao.FeedbackDAO;
import com.bcwellness.model.Feedback;
import com.bcwellness.exception.ExceptionHandler;
import com.bcwellness.exception.ExceptionHandler.DatabaseException;
import com.bcwellness.exception.ExceptionHandler.ValidationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FeedbackManagementPanel extends JPanel {
    private final FeedbackDAO feedbackDAO;
    private final JTable feedbackTable;
    private final DefaultTableModel tableModel;

    public FeedbackManagementPanel(Connection connection) {
        this.feedbackDAO = new FeedbackDAO(connection);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{
            "ID", "Student Number", "Name", "Rating", "Comments", "Submitted At"
        }, 0);
        feedbackTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnStats = new JButton("View Stats");

        btnRefresh.addActionListener(e -> loadFeedback());
        btnDelete.addActionListener(e -> deleteSelectedFeedback());
        btnStats.addActionListener(e -> showStats());

        controlPanel.add(btnRefresh);
        controlPanel.add(btnDelete);
        controlPanel.add(btnStats);
        add(controlPanel, BorderLayout.SOUTH);

        loadFeedback();
    }

    private void loadFeedback() {
        try {
            tableModel.setRowCount(0);
            List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Feedback f : feedbackList) {
                tableModel.addRow(new Object[]{
                    f.getFeedbackId(),
                    f.getStudentNumber(),
                    f.getStudentName(),
                    f.getRating(),
                    f.getComments(),
                    f.getSubmittedAt().format(formatter)
                });
            }
        } catch (DatabaseException e) {
            ExceptionHandler.handleDatabaseError("fetching feedback", e);
        }
    }

    private void deleteSelectedFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow >= 0) {
            int feedbackId = (int) tableModel.getValueAt(selectedRow, 0);
            if (ExceptionHandler.showConfirmDialog("Are you sure you want to delete this feedback?")) {
                try {
                    if (feedbackDAO.deleteFeedback(feedbackId)) {
                        ExceptionHandler.showSuccessMessage("Feedback deleted successfully.");
                        loadFeedback();
                    }
                } catch (DatabaseException e) {
                    ExceptionHandler.handleDatabaseError("deleting feedback", e);
                }
            }
        } else {
            ExceptionHandler.handleValidationError("Please select a feedback entry to delete.");
        }
    }

    private void showStats() {
        try {
            FeedbackDAO.FeedbackStats stats = feedbackDAO.getFeedbackStats();
            String message = String.format(
                "Total Feedback: %d\nAverage Rating: %.2f\nHighest Rating: %d\nLowest Rating: %d",
                stats.getTotalFeedback(),
                stats.getAverageRating(),
                stats.getHighestRating(),
                stats.getLowestRating()
            );
            JOptionPane.showMessageDialog(this, message, "Feedback Stats", JOptionPane.INFORMATION_MESSAGE);
        } catch (DatabaseException e) {
            ExceptionHandler.handleDatabaseError("loading statistics", e);
        }
    }
}

