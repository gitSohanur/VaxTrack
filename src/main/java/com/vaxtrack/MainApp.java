package com.vaxtrack;

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

        PatientService patientService = new PatientService();

        try {
            // --- Test 1: Add patients ---
            System.out.println("=== ADD PATIENTS ===");
            patientService.addPatient("Rahim Uddin",
                    LocalDate.of(1990, 3, 15), "Male", "01711000001", "Dhaka");
            patientService.addPatient("Fatema Begum",
                    LocalDate.of(1985, 7, 22), "Female", "01722000002", "Chittagong");
            patientService.addPatient("Karim Molla",
                    LocalDate.of(2000, 11, 5), "Male", "01733000003", "Sylhet");

            // --- Test 2: Get all ---
            System.out.println("\n=== ALL PATIENTS ===");
            patientService.getAllPatients()
                    .forEach(p -> System.out.println(p.getDisplayInfo()));

            // --- Test 3: Search ---
            System.out.println("\n=== SEARCH: 'rahim' ===");
            patientService.searchPatients("rahim")
                    .forEach(p -> System.out.println(p.getDisplayInfo()));

            // --- Test 4: Update ---
            System.out.println("\n=== UPDATE patient ID 1 ===");
            patientService.updatePatient(1, "Rahim Uddin Khan",
                    LocalDate.of(1990, 3, 15), "Male", "01711999999", "Dhaka, Mirpur");
            System.out.println(patientService.getPatientById(1).getDisplayInfo());

            // --- Test 5: Delete ---
            System.out.println("\n=== DELETE patient ID 3 ===");
            patientService.deletePatient(3);
            System.out.println("Remaining count: " + patientService.getAllPatients().size());

            // --- Test 6: Validation error ---
            System.out.println("\n=== VALIDATION TEST ===");
            try {
                patientService.addPatient("", null, "", "", "");
            } catch (IllegalArgumentException e) {
                System.out.println("Caught validation error: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }

        primaryStage.setTitle("VaxTrack — Patient Test");
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
