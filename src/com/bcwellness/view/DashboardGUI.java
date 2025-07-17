/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.view;

/**
 *
 * @author yanga
 */
import com.bcwellness.controller.AppointmentController;
import com.bcwellness.controller.CounselorController;
import com.bcwellness.controller.FeedbackController;
import com.bcwellness.db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;


public class DashboardGUI extends JFrame {

    private static Connection conn;
    private AppointmentController appointmentController;
    private CounselorController counselorController;
    private FeedbackController feedbackController;
    private JTabbedPane tabbedPane;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    private JPanel headerPanel;
    private JMenuBar menuBar;
    private String currentUser;
    private Connection dbConnection;

    public DashboardGUI(Connection dbConnection) {
        this.dbConnection = dbConnection;
        if (this.dbConnection == null){
            JOptionPane.showMessageDialog(this, "Database connection failed. Exiting.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
      
        initializeSystem();
        setupGUI();
        setupMenuBar();
        setupEventHandlers();
    }

    private void initializeSystem() {
        try {
            appointmentController = new AppointmentController(dbConnection);
            counselorController = new CounselorController(dbConnection);
            feedbackController = new FeedbackController(dbConnection);

            appointmentController.initializeTabContent();
            counselorController.initializeTabContent();
            feedbackController.initializeTabContent();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error initializing system: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Initialization error: " + e.getMessage());
        }
    }

    private void setupGUI() {
        setTitle("BC Student Wellness Management System - " + new java.util.Date());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        createHeaderPanel();
        createTabbedPane();

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        applySystemStyling();
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        welcomeLabel = new JLabel("Welcome to BC Student Wellness Management System");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
    }

    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        tabbedPane.addTab("Appointments", appointmentController.getTabComponent());
        tabbedPane.addTab("Counselors", counselorController.getTabComponent());
        tabbedPane.addTab("Feedback", feedbackController.getTabComponent());

        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
    }

    private void setupMenuBar() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> confirmExit());
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem appointmentsItem = new JMenuItem("Appointments");
        JMenuItem counselorsItem = new JMenuItem("Counselors");
        JMenuItem feedbackItem = new JMenuItem("Feedback");

        appointmentsItem.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        counselorsItem.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        feedbackItem.addActionListener(e -> tabbedPane.setSelectedIndex(2));

        viewMenu.add(appointmentsItem);
        viewMenu.add(counselorsItem);
        viewMenu.add(feedbackItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void setupEventHandlers() {
        logoutButton.addActionListener(e -> confirmLogout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            refreshCurrentTab(selectedIndex);
        });
    }

    private void applySystemStyling() {
        try {
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(Color.BLACK);

        Font systemFont = new Font("Arial", Font.PLAIN, 12);
        UIManager.put("Button.font", systemFont);
        UIManager.put("Label.font", systemFont);
        UIManager.put("TextField.font", systemFont);
        UIManager.put("Table.font", systemFont);
    }

    private void refreshCurrentTab(int tabIndex) {
        switch (tabIndex) {
            case 0:
                appointmentController.refreshTab();
                break;
            case 1:
                counselorController.refreshTab();
                break;
            case 2:
                feedbackController.refreshTab();
                break;
        }
    }

    public void setCurrentUser(String username) {
        this.currentUser = username;
        welcomeLabel.setText("Welcome, " + username + " - BC Student Wellness Management System");
    }

    private void confirmLogout() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            performLogout();
        }
    }

    private void performLogout() {
        currentUser = null;
        
        try {
            appointmentController.closeConnections();
            counselorController.closeConnections();
            feedbackController.closeConnections();
            DBConnection.closeConnection();
        } catch (Exception e) {
            System.err.println("Error closing connections: " + e.getMessage());
        }
        
        setVisible(false);
        new LoginGUI().setVisible(true);
        dispose();
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            performLogout();
        }
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
            this,
            "BC Student Wellness Management System\n" +
            "Version 1.0\n" +
            "Developed for Belgium Campus\n" +
            "Programming 37(8)1 - Project 2025\n" +
            "Last Updated: " + new java.util.Date(),
            "About",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DashboardGUI dashboard = new DashboardGUI(conn);
                dashboard.setCurrentUser("Test User"); 
                dashboard.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(), 
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

}