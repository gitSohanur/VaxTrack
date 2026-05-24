package com.vaxtrack;

import com.vaxtrack.service.VaccineRecordService;
import java.time.LocalDate;

import com.vaxtrack.service.SearchService;
import com.vaxtrack.model.SearchResult;
import java.util.List;

import com.vaxtrack.database.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.vaxtrack.service.PatientService;
import java.time.LocalDate;


import com.vaxtrack.service.AuthService;
import com.vaxtrack.utils.SessionManager;
import com.vaxtrack.model.User;
import java.sql.SQLException;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;   // ✅ Import LocalDate

// ✅ Import your model classes
import com.vaxtrack.model.Patient;
import com.vaxtrack.model.Vaccine;
import com.vaxtrack.model.VaccineRecord;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        SearchService searchService = new SearchService();

        try {
            // --- Test 1: Search patients by name ---
            System.out.println("=== SEARCH PATIENTS: 'rahim' ===");
            searchService.searchPatients("rahim")
                    .forEach(p -> System.out.println(p.getDisplayInfo()));

            // --- Test 2: Search patients by phone ---
            System.out.println("\n=== SEARCH PATIENTS BY PHONE: '017' ===");
            searchService.searchPatients("017")
                    .forEach(p -> System.out.println(p.getDisplayInfo()));

            // --- Test 3: Search records by vaccine name ---
            System.out.println("\n=== SEARCH RECORDS: 'covid' ===");
            searchService.searchRecords("covid")
                    .forEach(r -> System.out.println(r.getDisplayInfo()));

            // --- Test 4: Unified search ---
            System.out.println("\n=== UNIFIED SEARCH: 'pfizer' ===");
            searchService.search("pfizer")
                    .forEach(r -> System.out.println(r));

            // --- Test 5: Patient vaccine history ---
            System.out.println("\n=== PATIENT HISTORY: ID 1 ===");
            searchService.getPatientHistory(1)
                    .forEach(r -> System.out.println(r.getDisplayInfo()));

            // --- Test 6: Dashboard stats ---
            System.out.println("\n=== DASHBOARD STATS ===");
            System.out.println("Total Patients    : " + searchService.getTotalPatients());
            System.out.println("Total Vaccinations: " + searchService.getTotalVaccinations());
            System.out.println("Total Vaccines    : " + searchService.getTotalVaccines());

            // --- Test 7: Short keyword validation ---
            System.out.println("\n=== SHORT KEYWORD TEST ===");
            try {
                searchService.search("a");
            } catch (IllegalArgumentException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            // --- Test 8: Blank search returns empty ---
            System.out.println("\n=== BLANK SEARCH TEST ===");
            List<SearchResult> empty = searchService.search("  ");
            System.out.println("Results for blank: " + empty.size()); // 0

        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }

        primaryStage.setTitle("VaxTrack — Search Test");
        primaryStage.setScene(new Scene(new StackPane(new Label("Check console")), 300, 150));
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
