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
    
    public AppointmentDAO(Connection conn){
        this.conn = conn;
    }
    
    //Counselor Id/Name possibly use a drop down, still have to insert ID though
    public void addAppointment(Appointment a) throws SQLException{
        String sql = "INSERT INTO APPOINTMENTS (student, counsellor_ID, date, time, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, a.getStudentName());
        stmt.setInt(2, a.getCounselorID());
        stmt.setDate(3, java.sql.Date.valueOf(a.getDate()));
        stmt.setTime(4, java.sql.Time.valueOf(a.getTime()));
        stmt.setString(5, a.getStatus());
        stmt.executeUpdate();
    }
    
    //Getting the counsellor name from the Counsellor table into appointment
    public ArrayList<Appointment> getAllAppointments() throws SQLException{
        ArrayList<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.id, a.student, a.counsellor_ID, c.name AS counselorName, a.date, a.time, a.status "
           + "FROM Appointments a JOIN Counsellors c ON a.counsellor_ID = c.id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while(rs.next()){
            Appointment a = new Appointment(
            rs.getInt("id"),
            rs.getString("student"),
            rs.getInt("counsellor_ID"),
            rs.getString("counselorName"),
            rs.getDate("date").toLocalDate(),
            rs.getTime("time").toLocalTime(),
            rs.getString("status")
            );
            list.add(a);
        }
        
        return list;
    }
    
    public void updateAppointment(Appointment a)throws SQLException{
        String sql = "UPDATE APPOINTMENTS SET student = ?, counsellor_id = ?, date = ?, time = ?, status = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, a.getStudentName());
        stmt.setInt(2, a.getCounselorID());
        stmt.setDate(3, java.sql.Date.valueOf(a.getDate()));
        stmt.setTime(4, java.sql.Time.valueOf(a.getTime()));
        stmt.setString(5, a.getStatus());
        stmt.setInt(6, a.getId());
        stmt.executeUpdate();
        
        
    }
    public void deleteAppointment(int id) throws SQLException{
            String sql = "DELETE FROM APPOINTMENTS WHERE id =?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
}
