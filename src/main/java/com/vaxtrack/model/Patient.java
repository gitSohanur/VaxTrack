package com.vaxtrack.model;

import java.time.LocalDate;
import java.time.Period;

/**
 * Represents a patient in the system.
 * Demonstrates: Encapsulation, Inheritance, Computed property (age)
 */
public class Patient extends BaseEntity {

    private String    fullName;
    private LocalDate dateOfBirth;
    private String    gender;
    private String    phone;
    private String    address;

    // --- Constructors ---

    public Patient() {
        super();
    }

    public Patient(int id, String fullName, LocalDate dateOfBirth,
                   String gender, String phone, String address) {
        super(id);
        this.fullName    = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender      = gender;
        this.phone       = phone;
        this.address     = address;
    }

    // --- Computed property ---

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // --- Getters & Setters ---

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // --- Displayable implementation ---

    @Override
    public String getDisplayInfo() {
        return "Patient[" + getId() + "] " + fullName +
                " | Age: " + getAge() +
                " | Gender: " + gender +
                " | Phone: " + phone;
    }
}
