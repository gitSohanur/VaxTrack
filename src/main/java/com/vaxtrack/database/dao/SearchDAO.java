package com.vaxtrack.database.dao;

import com.vaxtrack.database.DatabaseConnection;
import com.vaxtrack.model.Patient;
import com.vaxtrack.model.SearchResult;
import com.vaxtrack.model.VaccineRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dedicated DAO for cross-table search queries.
 * Does NOT implement CrudDAO — it is read-only by design.
 * Demonstrates: Single Responsibility, SQL JOIN, LIKE queries
 */
public class SearchDAO {

    // -------------------------------------------------------
    // SEARCH PATIENTS — by name or phone
    // -------------------------------------------------------
    public List<Patient> searchPatients(String keyword) throws SQLException {
        List<Patient> results = new ArrayList<>();
        String sql = """
                SELECT * FROM patients
                WHERE full_name LIKE ? OR phone LIKE ?
                ORDER BY full_name ASC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword.trim() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Patient p = new Patient();
                    p.setId(rs.getInt("id"));
                    p.setFullName(rs.getString("full_name"));
                    p.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                    p.setGender(rs.getString("gender"));
                    p.setPhone(rs.getString("phone"));
                    p.setAddress(rs.getString("address"));
                    results.add(p);
                }
            }
        }
        return results;
    }

    // -------------------------------------------------------
    // SEARCH VACCINE RECORDS — by patient name or vaccine name
    // -------------------------------------------------------
    public List<VaccineRecord> searchRecords(String keyword) throws SQLException {
        List<VaccineRecord> results = new ArrayList<>();
        String sql = """
                SELECT vr.*,
                       p.full_name    AS patient_name,
                       v.vaccine_name AS vaccine_name
                FROM vaccine_records vr
                JOIN patients p ON vr.patient_id = p.id
                JOIN vaccines  v ON vr.vaccine_id  = v.id
                WHERE p.full_name   LIKE ?
                   OR v.vaccine_name LIKE ?
                ORDER BY vr.date_given DESC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword.trim() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VaccineRecord r = new VaccineRecord();
                    r.setId(rs.getInt("id"));
                    r.setPatientId(rs.getInt("patient_id"));
                    r.setVaccineId(rs.getInt("vaccine_id"));
                    r.setDoseNumber(rs.getInt("dose_number"));
                    r.setDateGiven(rs.getDate("date_given").toLocalDate());
                    r.setAdministeredBy(rs.getString("administered_by"));
                    r.setNotes(rs.getString("notes"));
                    r.setPatientName(rs.getString("patient_name"));
                    r.setVaccineName(rs.getString("vaccine_name"));
                    results.add(r);
                }
            }
        }
        return results;
    }

    // -------------------------------------------------------
    // UNIFIED SEARCH — returns SearchResult list for UI display
    // -------------------------------------------------------
    public List<SearchResult> unifiedSearch(String keyword) throws SQLException {
        List<SearchResult> results = new ArrayList<>();

        // --- Patient results ---
        for (Patient p : searchPatients(keyword)) {
            results.add(new SearchResult(
                    p.getId(),
                    SearchResult.ResultType.PATIENT,
                    p.getFullName(),
                    "Phone: " + p.getPhone() + " | Age: " + p.getAge(),
                    p.getGender(),
                    p.getDateOfBirth().toString()
            ));
        }

        // --- Vaccine record results ---
        for (VaccineRecord r : searchRecords(keyword)) {
            results.add(new SearchResult(
                    r.getId(),
                    SearchResult.ResultType.VACCINE_RECORD,
                    r.getPatientName(),
                    "Vaccine: " + r.getVaccineName() + " | Dose: " + r.getDoseNumber(),
                    "By: " + r.getAdministeredBy(),
                    r.getDateGiven().toString()
            ));
        }

        return results;
    }

    // -------------------------------------------------------
    // DASHBOARD STATS — counts for the admin home screen
    // -------------------------------------------------------
    public int getTotalPatients() throws SQLException {
        return getCount("SELECT COUNT(*) FROM patients");
    }

    public int getTotalVaccinations() throws SQLException {
        return getCount("SELECT COUNT(*) FROM vaccine_records");
    }

    public int getTotalVaccines() throws SQLException {
        return getCount("SELECT COUNT(*) FROM vaccines");
    }

    // -------------------------------------------------------
    // HELPER — run a COUNT query
    // -------------------------------------------------------
    private int getCount(String sql) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}
