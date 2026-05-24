package com.vaxtrack;

import com.vaxtrack.service.VaccineRecordService;
import java.time.LocalDate;


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

        VaccineRecordService recordService = new VaccineRecordService();

        try {
            // Assumes patient ID 1 and 2 exist from Phase 5 tests
            // Vaccines 1-5 were seeded in Phase 2

            // --- Test 1: Add vaccine records ---
            System.out.println("=== ADD RECORDS ===");
            recordService.addRecord(1, 1, 1, LocalDate.of(2024, 1, 10),
                    "Dr. Rahman", "First dose given");
            recordService.addRecord(1, 1, 2, LocalDate.of(2024, 2, 10),
                    "Dr. Rahman", "Second dose given");
            recordService.addRecord(1, 4, 1, LocalDate.of(2024, 3, 1),
                    "Dr. Karim", "Annual flu shot");
            recordService.addRecord(2, 3, 1, LocalDate.of(2024, 4, 5),
                    "Dr. Hasan", "Hep B first dose");

            // --- Test 2: View patient history ---
            System.out.println("\n=== HISTORY: Patient ID 1 ===");
            recordService.getPatientHistory(1)
                    .forEach(r -> System.out.println(r.getDisplayInfo()));

            // --- Test 3: View all records ---
            System.out.println("\n=== ALL RECORDS ===");
            recordService.getAllRecords()
                    .forEach(r -> System.out.println(r.getDisplayInfo()));

            // --- Test 4: Duplicate dose prevention ---
            System.out.println("\n=== DUPLICATE DOSE TEST ===");
            try {
                recordService.addRecord(1, 1, 1, LocalDate.now(), "Dr. X", "");
            } catch (IllegalArgumentException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            // --- Test 5: Exceeded dose limit ---
            System.out.println("\n=== EXCEEDED DOSE LIMIT TEST ===");
            try {
                recordService.addRecord(1, 4, 2, LocalDate.now(), "Dr. X", "");
                // Influenza only needs 1 dose
            } catch (IllegalArgumentException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            // --- Test 6: Delete a record ---
            System.out.println("\n=== DELETE RECORD ID 4 ===");
            recordService.deleteRecord(4);
            System.out.println("Remaining records: " + recordService.getAllRecords().size());

            // --- Test 7: List available vaccines ---
            System.out.println("\n=== ALL VACCINES ===");
            recordService.getAllVaccines()
                    .forEach(v -> System.out.println(v.getDisplayInfo()));

        } catch (SQLException e) {
            System.err.println("DB Error: " + e.getMessage());
        }

        primaryStage.setTitle("VaxTrack — Records Test");
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
