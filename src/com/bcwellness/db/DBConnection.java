package com.bcwellness.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL = "jdbc:derby:wellness_db; create=true";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
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
