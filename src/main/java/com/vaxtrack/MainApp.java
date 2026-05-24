package com.vaxtrack;

import com.vaxtrack.database.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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

        // --- OOP test — remove after verification ---
        Patient p = new Patient(1, "Rahim Uddin", LocalDate.of(1990, 5, 15), "Male", "01711000000", "Dhaka");
        Vaccine v = new Vaccine(1, "COVID-19 (Pfizer)", "Pfizer", 2, "mRNA vaccine");
        VaccineRecord r = new VaccineRecord(1, 1, 1, 1, LocalDate.now(), "Dr. Karim", "No side effects");
        r.setPatientName(p.getFullName());
        r.setVaccineName(v.getVaccineName());

        System.out.println(p.getDisplayInfo());
        System.out.println("Age: " + p.getAge());
        System.out.println(v.getDisplayInfo());
        System.out.println(r.getDisplayInfo());
        // --- end OOP test ---

        // ✅ Define a status message for the UI
        String status = "[DB] Connection test not yet implemented";

        Label label = new Label(status);
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 200);

        primaryStage.setTitle("VaxTrack — DB Test");
        primaryStage.setScene(scene);
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
