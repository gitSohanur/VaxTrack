package com.vaxtrack.database.dao;

import com.vaxtrack.database.DatabaseConnection;
import com.vaxtrack.model.VaccineRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all DB operations for VaccineRecord.
 * Uses JOIN to fetch patient + vaccine names for display.
 * Demonstrates: DAO pattern, SQL JOIN, JDBC
 */
public class VaccineRecordDAO implements CrudDAO<VaccineRecord> {

    // -------------------------------------------------------
    // Base SELECT with JOIN — reused across queries
    // -------------------------------------------------------
    private static final String SELECT_WITH_JOIN = """
            SELECT vr.*,
                   p.full_name  AS patient_name,
                   v.vaccine_name AS vaccine_name
            FROM vaccine_records vr
            JOIN patients p ON vr.patient_id = p.id
            JOIN vaccines  v ON vr.vaccine_id  = v.id
            """;

    // -------------------------------------------------------
    // CREATE
    // -------------------------------------------------------
    @Override
    public void insert(VaccineRecord record) throws SQLException {
        String sql = """
            INSERT INTO vaccine_records
                (patient_id, vaccine_id, dose_number, date_given, administered_by, notes)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getVaccineId());
            stmt.setInt(3, record.getDoseNumber());
            stmt.setDate(4, Date.valueOf(record.getDateGiven()));
            stmt.setString(5, record.getAdministeredBy());
            stmt.setString(6, record.getNotes());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) record.setId(keys.getInt(1));
            }
        }
    }

    // -------------------------------------------------------
    // READ — by record ID (with JOIN)
    // -------------------------------------------------------
    @Override
    public VaccineRecord findById(int id) throws SQLException {
        String sql = SELECT_WITH_JOIN + " WHERE vr.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    // -------------------------------------------------------
    // READ — all records (with JOIN)
    // -------------------------------------------------------
    @Override
    public List<VaccineRecord> findAll() throws SQLException {
        List<VaccineRecord> records = new ArrayList<>();
        String sql = SELECT_WITH_JOIN + " ORDER BY vr.date_given DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                records.add(mapRow(rs));
            }
        }
        return records;
    }

    // -------------------------------------------------------
    // READ — all records for ONE patient (vaccine history)
    // -------------------------------------------------------
    public List<VaccineRecord> findByPatientId(int patientId) throws SQLException {
        List<VaccineRecord> records = new ArrayList<>();
        String sql = SELECT_WITH_JOIN +
                " WHERE vr.patient_id = ? ORDER BY vr.date_given DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapRow(rs));
                }
            }
        }
        return records;
    }

    // -------------------------------------------------------
    // READ — check if dose already recorded (prevent duplicates)
    // -------------------------------------------------------
    public boolean doseAlreadyRecorded(int patientId, int vaccineId,
                                       int doseNumber) throws SQLException {
        String sql = """
            SELECT COUNT(*) FROM vaccine_records
            WHERE patient_id = ? AND vaccine_id = ? AND dose_number = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            stmt.setInt(2, vaccineId);
            stmt.setInt(3, doseNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    @Override
    public void update(VaccineRecord record) throws SQLException {
        String sql = """
            UPDATE vaccine_records
            SET patient_id = ?, vaccine_id = ?, dose_number = ?,
                date_given = ?, administered_by = ?, notes = ?
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getVaccineId());
            stmt.setInt(3, record.getDoseNumber());
            stmt.setDate(4, Date.valueOf(record.getDateGiven()));
            stmt.setString(5, record.getAdministeredBy());
            stmt.setString(6, record.getNotes());
            stmt.setInt(7, record.getId());
            stmt.executeUpdate();
        }
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM vaccine_records WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // -------------------------------------------------------
    // HELPER — map ResultSet row → VaccineRecord object
    // -------------------------------------------------------
    private VaccineRecord mapRow(ResultSet rs) throws SQLException {
        VaccineRecord record = new VaccineRecord();
        record.setId(rs.getInt("id"));
        record.setPatientId(rs.getInt("patient_id"));
        record.setVaccineId(rs.getInt("vaccine_id"));
        record.setDoseNumber(rs.getInt("dose_number"));
        record.setDateGiven(rs.getDate("date_given").toLocalDate());
        record.setAdministeredBy(rs.getString("administered_by"));
        record.setNotes(rs.getString("notes"));

        // Joined fields for display
        record.setPatientName(rs.getString("patient_name"));
        record.setVaccineName(rs.getString("vaccine_name"));
        return record;
    }
}
