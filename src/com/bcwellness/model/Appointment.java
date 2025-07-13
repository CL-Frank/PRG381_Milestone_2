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
    private String counsellor;
    private int counsellorID;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Appointment(int id, String studentName,int counsellorID, String counsellor, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.studentName = studentName;
        this.counsellorID = counsellorID;
        this.counsellor = counsellor;
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
        return counsellor;
    }
    
    public int getCounsellorID(){
        return counsellorID;
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
