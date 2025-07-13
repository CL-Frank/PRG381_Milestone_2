package com.bcwellness.model;

public class Counselor {
    private int id;
    private String name;
    private String specialization;
    private String availability;

    public Counselor() {}

    public Counselor(int id, String name, String specialization, String availability) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.availability = availability;
    }

    // Getters and setters
    public int getId() { return id; }

    public String getName() { return name; }

    public String getSpecialization() { return specialization; }

    public String getAvailability() { return availability; }
}
