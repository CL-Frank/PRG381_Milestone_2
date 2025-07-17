package com.bcwellness.db;

//  Required imports for database and logging operations
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    //  Logger for tracking connection events and errors
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    
    //  Singleton instance (thread-safe)
    private static DBConnection instance;
    
    //  Database connection parameters 
    private static final String DB_URL = "jdbc:derby://localhost:1527/wellness_db";
    private static final String USER = "frank";
    private static final String PASSWORD = "0618";
    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";

    public static void closeConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //  Active database connection
    private Connection connection;

    //  Private constructor for singleton pattern
    private DBConnection() {
        initializeConnection();
    }

    //  Singleton access point (thread-safe)
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public static Connection getConnection() {
        return getInstance().connection;
    }
    
    //  Initializes the database connection
    private void initializeConnection() {
        try {
            //  Must match your Derby installation
            Class.forName(DRIVER);
            
            //  Core connection logic
            this.connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            logger.log(Level.INFO, "Database connection established successfully");
        } catch (ClassNotFoundException e) {
            //  Driver configuration error
            logger.log(Level.SEVERE, "JDBC Driver not found", e);
            throw new RuntimeException("JDBC Driver not found", e);
        } catch (SQLException e) {
            //  Connection failure
            logger.log(Level.SEVERE, "Connection failed", e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    // Gets active connection (auto-reconnects if needed)
    public synchronized Connection getStaticConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            reconnect();
        }
        return connection;
    }

    //  Reconnection logic (thread-safe)
    private synchronized void reconnect() throws SQLException {
        try {
            //  Close stale connection if exists
            if (connection != null) {
                connection.close();
            }
            
            //  Establish new connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            logger.log(Level.INFO, "Database reconnected successfully");
        } catch (SQLException e) {
            //  Reconnection failure
            logger.log(Level.SEVERE, "Reconnection failed", e);
            throw e;
        }
    }

    //  Clean shutdown procedure
    public static void shutdown() {
        if (instance != null && instance.connection != null) {
            try {
                //  Close connection
                instance.connection.close();
                logger.log(Level.INFO, "Database connection closed");
            } catch (SQLException e) {
                // Shutdown error
                logger.log(Level.SEVERE, "Error closing connection", e);
            }
        }
    }
}
