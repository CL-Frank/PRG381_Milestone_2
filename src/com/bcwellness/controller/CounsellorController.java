package com.bcwellness.controller;

import com.bcwellness.dao.CounselorDAO;
import com.bcwellness.db.DBConnection;
import com.bcwellness.model.Counselor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CounsellorController {

    private final CounselorDAO counselorDAO;

    public CounsellorController() {
        try {
            Connection conn = DBConnection.getConnection();

            counselorDAO = new CounselorDAO(); // DAO manages its own connection internally
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize CounselorController", e);
        }
    }

    // CREATE
    public boolean addCounselor(Counselor counselor) {
        try {
            return counselorDAO.addCounselor(counselor);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<Counselor> getAllCounselors() {
        try {
            return counselorDAO.getAllCounselors();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // READ BY ID
    public Counselor getCounselorById(int id) {
        try {
            return counselorDAO.getCounselorById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // UPDATE
    public boolean updateCounselor(Counselor counselor) {
        try {
            return counselorDAO.updateCounselor(counselor);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteCounselor(int id) {
        try {
            return counselorDAO.deleteCounselor(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // VALIDATION
    public boolean emailExists(String email, int excludeId) {
        try {
            return counselorDAO.emailExists(email, excludeId);
        } catch (SQLException e) {
            e.printStackTrace();
            return true; // safer to assume exists in case of failure
        }
    }
}
