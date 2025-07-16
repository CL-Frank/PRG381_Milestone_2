package com.bcwellness.dao;

import com.bcwellness.model.Counselor;
import com.bcwellness.db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CounselorDAO {
    private final DBConnection dbConnection;

    public CounselorDAO() {
        this.dbConnection = DBConnection.getInstance();
    }

    // CREATE
    public boolean addCounselor(Counselor counselor) throws SQLException {
        String sql = "INSERT INTO Counselors (name, surname, email, phone, specialization, availability) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            setStatementParameters(stmt, counselor);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        counselor.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // READ (All)
    public List<Counselor> getAllCounselors() throws SQLException {
        List<Counselor> counselors = new ArrayList<>();
        String sql = "SELECT * FROM Counselors";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                counselors.add(mapResultSetToCounselor(rs));
            }
        }
        return counselors;
    }

    // READ (Single)
    public Counselor getCounselorById(int id) throws SQLException {
        String sql = "SELECT * FROM Counselors WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCounselor(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public boolean updateCounselor(Counselor counselor) throws SQLException {
        String sql = "UPDATE Counselors SET name = ?, surname = ?, email = ?, phone = ?, " +
                     "specialization = ?, availability = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            setStatementParameters(stmt, counselor);
            stmt.setInt(7, counselor.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    // DELETE
    public boolean deleteCounselor(int id) throws SQLException {
        String sql = "DELETE FROM Counselors WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // VALIDATION
    public boolean emailExists(String email, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Counselors WHERE email = ? AND id != ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setInt(2, excludeId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // HELPER METHODS
    private void setStatementParameters(PreparedStatement stmt, Counselor counselor) throws SQLException {
        stmt.setString(1, counselor.getName());
        stmt.setString(2, counselor.getSurname());
        stmt.setString(3, counselor.getEmail());
        stmt.setString(4, counselor.getPhone());
        stmt.setString(5, counselor.getSpecialization());
        stmt.setString(6, counselor.getAvailability());
    }

    private Counselor mapResultSetToCounselor(ResultSet rs) throws SQLException {
        return new Counselor(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("surname"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("specialization"),
            rs.getString("availability")
        );
    }
}
