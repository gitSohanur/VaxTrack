package com.vaxtrack.controller;

import com.vaxtrack.service.AuthService;
import com.vaxtrack.service.SearchService;
import com.vaxtrack.ui.DashboardScreen;
import com.vaxtrack.ui.LoginScreen;
import com.vaxtrack.utils.SessionManager;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DashboardController {

    private final DashboardScreen dashboardScreen;
    private final SearchService   searchService;
    private final AuthService     authService;
    private final Stage           stage;

    public DashboardController(Stage stage) {
        this.stage           = stage;
        this.dashboardScreen = new DashboardScreen(stage);
        this.searchService   = new SearchService();
        this.authService     = new AuthService();
    }

    public Scene getScene() {
        return dashboardScreen.getScene();
    }

    // ── This is the method LoginController calls ──────────
    public void init() {
        setWelcomeLabel();
        loadStats();
        wireButtons();
    }

    private void setWelcomeLabel() {
        String username = SessionManager.getInstance()
                .getCurrentUser()
                .getUsername();
        dashboardScreen.getWelcomeLabel()
                .setText("Welcome, " + username);
    }

    private void loadStats() {
        try {
            dashboardScreen.getPatientCountLabel()
                    .setText(String.valueOf(searchService.getTotalPatients()));
            dashboardScreen.getVaccinationCountLabel()
                    .setText(String.valueOf(searchService.getTotalVaccinations()));
            dashboardScreen.getVaccineCountLabel()
                    .setText(String.valueOf(searchService.getTotalVaccines()));
        } catch (SQLException e) {
            System.err.println("[Dashboard] Stats error: " + e.getMessage());
        }
    }

    private void wireButtons() {

        // → Patients screen
        dashboardScreen.getPatientsBtn().setOnAction(e -> {
            PatientController ctrl = new PatientController(stage, this);
            stage.setTitle("VaxTrack — Patients");
            stage.setScene(ctrl.getScene());
            ctrl.init();
        });

        // → Vaccine records screen
        dashboardScreen.getRecordsBtn().setOnAction(e -> {
            VaccineRecordController ctrl = new VaccineRecordController(stage, this);
            stage.setTitle("VaxTrack — Vaccine Records");
            stage.setScene(ctrl.getScene());
            ctrl.init();
        });

        // → Search screen
        dashboardScreen.getSearchBtn().setOnAction(e -> {
            SearchController ctrl = new SearchController(stage, this);
            stage.setTitle("VaxTrack — Search");
            stage.setScene(ctrl.getScene());
            ctrl.init();
        });

        // → Logout
        dashboardScreen.getLogoutBtn().setOnAction(e -> {
            authService.logout();
            LoginController loginCtrl = new LoginController(stage);
            stage.setTitle("VaxTrack — Login");
            stage.setScene(loginCtrl.getScene());
            stage.setResizable(false);
            stage.centerOnScreen();
        });
    }

    // Called by child controllers when navigating back
    public void returnToDashboard() {
        stage.setTitle("VaxTrack — Dashboard");
        stage.setScene(dashboardScreen.getScene());
        loadStats();
    }
}