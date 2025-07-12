/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.dao;
import com.bcwellness.model.Appointment;
import com.bcwellness.db.DBConnection;
import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AppointmentDAO {
    private Connection conn;
    
    public void addAppointment(Appointment a) throws SQLException{
        String sql = "INSERT INTO APPOINTMENTS (studentName, councellorID, date, time, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, a.getStudentName());
        stmt.setString(2, a.getCouncellorName());
        stmt.setString(3, a.getDate());
        stmt.setString(4, a.getTime());
        stmt.setString(5, a.getStatus());
    }
    
    public ArrayList<Appointment> getAllAppointments() throws SQLException{
        ArrayList<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM APPOINTMENTS";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()){
            Appointment a = new Appointment(
            rs.getString("id"),
            rs.getString("studentName"),
            rs.getString("councellorID"),
            rs.getString("date"),
            rs.getString("time"),
            rs.getString("status")
            );
            list.add(a);
        }
        
        return list;
    }
    
    public void UpdateAppointment(Appointment a)throws SQLException{
        String sql = "UPDATE APPOINTMENTS SET studentName = ?, councellorID = ?, date = ?, time = ?, status = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, a.getStudentName());
        stmt.setString(2, a.getCouncellorName());
        stmt.setString(3, a.getDate());
        stmt.setString(4, a.getTime());
        stmt.setString(5, a.getStatus());
        stmt.setString(6, a.getId());
        stmt.executeQuery();
        
        
    }
    public void deleteAppointment(String id) throws SQLException{
            String sql = "DELETE FROM APPOINTMENTS WHERE id =?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.executeQuery();
        }
}
