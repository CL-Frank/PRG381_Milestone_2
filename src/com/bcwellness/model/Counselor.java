package com.bcwellness.model;

import java.util.Objects;

public class Counselor {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String specialization;
    private String availability;

    // Constructors
    public Counselor() {}

    public Counselor(String name, String surname, String email, String phone, 
                    String specialization, String availability) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.availability = availability;
    }

    /**
     * Full constructor including ID (for existing counselors)
     */
    public Counselor(int id, String name, String surname, String email, String phone,
                    String specialization, String availability) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.availability = availability;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    /**
     * Returns the counselor's full name
     */
    public String getFullName() {
        return name + " " + surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counselor counselor = (Counselor) o;
        return id == counselor.id && 
               Objects.equals(email, counselor.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Counselor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", specialization='" + specialization + '\'' +
                ", availability='" + availability + '\'' +
                '}';
    }
}
