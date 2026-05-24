package com.vaxtrack.service;

import com.vaxtrack.database.dao.SearchDAO;
import com.vaxtrack.database.dao.VaccineRecordDAO;
import com.vaxtrack.model.Patient;
import com.vaxtrack.model.SearchResult;
import com.vaxtrack.model.VaccineRecord;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Unified search service used by the UI search bar.
 * Also provides dashboard statistics.
 * Demonstrates: Service layer, Facade pattern
 */
public class SearchService {

    private final SearchDAO        searchDAO;
    private final VaccineRecordDAO recordDAO;

    public SearchService() {
        this.searchDAO = new SearchDAO();
        this.recordDAO = new VaccineRecordDAO();
    }

    // -------------------------------------------------------
    // SEARCH PATIENTS ONLY
    // -------------------------------------------------------
    public List<Patient> searchPatients(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();
        return searchDAO.searchPatients(keyword);
    }

    // -------------------------------------------------------
    // SEARCH VACCINE RECORDS ONLY
    // -------------------------------------------------------
    public List<VaccineRecord> searchRecords(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();
        return searchDAO.searchRecords(keyword);
    }

    // -------------------------------------------------------
    // UNIFIED SEARCH — both patients + records together
    // -------------------------------------------------------
    public List<SearchResult> search(String keyword) throws SQLException {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();
        if (keyword.trim().length() < 2) {
            throw new IllegalArgumentException("Search keyword must be at least 2 characters.");
        }
        return searchDAO.unifiedSearch(keyword.trim());
    }

    // -------------------------------------------------------
    // GET VACCINE HISTORY FOR A PATIENT
    // -------------------------------------------------------
    public List<VaccineRecord> getPatientHistory(int patientId) throws SQLException {
        return recordDAO.findByPatientId(patientId);
    }

    // -------------------------------------------------------
    // DASHBOARD STATS
    // -------------------------------------------------------
    public int getTotalPatients() throws SQLException {
        return searchDAO.getTotalPatients();
    }

    public int getTotalVaccinations() throws SQLException {
        return searchDAO.getTotalVaccinations();
    }

    public int getTotalVaccines() throws SQLException {
        return searchDAO.getTotalVaccines();
    }
}
