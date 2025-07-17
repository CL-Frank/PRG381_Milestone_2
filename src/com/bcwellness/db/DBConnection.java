package com.bcwellness.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {
//  Database connection parameters 

    private static final String DB_URL = "jdbc:derby:wellness_db; create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
//  Required imports for database and logging operations
    //  Logger for tracking connection events and errors

//    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
//
//    //  Singleton instance (thread-safe)
    private static DBConnection instance;
//
    //  Active database connection
    private Connection connection;
//
//    //  Private constructor for singleton pattern
//    private DBConnection() {
//        initializeConnection();
//    }

    //  Singleton access point (thread-safe)
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

//    //  Initializes the database connection
//    private void initializeConnection() {
//        try {
//            //  Must match your Derby installation
//            Class.forName(DRIVER);
//
//            //  Core connection logic
//            this.connection = DriverManager.getConnection(DB_URL);
//            logger.log(Level.INFO, "Database connection established successfully");
//        } catch (ClassNotFoundException e) {
//            //  Driver configuration error
//            logger.log(Level.SEVERE, "JDBC Driver not found", e);
//            throw new RuntimeException("JDBC Driver not found", e);
//        } catch (SQLException e) {
//            //  Connection failure
//            logger.log(Level.SEVERE, "Connection failed", e);
//            throw new RuntimeException("Database connection failed", e);
//        }
//    }
//    // Gets active connection (auto-reconnects if needed)
//    public  Connection getConnection() throws SQLException {
//        if (connection == null || connection.isClosed()) {
//            reconnect();
//        }
//        return connection;
//    }
    public static Connection getConnection() {
        try {

            Class.forName(DRIVER);
            return DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
//    //  Reconnection logic (thread-safe)
//    private synchronized void reconnect() throws SQLException {
//        try {
//            //  Close stale connection if exists
//            if (connection != null) {
//                connection.close();
//            }
//
//            //  Establish new connection
//            connection = DriverManager.getConnection(DB_URL);
//            logger.log(Level.INFO, "Database reconnected successfully");
//        } catch (SQLException e) {
//            //  Reconnection failure
//            logger.log(Level.SEVERE, "Reconnection failed", e);
//            throw e;
//        }
//    }

//    //  Clean shutdown procedure
//    public static void shutdown() {
//        if (instance != null && instance.connection != null) {
//            try {
//                //  Close connection
//                instance.connection.close();
//                logger.log(Level.INFO, "Database connection closed");
//            } catch (SQLException e) {
//                // Shutdown error
//                logger.log(Level.SEVERE, "Error closing connection", e);
//            }
//        }
//    }
    public static void InitializeDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                return;
            }

            // Get metadata
            java.sql.DatabaseMetaData dbMeta = conn.getMetaData();

            // Check if the "Counsellors" table exists
            ResultSet tables = dbMeta.getTables(null, null, "COUNSELORS", null);
            if (!tables.next()) {
                String createTable = "CREATE TABLE Counselors ("
                        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "name VARCHAR(50), "
                        + "surname VARCHAR(50), "
                        + "email VARCHAR(100), "
                        + "phone VARCHAR(20), "
                        + "specialization VARCHAR(50), "
                        + "availability VARCHAR(50))";

                conn.createStatement().executeUpdate(createTable);

                conn.createStatement().executeUpdate(
                        "INSERT INTO Counselors (name, surname, email, phone, specialization, availability) VALUES "
                        + "('Lisa', 'Ngwenya', 'lisa@wellness.com', '0821234567', 'Burnout', 'Tue-Thu'), "
                        + "('Tebogo', 'Modise', 'tebogo@wellness.com', '0739876543', 'Exam Stress', 'Mon-Fri'), "
                        + "('Aisha', 'Khan', 'aisha@wellness.com', '0840001122', 'Anxiety', 'Wed-Fri')"
                );
                System.out.println("Created 'Counselors' table.");

            } else {
                System.out.println("'counselors' table already exists.");
            }

            // Check if the "Appointments" table exists
            tables = dbMeta.getTables(null, null, "APPOINTMENTS", null);
            if (!tables.next()) {
                // Table does not exist, so create it
                String createAppointmentsTable = "CREATE TABLE Appointments ("
                        + "    id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,"
                        + "    student VARCHAR(50),"
                        + "    counselor_id INT,"
                        + "    date DATE,"
                        + "    time TIME,"
                        + "    status VARCHAR(20),"
                        + "    FOREIGN KEY (counselor_id) REFERENCES counselors(id))";
                conn.createStatement().executeUpdate(createAppointmentsTable);

                conn.createStatement().executeUpdate(
                        "INSERT INTO Appointments (student, counselor_id, date, time, status)\n"
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
                String createFeedbackTable = "CREATE TABLE Feedback ("
                        + "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "student_number VARCHAR(20), "
                        + "name VARCHAR(100), "
                        + "rating INT, "
                        + "comments VARCHAR(255), "
                        + "submitted_at TIMESTAMP"
                        + ")";

                conn.createStatement().executeUpdate(createFeedbackTable);

                conn.createStatement().executeUpdate(
                        "INSERT INTO Feedback (student_number, name, rating, comments, submitted_at) VALUES "
                        + "('SN123', 'Alice Johnson', 5, 'Very helpful session!', TIMESTAMP('2025-07-15 10:23:45')), "
                        + "('SN456', 'Brian Lee', 4, 'Great advice, thank you.', TIMESTAMP('2025-07-14 14:08:00')), "
                        + "('SN789', 'Chipo Moyo', 3, 'Helpful but rushed.', TIMESTAMP('2025-07-12 09:41:33'))"
                );

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
