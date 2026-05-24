package com.vaxtrack.controller;

import com.vaxtrack.model.Patient;
import com.vaxtrack.service.PatientService;
import com.vaxtrack.ui.PatientScreen;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Wires PatientScreen to PatientService.
 * Handles all CRUD operations and search.
 */
public class PatientController {

    private final PatientScreen       patientScreen;
    private final PatientService      patientService;
    private final DashboardController dashboardCtrl;
    private final Stage               stage;

    // Tracks which patient row is selected for update/delete
    private Patient selectedPatient = null;

    public PatientController(Stage stage, DashboardController dashboardCtrl) {
        this.stage            = stage;
        this.dashboardCtrl    = dashboardCtrl;
        this.patientScreen    = new PatientScreen(stage);
        this.patientService   = new PatientService();
    }

    public Scene getScene() {
        return patientScreen.getScene();
    }

    public void init() {
        loadAllPatients();
        wireButtons();
        trackSelection();
    }

    // ── Load all patients into table ──────────────────────
    private void loadAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            patientScreen.getTableData().setAll(patients);
            patientScreen.setStatus("Loaded " + patients.size() + " patient(s).", false);
        } catch (SQLException e) {
            patientScreen.setStatus("Failed to load patients: " + e.getMessage(), true);
        }
    }

    // ── Track table row selection ─────────────────────────
    private void trackSelection() {
        patientScreen.getTable()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    selectedPatient = selected;
                });
    }

    // ── Wire all buttons ──────────────────────────────────
    private void wireButtons() {

        // ── Add Patient ───────────────────────────────────
        patientScreen.getAddBtn().setOnAction(e -> handleAdd());

        // ── Update Patient ────────────────────────────────
        patientScreen.getUpdateBtn().setOnAction(e -> handleUpdate());

        // ── Delete Patient ────────────────────────────────
        patientScreen.getDeleteBtn().setOnAction(e -> handleDelete());

        // ── Clear Form ────────────────────────────────────
        patientScreen.getClearBtn().setOnAction(e -> {
            patientScreen.clearForm();
            selectedPatient = null;
            patientScreen.setStatus("Form cleared.", false);
        });

        // ── Search ────────────────────────────────────────
        patientScreen.getSearchBtn().setOnAction(e -> handleSearch());
        patientScreen.getSearchField().setOnAction(e -> handleSearch());

        // ── Back to Dashboard ─────────────────────────────
        patientScreen.getBackBtn().setOnAction(e ->
                dashboardCtrl.returnToDashboard()
        );
    }

    // ── ADD ───────────────────────────────────────────────
    private void handleAdd() {
        try {
            String    name    = patientScreen.getFullNameField().getText();
            LocalDate dob     = patientScreen.getDobPicker().getValue();
            String    gender  = patientScreen.getGenderCombo().getValue();
            String    phone   = patientScreen.getPhoneField().getText();
            String    address = patientScreen.getAddressField().getText();

            patientService.addPatient(name, dob, gender, phone, address);

            loadAllPatients();
            patientScreen.clearForm();
            patientScreen.setStatus("✔ Patient added successfully.", false);

        } catch (IllegalArgumentException e) {
            patientScreen.setStatus("⚠ " + e.getMessage(), true);
        } catch (SQLException e) {
            patientScreen.setStatus("DB Error: " + e.getMessage(), true);
        }
    }

    // ── UPDATE ────────────────────────────────────────────
    private void handleUpdate() {
        if (selectedPatient == null) {
            patientScreen.setStatus("⚠ Please select a patient to update.", true);
            return;
        }

        try {
            String    name    = patientScreen.getFullNameField().getText();
            LocalDate dob     = patientScreen.getDobPicker().getValue();
            String    gender  = patientScreen.getGenderCombo().getValue();
            String    phone   = patientScreen.getPhoneField().getText();
            String    address = patientScreen.getAddressField().getText();

            patientService.updatePatient(
                    selectedPatient.getId(), name, dob, gender, phone, address);

            loadAllPatients();
            patientScreen.clearForm();
            selectedPatient = null;
            patientScreen.setStatus("✔ Patient updated successfully.", false);

        } catch (IllegalArgumentException e) {
            patientScreen.setStatus("⚠ " + e.getMessage(), true);
        } catch (SQLException e) {
            patientScreen.setStatus("DB Error: " + e.getMessage(), true);
        }
    }

    // ── DELETE ────────────────────────────────────────────
    private void handleDelete() {
        if (selectedPatient == null) {
            patientScreen.setStatus("⚠ Please select a patient to delete.", true);
            return;
        }

        // Confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Patient");
        alert.setContentText("Are you sure you want to delete \""
                + selectedPatient.getFullName() + "\"?\n"
                + "All their vaccine records will also be deleted.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                patientService.deletePatient(selectedPatient.getId());
                loadAllPatients();
                patientScreen.clearForm();
                selectedPatient = null;
                patientScreen.setStatus("✔ Patient deleted.", false);

            } catch (SQLException e) {
                patientScreen.setStatus("DB Error: " + e.getMessage(), true);
            }
        }
    }

    // ── SEARCH ────────────────────────────────────────────
    private void handleSearch() {
        String keyword = patientScreen.getSearchField().getText();

        try {
            List<Patient> results = patientService.searchPatients(keyword);
            patientScreen.getTableData().setAll(results);

            if (results.isEmpty()) {
                patientScreen.setStatus("No patients found for: \"" + keyword + "\"", true);
            } else {
                patientScreen.setStatus(
                        "Found " + results.size() + " result(s) for: \"" + keyword + "\"", false);
            }

        } catch (SQLException e) {
            patientScreen.setStatus("DB Error: " + e.getMessage(), true);
        }
    }
}