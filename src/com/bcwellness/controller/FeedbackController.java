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
    
    public List<Feedback> searchFeedbackByStudentNumber(String studentNumber) {
        return getFeedbackByStudent(studentNumber);
    }
    
    // SUBMIT NEW FEEDBACK
    public boolean submitFeedback(String studentNumber, String name, int rating, String comments) {
        Feedback feedback = new Feedback();
        feedback.setStudentNumber(studentNumber);
        feedback.setStudentName(name);
        feedback.setRating(rating);
        feedback.setComments(comments);
        return addFeedback(feedback);
    }

    // UPDATE EXISTING FEEDBACK
    public boolean updateFeedback(int feedbackId, String name, int rating, String comments) {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(feedbackId);
        feedback.setStudentName(name);
        feedback.setRating(rating);
        feedback.setComments(comments);
        return updateFeedback(feedback);
    }
     
    
}
