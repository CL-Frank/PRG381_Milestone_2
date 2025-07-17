/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.exception;

/**
 *
 * @author vunen
 */

import javax.swing.*;
import java.util.logging.*;

public class ExceptionHandler {
    private static final Logger logger = Logger.getLogger(ExceptionHandler.class.getName());

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class DatabaseException extends Exception {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BusinessLogicException extends Exception {
        public BusinessLogicException(String message) {
            super(message);
        }
    }

    public static void handleValidationError(String message) {
        logger.log(Level.WARNING, "Validation Error: {0}", message);
        JOptionPane.showMessageDialog(null, message, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    public static void handleDatabaseError(String operation, Exception e) {
        String message = "Database error during " + operation + ": " + e.getMessage();
        logger.log(Level.SEVERE, message, e);
        JOptionPane.showMessageDialog(null,
                "Database operation failed. Please try again or contact support.",
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void handleBusinessLogicError(String message) {
        logger.log(Level.WARNING, "Business Logic Error: {0}", message);
        JOptionPane.showMessageDialog(null, message, "Operation Error", JOptionPane.WARNING_MESSAGE);
    }

    public static void handleSystemError(String operation, Exception e) {
        String message = "System error during " + operation + ": " + e.getMessage();
        logger.log(Level.SEVERE, message, e);
        JOptionPane.showMessageDialog(null,
                "An unexpected error occurred. Please restart the application.",
                "System Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(null, message, "Confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
} 

