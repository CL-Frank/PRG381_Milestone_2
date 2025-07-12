package com.bcwellness.controller;

import com.bcwellness.dao.AppointmentDAO;
import com.bcwellness.db.DBConnection;
import com.bcwellness.model.Appointment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class AppointmentController {
    private AppointmentDAO appointmentDAO;

    public AppointmentController() {
        try {
            Connection conn = DBConnection.getConnection();
            appointmentDAO = new AppointmentDAO(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAppointment(String student, int counselorID, Date date, Time time, String status) {
        try {
            Appointment a = new Appointment(0, student, counselorID, date, time, status);
            appointmentDAO.addAppointment(a);
        } catch (Exception e) {
            e.printStackTrace(); // Or handle more gracefully
        }
    }

    public List<Appointment> getAppointments() {
        try {
            return appointmentDAO.getAllAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateAppointment(Appointment a) {
        try {
            appointmentDAO.updateAppointment(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAppointment(int id) {
        try {
            appointmentDAO.deleteAppointment(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
