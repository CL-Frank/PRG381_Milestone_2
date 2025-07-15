package com.bcwellness.db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    private static DBConnection instance;
    
    // Your existing configuration
    private static final String DB_URL = "jdbc:derby://localhost:1527/wellness_db";
    private static final String USER = "frank";
    private static final String PASSWORD = "0618";
    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";

    private Connection connection;

    private DBConnection() {
        initializeConnection();
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private void initializeConnection() {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            logger.log(Level.INFO, "Database connection established successfully");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "JDBC Driver not found", e);
            throw new RuntimeException("JDBC Driver not found", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Connection failed", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            reconnect();
        }
        return connection;
    }

    private synchronized void reconnect() throws SQLException {
        try {
            if (connection != null) {
                connection.close();
            }
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            logger.log(Level.INFO, "Database reconnected successfully");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Reconnection failed", e);
            throw e;
        }
    }

    public static void shutdown() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
                logger.log(Level.INFO, "Database connection closed");
                // For Derby network server, we don't shutdown the entire DB
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Error closing connection", e);
            }
        }
    }
}
