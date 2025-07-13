/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bcwellness.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author USER-PC
 */
public class Appointment {
    private int id;
    private String studentName;
//    counsellor name will be gotten from counsellor table 
    private String counselor;
    private int counselorID;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Appointment(int id, String studentName,int counselorID, String counselor, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.studentName = studentName;
        this.counselorID = counselorID;
        this.counselor = counselor;
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

    public String getCounselorName  () {
        return counselor;
    }
    
    public int getCounselorID(){
        return counselorID;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
    
    
    
}
