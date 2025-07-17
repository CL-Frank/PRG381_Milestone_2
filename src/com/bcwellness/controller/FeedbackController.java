/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.controller;

/**
 *
 * @author yanga
 */
import java.awt.BorderLayout;
import javax.swing.*;
import java.sql.Connection;

public class FeedbackController {
    
    private Connection dbConnection;
    private JPanel tabPanel;

    public FeedbackController(Connection conn) {
        this.dbConnection = conn;
    }

    public void initializeTabContent() {
        tabPanel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        JButton bookButton = new JButton("Book Feedback");
       
        tabPanel.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public JComponent getTabComponent() {
        return tabPanel;
    }

    public void refreshTab() {
        
    }

    public void closeConnections() {
        
    }
}

