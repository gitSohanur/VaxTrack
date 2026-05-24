package com.vaxtrack.service;

import com.vaxtrack.database.dao.PatientDAO;
import com.vaxtrack.model.Patient;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Business logic layer for patient operations.
 * Validates input before hitting the DB.
 * Demonstrates: Service layer, Input validation, Separation of concerns
 */
public class PatientService {

    private final PatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
    }

    // -------------------------------------------------------
    // ADD PATIENT
    // -------------------------------------------------------
    public void addPatient(String fullName, LocalDate dob,
                           String gender, String phone,
                           String address) throws SQLException {

        validatePatientInput(fullName, dob, gender);

        Patient patient = new Patient();
        patient.setFullName(fullName.trim());
        patient.setDateOfBirth(dob);
        patient.setGender(gender);
        patient.setPhone(phone != null ? phone.trim() : "");
        patient.setAddress(address != null ? address.trim() : "");

        patientDAO.insert(patient);
        System.out.println("[PatientService] Added: " + patient.getDisplayInfo());
    }

    // -------------------------------------------------------
    // GET ALL PATIENTS
    // -------------------------------------------------------
    public List<Patient> getAllPatients() throws SQLException {
        return patientDAO.findAll();
    }

    // -------------------------------------------------------
    // GET PATIENT BY ID
    // -------------------------------------------------------
    public Patient getPatientById(int id) throws SQLException {
        Patient patient = patientDAO.findById(id);
        if (patient == null) {
            throw new IllegalArgumentException("No patient found with ID: " + id);
        }
        return patient;
    }

    // -------------------------------------------------------
    // SEARCH PATIENTS
    // -------------------------------------------------------
    public List<Patient> searchPatients(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) {
            return getAllPatients(); // Return all if empty search
        }
        return patientDAO.searchByNameOrPhone(keyword);
    }

    // -------------------------------------------------------
    // UPDATE PATIENT
    // -------------------------------------------------------
    public void updatePatient(int id, String fullName, LocalDate dob,
                              String gender, String phone,
                              String address) throws SQLException {

        validatePatientInput(fullName, dob, gender);

        // Confirm patient exists before updating
        Patient existing = patientDAO.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Cannot update — patient ID not found: " + id);
        }

        existing.setFullName(fullName.trim());
        existing.setDateOfBirth(dob);
        existing.setGender(gender);
        existing.setPhone(phone != null ? phone.trim() : "");
        existing.setAddress(address != null ? address.trim() : "");

        patientDAO.update(existing);
        System.out.println("[PatientService] Updated: " + existing.getDisplayInfo());
    }

    // -------------------------------------------------------
    // DELETE PATIENT
    // -------------------------------------------------------
    public void deletePatient(int id) throws SQLException {
        Patient existing = patientDAO.findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Cannot delete — patient ID not found: " + id);
        }
        patientDAO.delete(id);
        System.out.println("[PatientService] Deleted patient ID: " + id);
    }

    // -------------------------------------------------------
    // PRIVATE — Input Validation
    // -------------------------------------------------------
    private void validatePatientInput(String fullName, LocalDate dob, String gender) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (fullName.trim().length() < 2) {
            throw new IllegalArgumentException("Full name must be at least 2 characters.");
        }
        if (dob == null) {
            throw new IllegalArgumentException("Date of birth is required.");
        }
        if (dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future.");
        }
        if (gender == null || gender.isBlank()) {
            throw new IllegalArgumentException("Gender is required.");
        }
    }
}
