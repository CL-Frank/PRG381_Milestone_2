package com.bcwellness.controller;

import com.bcwellness.dao.FeedbackDAO;
import com.bcwellness.db.DBConnection;
import com.bcwellness.model.Feedback;
import com.bcwellness.dao.FeedbackDAO.FeedbackStats;
import com.bcwellness.exception.ExceptionHandler.DatabaseException;
import com.bcwellness.exception.ExceptionHandler.ValidationException;

import java.sql.Connection;
import java.util.List;

public class FeedbackController {

    private final FeedbackDAO feedbackDAO;

    public FeedbackController() {
        try {
            Connection conn = DBConnection.getConnection();
            DBConnection.InitializeDatabase();
            
            feedbackDAO = new FeedbackDAO(conn);
            
            System.out.println("FeedbackController connected to database.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize FeedbackController", e);
        }
    }

    // CREATE
    public boolean addFeedback(Feedback feedback) {
        try {
            return feedbackDAO.addFeedback(feedback);
        } catch (ValidationException | DatabaseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<Feedback> getAllFeedback() {
        try {
            return feedbackDAO.getAllFeedback();
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // READ BY STUDENT
    public List<Feedback> getFeedbackByStudent(String studentNumber) {
        try {
            return feedbackDAO.getFeedbackByStudent(studentNumber);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // UPDATE
    public boolean updateFeedback(Feedback feedback) {
        try {
            return feedbackDAO.updateFeedback(feedback);
        } catch (ValidationException | DatabaseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteFeedback(int feedbackId) {
        try {
            return feedbackDAO.deleteFeedback(feedbackId);
        } catch (DatabaseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // STATS
    public FeedbackStats getFeedbackStats() {
        try {
            return feedbackDAO.getFeedbackStats();
        } catch (DatabaseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
