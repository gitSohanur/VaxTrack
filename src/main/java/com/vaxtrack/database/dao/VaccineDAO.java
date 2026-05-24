package com.vaxtrack.database.dao;

import com.vaxtrack.database.DatabaseConnection;
import com.vaxtrack.model.Vaccine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all DB operations for the Vaccine model.
 * Vaccines are mostly pre-seeded but can be managed by admin.
 * Demonstrates: DAO pattern, JDBC
 */
public class VaccineDAO implements CrudDAO<Vaccine> {

    // -------------------------------------------------------
    // CREATE
    // -------------------------------------------------------
    @Override
    public void insert(Vaccine vaccine) throws SQLException {
        String sql = """
            INSERT INTO vaccines (vaccine_name, manufacturer, doses_required, description)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vaccine.getVaccineName());
            stmt.setString(2, vaccine.getManufacturer());
            stmt.setInt(3, vaccine.getDosesRequired());
            stmt.setString(4, vaccine.getDescription());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) vaccine.setId(keys.getInt(1));
            }
        }
    }

    // -------------------------------------------------------
    // READ — by ID
    // -------------------------------------------------------
    @Override
    public Vaccine findById(int id) throws SQLException {
        String sql = "SELECT * FROM vaccines WHERE id = ?";

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
    // READ — all vaccines (used to populate dropdowns)
    // -------------------------------------------------------
    @Override
    public List<Vaccine> findAll() throws SQLException {
        List<Vaccine> vaccines = new ArrayList<>();
        String sql = "SELECT * FROM vaccines ORDER BY vaccine_name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vaccines.add(mapRow(rs));
            }
        }
        return vaccines;
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    @Override
    public void update(Vaccine vaccine) throws SQLException {
        String sql = """
            UPDATE vaccines
            SET vaccine_name = ?, manufacturer = ?, doses_required = ?, description = ?
            WHERE id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vaccine.getVaccineName());
            stmt.setString(2, vaccine.getManufacturer());
            stmt.setInt(3, vaccine.getDosesRequired());
            stmt.setString(4, vaccine.getDescription());
            stmt.setInt(5, vaccine.getId());
            stmt.executeUpdate();
        }
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM vaccines WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // -------------------------------------------------------
    // HELPER — map ResultSet row → Vaccine object
    // -------------------------------------------------------
    private Vaccine mapRow(ResultSet rs) throws SQLException {
        Vaccine vaccine = new Vaccine();
        vaccine.setId(rs.getInt("id"));
        vaccine.setVaccineName(rs.getString("vaccine_name"));
        vaccine.setManufacturer(rs.getString("manufacturer"));
        vaccine.setDosesRequired(rs.getInt("doses_required"));
        vaccine.setDescription(rs.getString("description"));
        return vaccine;
    }
}
