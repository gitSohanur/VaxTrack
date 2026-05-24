package com.vaxtrack.model;

import java.time.LocalDate;

/**
 * Represents a single vaccination event for a patient.
 * Links Patient ↔ Vaccine.
 * Demonstrates: Encapsulation, Inheritance, Composition
 */
public class VaccineRecord extends BaseEntity {

    private int       patientId;
    private int       vaccineId;
    private int       doseNumber;
    private LocalDate dateGiven;
    private String    administeredBy;
    private String    notes;

    // For display purposes (joined from DB)
    private String patientName;
    private String vaccineName;

    // --- Constructors ---

    public VaccineRecord() {
        super();
    }

    public VaccineRecord(int id, int patientId, int vaccineId,
                         int doseNumber, LocalDate dateGiven,
                         String administeredBy, String notes) {
        super(id);
        this.patientId      = patientId;
        this.vaccineId      = vaccineId;
        this.doseNumber     = doseNumber;
        this.dateGiven      = dateGiven;
        this.administeredBy = administeredBy;
        this.notes          = notes;
    }

    // --- Getters & Setters ---

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getVaccineId() { return vaccineId; }
    public void setVaccineId(int vaccineId) { this.vaccineId = vaccineId; }

    public int getDoseNumber() { return doseNumber; }
    public void setDoseNumber(int doseNumber) { this.doseNumber = doseNumber; }

    public LocalDate getDateGiven() { return dateGiven; }
    public void setDateGiven(LocalDate dateGiven) { this.dateGiven = dateGiven; }

    public String getAdministeredBy() { return administeredBy; }
    public void setAdministeredBy(String administeredBy) { this.administeredBy = administeredBy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getVaccineName() { return vaccineName; }
    public void setVaccineName(String vaccineName) { this.vaccineName = vaccineName; }

    // --- Displayable implementation ---

    @Override
    public String getDisplayInfo() {
        return "Record[" + getId() + "] " +
                (patientName != null ? patientName : "Patient#" + patientId) +
                " → " +
                (vaccineName != null ? vaccineName : "Vaccine#" + vaccineId) +
                " | Dose: " + doseNumber +
                " | Date: " + dateGiven;
    }
}

