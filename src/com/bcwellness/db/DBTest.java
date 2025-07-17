package com.bcwellness.db;

import java.sql.Connection;

public class DBTest {
    public static void main(String[] args) {
        try {
            // Get the singleton instance, then call getConnection()
            Connection conn = DBConnection.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
