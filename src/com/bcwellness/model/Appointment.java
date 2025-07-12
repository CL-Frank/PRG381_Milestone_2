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
    private int id;
    private String studentName;
    private int councellorID;
    private Date date;
    private Time time;
    private String status;

    public Appointment(int id, String studentName, int councellorID, Date date, Time time, String status) {
        this.id = id;
        this.studentName = studentName;
        this.councellorID = councellorID;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    
    

    public int getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getCouncellorID() {
        return councellorID;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
    
    
    
}
