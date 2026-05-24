package com.vaxtrack.database.dao;

import com.vaxtrack.database.DatabaseConnection;
import com.vaxtrack.model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all DB operations for the Patient model.
 * Demonstrates: DAO pattern, JDBC, PreparedStatement
 */
public class PatientDAO implements CrudDAO<Patient> {

    // -------------------------------------------------------
    // CREATE
    // -------------------------------------------------------
    @Override
    public void insert(Patient patient) throws SQLException {
        String sql = """
            INSERT INTO patients (full_name, date_of_birth, gender, phone, address)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, patient.getFullName());
            stmt.setDate(2, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getAddress());
            stmt.executeUpdate();

            // Get the auto-generated ID and set it back on the object
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) patient.setId(keys.getInt(1));
            }
        }
    }

    // -------------------------------------------------------
    // READ — by ID
    // -------------------------------------------------------
    @Override
    public Patient findById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";

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
    // READ — all patients
    // -------------------------------------------------------
    @Override
    public List<Patient> findAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY full_name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                patients.add(mapRow(rs));
            }
        }
        return patients;
    }

    // -------------------------------------------------------
    // SEARCH — by name or phone (partial match)
    // -------------------------------------------------------
    public List<Patient> searchByNameOrPhone(String keyword) throws SQLException {
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
                    results.add(mapRow(rs));
                }
            }
        }
        return results;
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    @Override
    public void update(Patient patient) throws SQLException {
        String sql = """
            UPDATE patients
            SET full_name = ?, date_of_birth = ?, gender = ?, phone = ?, address = ?
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getFullName());
            stmt.setDate(2, Date.valueOf(patient.getDateOfBirth()));
            stmt.setString(3, patient.getGender());
            stmt.setString(4, patient.getPhone());
            stmt.setString(5, patient.getAddress());
            stmt.setInt(6, patient.getId());
            stmt.executeUpdate();
        }
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // -------------------------------------------------------
    // HELPER — map ResultSet row → Patient object
    // -------------------------------------------------------
    private Patient mapRow(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setId(rs.getInt("id"));
        patient.setFullName(rs.getString("full_name"));
        patient.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        patient.setGender(rs.getString("gender"));
        patient.setPhone(rs.getString("phone"));
        patient.setAddress(rs.getString("address"));
        return patient;
    }
}
