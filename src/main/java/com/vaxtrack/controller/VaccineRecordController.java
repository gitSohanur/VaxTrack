package com.vaxtrack.controller;

import com.vaxtrack.model.Patient;
import com.vaxtrack.model.Vaccine;
import com.vaxtrack.model.VaccineRecord;
import com.vaxtrack.service.PatientService;
import com.vaxtrack.service.VaccineRecordService;
import com.vaxtrack.ui.VaccineRecordScreen;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Wires VaccineRecordScreen to VaccineRecordService + PatientService.
 */
public class VaccineRecordController {

    private final VaccineRecordScreen    recordScreen;
    private final VaccineRecordService   recordService;
    private final PatientService         patientService;
    private final DashboardController    dashboardCtrl;
    private final Stage                  stage;

    private VaccineRecord selectedRecord = null;

    public VaccineRecordController(Stage stage, DashboardController dashboardCtrl) {
        this.stage          = stage;
        this.dashboardCtrl  = dashboardCtrl;
        this.recordScreen   = new VaccineRecordScreen(stage);
        this.recordService  = new VaccineRecordService();
        this.patientService = new PatientService();
    }

    public Scene getScene() {
        return recordScreen.getScene();
    }

    public void init() {
        loadDropdowns();
        loadAllRecords();
        wireButtons();
        trackSelection();
    }

    // ── Load patients + vaccines into combo boxes ──────────
    private void loadDropdowns() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            recordScreen.getPatientCombo()
                    .getItems()
                    .setAll(patients);

            List<Vaccine> vaccines = recordService.getAllVaccines();
            recordScreen.getVaccineCombo()
                    .getItems()
                    .setAll(vaccines);

        } catch (SQLException e) {
            recordScreen.setStatus("Failed to load dropdowns: " + e.getMessage(), true);
        }
    }

    // ── Load all records into table ───────────────────────
    private void loadAllRecords() {
        try {
            List<VaccineRecord> records = recordService.getAllRecords();
            recordScreen.getTableData().setAll(records);
            recordScreen.setStatus("Loaded " + records.size() + " record(s).", false);
        } catch (SQLException e) {
            recordScreen.setStatus("Failed to load records: " + e.getMessage(), true);
        }
    }

    // ── Track table row selection ─────────────────────────
    private void trackSelection() {
        recordScreen.getTable()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    selectedRecord = selected;
                });
    }

    // ── Wire buttons ──────────────────────────────────────
    private void wireButtons() {

        // ── Add Record ────────────────────────────────────
        recordScreen.getAddBtn().setOnAction(e -> handleAdd());

        // ── Delete Record ─────────────────────────────────
        recordScreen.getDeleteBtn().setOnAction(e -> handleDelete());

        // ── Clear Form ────────────────────────────────────
        recordScreen.getClearBtn().setOnAction(e -> {
            recordScreen.clearForm();
            selectedRecord = null;
            recordScreen.setStatus("Form cleared.", false);
        });

        // ── Refresh Table ─────────────────────────────────
        recordScreen.getRefreshBtn().setOnAction(e -> {
            loadDropdowns();
            loadAllRecords();
        });

        // ── Back to Dashboard ─────────────────────────────
        recordScreen.getBackBtn().setOnAction(e ->
                dashboardCtrl.returnToDashboard()
        );
    }

    // ── ADD RECORD ────────────────────────────────────────
    private void handleAdd() {
        Patient patient   = recordScreen.getPatientCombo().getValue();
        Vaccine vaccine   = recordScreen.getVaccineCombo().getValue();
        int     dose      = recordScreen.getDoseSpinner().getValue();
        LocalDate date    = recordScreen.getDateGivenPicker().getValue();
        String  adminBy   = recordScreen.getAdministeredByField().getText();
        String  notes     = recordScreen.getNotesField().getText();

        if (patient == null) {
            recordScreen.setStatus("⚠ Please select a patient.", true);
            return;
        }
        if (vaccine == null) {
            recordScreen.setStatus("⚠ Please select a vaccine.", true);
            return;
        }
        if (date == null) {
            recordScreen.setStatus("⚠ Please select the date given.", true);
            return;
        }

        try {
            recordService.addRecord(
                    patient.getId(), vaccine.getId(),
                    dose, date, adminBy, notes);

            loadAllRecords();
            recordScreen.clearForm();
            recordScreen.setStatus(
                    "✔ Vaccine record added for " + patient.getFullName() + ".", false);

        } catch (IllegalArgumentException e) {
            recordScreen.setStatus("⚠ " + e.getMessage(), true);
        } catch (SQLException e) {
            recordScreen.setStatus("DB Error: " + e.getMessage(), true);
        }
    }

    // ── DELETE RECORD ─────────────────────────────────────
    private void handleDelete() {
        if (selectedRecord == null) {
            recordScreen.setStatus("⚠ Please select a record to delete.", true);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Vaccine Record");
        alert.setContentText("Delete record for \""
                + selectedRecord.getPatientName()
                + "\" — " + selectedRecord.getVaccineName()
                + " Dose " + selectedRecord.getDoseNumber() + "?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                recordService.deleteRecord(selectedRecord.getId());
                loadAllRecords();
                recordScreen.clearForm();
                selectedRecord = null;
                recordScreen.setStatus("✔ Record deleted.", false);

            } catch (Exception e) {
                recordScreen.setStatus("Error: " + e.getMessage(), true);
            }
        }
    }
}