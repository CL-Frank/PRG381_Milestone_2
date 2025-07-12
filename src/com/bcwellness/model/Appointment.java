/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.model;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author USER-PC
 */
public class Appointment {
    private String id;
    private String studentName;
    private String councellorName;
    private String date;
    private String time;
    private String status;

    public Appointment(String id, String studentName, String councellorName, String date, String time, String status) {
        this.id = id;
        this.studentName = studentName;
        this.councellorName = councellorName;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    
    

    public String getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCouncellorName() {
        return councellorName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
    
    
    
}
