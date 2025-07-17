/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.dao;

/**
 *
 * @author vunen
 */
import com.bcwellness.model.Feedback;
import com.bcwellness.exception.ExceptionHandler;
import com.bcwellness.exception.ExceptionHandler.DatabaseException;
import com.bcwellness.exception.ExceptionHandler.ValidationException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {
    private Connection connection;

    public FeedbackDAO(Connection connection) {
        this.connection = connection;
//        createFeedbackTable();
    }

//    private void createFeedbackTable() {
//        String sql = "CREATE TABLE IF NOT EXISTS feedback ("
//        + "feedback_id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
//        + "student_number VARCHAR(20) NOT NULL, "
//        + "student_name VARCHAR(100) NOT NULL, "
//        + "rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5), "
//        + "comments CLOB, "
//        + "submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
//        + ")";
//
//
//        try (Statement stmt = connection.createStatement()) {
//            stmt.execute(sql);
//        } catch (SQLException e) {
//            ExceptionHandler.handleDatabaseError("table creation", e);
//        }
//    }

    public boolean addFeedback(Feedback feedback) throws DatabaseException, ValidationException {
        validateFeedback(feedback);

        String sql = "INSERT INTO feedback (student_number, student_name, rating, comments) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, feedback.getStudentNumber());
            pstmt.setString(2, feedback.getStudentName());
            pstmt.setInt(3, feedback.getRating());
            pstmt.setString(4, feedback.getComments());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        feedback.setFeedbackId(keys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add feedback", e);
        }
    }

    public List<Feedback> getAllFeedback() throws DatabaseException {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM Feedback ORDER BY submitted_at DESC";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToFeedback(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve feedback", e);
        }

        return list;
    }

    public List<Feedback> getFeedbackByStudent(String studentNumber) throws DatabaseException {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM feedback WHERE student_number = ? ORDER BY submitted_at DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, studentNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToFeedback(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve student feedback", e);
        }

        return list;
    }

    public boolean updateFeedback(Feedback feedback) throws DatabaseException, ValidationException {
        validateFeedback(feedback);
        String sql = "UPDATE feedback SET rating = ?, comments = ? WHERE feedback_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, feedback.getRating());
            pstmt.setString(2, feedback.getComments());
            pstmt.setInt(3, feedback.getFeedbackId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update feedback", e);
        }
    }

    public boolean deleteFeedback(int feedbackId) throws DatabaseException {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, feedbackId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete feedback", e);
        }
    }

    public FeedbackStats getFeedbackStats() throws DatabaseException {
        String sql = "SELECT "
        + "COUNT(*) as total_feedback, "
        + "AVG(CAST(rating AS DOUBLE)) as average_rating, "
        + "MAX(rating) as highest_rating, "
        + "MIN(rating) as lowest_rating "
        + "FROM feedback";


        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new FeedbackStats(
                    rs.getInt("total_feedback"),
                    rs.getDouble("average_rating"),
                    rs.getInt("highest_rating"),
                    rs.getInt("lowest_rating")
                );
            }
            return new FeedbackStats(0, 0.0, 0, 0);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve feedback statistics", e);
        }
    }

    private void validateFeedback(Feedback feedback) throws ValidationException {
        if (feedback.getStudentNumber() == null || feedback.getStudentNumber().trim().isEmpty()) {
            throw new ValidationException("Student number is required");
        }
        if (feedback.getStudentName() == null || feedback.getStudentName().trim().isEmpty()) {
            throw new ValidationException("Student name is required");
        }
        if (feedback.getRating() < 1 || feedback.getRating() > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }
        if (feedback.getComments() != null && feedback.getComments().length() > 1000) {
            throw new ValidationException("Comments must be less than 1000 characters");
        }
    }

    private Feedback mapResultSetToFeedback(ResultSet rs) throws SQLException {
        return new Feedback(
            rs.getInt("id"),
            rs.getString("student_number"),
            rs.getString("name"),
            rs.getInt("rating"),
            rs.getString("comments"),
            rs.getTimestamp("submitted_at").toLocalDateTime()
        );
    }

    public static class FeedbackStats {
        private final int totalFeedback;
        private final double averageRating;
        private final int highestRating;
        private final int lowestRating;

        public FeedbackStats(int total, double average, int high, int low) {
            this.totalFeedback = total;
            this.averageRating = average;
            this.highestRating = high;
            this.lowestRating = low;
        }

        public int getTotalFeedback() { return totalFeedback; }
        public double getAverageRating() { return averageRating; }
        public int getHighestRating() { return highestRating; }
        public int getLowestRating() { return lowestRating; }
    }
}

