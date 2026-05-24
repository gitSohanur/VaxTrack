package com.vaxtrack.service;

import com.vaxtrack.database.dao.VaccineDAO;
import com.vaxtrack.database.dao.VaccineRecordDAO;
import com.vaxtrack.model.Vaccine;
import com.vaxtrack.model.VaccineRecord;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Business logic for vaccine record operations.
 * Validates doses, prevents duplicates, retrieves history.
 * Demonstrates: Service layer, Validation, Composition
 */
public class VaccineRecordService {

    private final VaccineRecordDAO recordDAO;
    private final VaccineDAO       vaccineDAO;

    public VaccineRecordService() {
        this.recordDAO  = new VaccineRecordDAO();
        this.vaccineDAO = new VaccineDAO();
    }

    // -------------------------------------------------------
    // ADD VACCINE RECORD
    // -------------------------------------------------------
    public void addRecord(int patientId, int vaccineId, int doseNumber,
                          LocalDate dateGiven, String administeredBy,
                          String notes) throws SQLException {

        // --- Validation ---
        if (dateGiven == null) {
            throw new IllegalArgumentException("Date given is required.");
        }
        if (dateGiven.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date given cannot be in the future.");
        }
        if (administeredBy == null || administeredBy.isBlank()) {
            throw new IllegalArgumentException("Administered by is required.");
        }
        if (doseNumber < 1) {
            throw new IllegalArgumentException("Dose number must be at least 1.");
        }

        // --- Check vaccine exists ---
        Vaccine vaccine = vaccineDAO.findById(vaccineId);
        if (vaccine == null) {
            throw new IllegalArgumentException("Vaccine not found with ID: " + vaccineId);
        }

        // --- Check dose number is valid ---
        if (doseNumber > vaccine.getDosesRequired()) {
            throw new IllegalArgumentException(
                    vaccine.getVaccineName() + " only requires " +
                            vaccine.getDosesRequired() + " dose(s). Cannot add dose " + doseNumber + "."
            );
        }

        // --- Prevent duplicate dose ---
        if (recordDAO.doseAlreadyRecorded(patientId, vaccineId, doseNumber)) {
            throw new IllegalArgumentException(
                    "Dose " + doseNumber + " of " + vaccine.getVaccineName() +
                            " is already recorded for this patient."
            );
        }

        // --- Build and insert record ---
        VaccineRecord record = new VaccineRecord();
        record.setPatientId(patientId);
        record.setVaccineId(vaccineId);
        record.setDoseNumber(doseNumber);
        record.setDateGiven(dateGiven);
        record.setAdministeredBy(administeredBy.trim());
        record.setNotes(notes != null ? notes.trim() : "");

        recordDAO.insert(record);
        System.out.println("[VaccineRecordService] Added: " + record.getDisplayInfo());
    }

    // -------------------------------------------------------
    // GET VACCINE HISTORY FOR A PATIENT
    // -------------------------------------------------------
    public List<VaccineRecord> getPatientHistory(int patientId) throws SQLException {
        List<VaccineRecord> history = recordDAO.findByPatientId(patientId);
        System.out.println("[VaccineRecordService] History for patient ID " +
                patientId + ": " + history.size() + " record(s)");
        return history;
    }

    // -------------------------------------------------------
    // GET ALL RECORDS
    // -------------------------------------------------------
    public List<VaccineRecord> getAllRecords() throws SQLException {
        return recordDAO.findAll();
    }

    // -------------------------------------------------------
    // GET ALL VACCINES (for dropdowns in UI)
    // -------------------------------------------------------
    public List<Vaccine> getAllVaccines() throws SQLException {
        return vaccineDAO.findAll();
    }

    // -------------------------------------------------------
    // DELETE A RECORD
    // -------------------------------------------------------
    public void deleteRecord(int recordId) throws SQLException {
        VaccineRecord existing = recordDAO.findById(recordId);
        if (existing == null) {
            throw new IllegalArgumentException("Record not found with ID: " + recordId);
        }
        recordDAO.delete(recordId);
        System.out.println("[VaccineRecordService] Deleted record ID: " + recordId);
    }
}
