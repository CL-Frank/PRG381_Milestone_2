/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.model;

/**
 *
 * @author vunen
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private int feedbackId;
    private String studentNumber;
    private String studentName;
    private int rating;
    private String comments;
    private LocalDateTime submittedAt;

    public Feedback() {
        this.submittedAt = LocalDateTime.now();
    }

    public Feedback(String studentNumber, String studentName, int rating, String comments) {
        this();
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.rating = rating;
        this.comments = comments;
    }

    public Feedback(int feedbackId, String studentNumber, String studentName, int rating, String comments, LocalDateTime submittedAt) {
        this.feedbackId = feedbackId;
        this.studentNumber = studentNumber;
        this.studentName = studentName;
        this.rating = rating;
        this.comments = comments;
        this.submittedAt = submittedAt;
    }

    public int getFeedbackId() { return feedbackId; }
    public void setFeedbackId(int feedbackId) { this.feedbackId = feedbackId; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public String getFormattedSubmittedAt() {
        return submittedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getRatingAsStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            stars.append(i <= rating ? "★" : "☆");
        }
        return stars.toString();
    }

    @Override
    public String toString() {
        return String.format("Feedback[ID=%d, Student=%s, Rating=%d, Date=%s]", feedbackId, studentName, rating, getFormattedSubmittedAt());
    }
}

