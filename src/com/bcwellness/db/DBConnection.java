package com.bcwellness.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
//  Database connection parameters 
    private static final String DB_URL = "jdbc:derby:wellness_db; create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
//  Required imports for database and logging operations
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
    //  Logger for tracking connection events and errors
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    
    //  Singleton instance (thread-safe)
    private static DBConnection instance;
    

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
    public Connection getConnection() throws SQLException {
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

    public static void InitializeDatabase() {
        try (Connection conn = getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                return;
            }

            // Get metadata
            java.sql.DatabaseMetaData dbMeta = conn.getMetaData();

            // Check if the "Counsellors" table exists
            ResultSet tables = dbMeta.getTables(null, null, "COUNSELLORS", null);
            if (!tables.next()) {
                String createTable = "CREATE TABLE Counsellors ("
                        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "name VARCHAR(50), "
                        + "specialization VARCHAR(50), "
                        + "availability VARCHAR(20))";
                conn.createStatement().executeUpdate(createTable);

                conn.createStatement().executeUpdate(
                        "INSERT INTO Counsellors (name, specialization, availability) VALUES "
                        + "('Dr. Smith', 'Anxiety', 'Mon-Wed'), "
                        + "('Dr. Patel', 'Depression', 'Tue-Thu'), "
                        + "('Dr. Mokoena', 'Academic Stress', 'Mon-Fri')");
                System.out.println("Created 'Counsellors' table.");
            } else {
                System.out.println("'counsellors' table already exists.");
            }

            // Check if the "Appointments" table exists
            tables = dbMeta.getTables(null, null, "APPOINTMENTS", null);
            if (!tables.next()) {
                // Table does not exist, so create it
                String createAppointmentsTable = "CREATE TABLE Appointments ("
                        + "    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                        + "    student VARCHAR(50),"
                        + "    counsellor_id INT,"
                        + "    date DATE,"
                        + "    time TIME,"
                        + "    status VARCHAR(20),"
                        + "    FOREIGN KEY (counsellor_id) REFERENCES counsellors(id))";
                conn.createStatement().executeUpdate(createAppointmentsTable);
                
                conn.createStatement().executeUpdate(
                        "INSERT INTO Appointments (student, counsellor_id, date, time, status)\n"
                        + "VALUES \n"
                        + "  ('Alice Johnson', 1, '2025-07-15', '10:00:00', 'Scheduled'),\n"
                        + "  ('Brian Lee', 2, '2025-07-16', '14:30:00', 'Scheduled'),\n"
                        + "  ('Chipo Moyo', 3, '2025-07-17', '09:00:00', 'Completed')");
                System.out.println("Created 'Appointments' table.");
            } else {
                System.out.println("'Appointments' table already exists.");
            }

            // Check if the "Feedback" table exists
            tables = dbMeta.getTables(null, null, "FEEDBACK", null);
            if (!tables.next()) {
                // Table does not exist, so create it
                String createFeedbackTable = "CREATE TABLE Feedback ("
                        + "    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                        + "    student VARCHAR(50),"
                        + "    rating INT,"
                        + "    comments VARCHAR(255)"
                        + ")";
                conn.createStatement().executeUpdate(createFeedbackTable);
                        conn.createStatement().executeUpdate(
                                "INSERT INTO Feedback (student, rating, comments)"
                        + "VALUES "
                        + "  ('Alice Johnson', 5, 'Very helpful session!'),"
                        + "  ('Brian Lee', 4, 'Great advice, thank you.'),"
                        + "  ('Chipo Moyo', 3, 'Helpful but rushed.')");
                System.out.println("Created 'Feedback' table.");
            } else {
                System.out.println("'Feedback' table already exists.");
            }
            conn.close();

            // Repeat for other tables like appointments, feedback, etc.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
