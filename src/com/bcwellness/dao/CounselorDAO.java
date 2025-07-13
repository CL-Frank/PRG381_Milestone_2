package com.bcwellness.dao;

import com.bcwellness.model.Counselor;
import java.sql.*;
import java.util.*;

public class CounselorDAO {
    private Connection conn;

    public CounselorDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Counselor> getAllCounselors() throws SQLException {
        List<Counselor> list = new ArrayList<>();
        String sql = "SELECT * FROM Counsellors";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Counselor c = new Counselor(
            (rs.getInt("id")),
            (rs.getString("name")),
            (rs.getString("specialization")),
            (rs.getString("availability"))
            );
            list.add(c);
        }

        return list;
    }

    // Optional: add getById() or getByName() if needed later
}
