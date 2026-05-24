package com.vaxtrack;

import com.vaxtrack.database.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


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

        AuthService authService = new AuthService();

        try {
            // Test 1: Wrong credentials
            boolean fail = authService.login("admin", "wrongpassword");
            System.out.println("Wrong password result: " + fail); // false

            // Test 2: Correct credentials
            boolean success = authService.login("admin", "admin123");
            System.out.println("Correct login result: " + success); // true

            // Test 3: Session check
            if (authService.isLoggedIn()) {
                User u = SessionManager.getInstance().getCurrentUser();
                System.out.println("Session user: " + u.getDisplayInfo());
                System.out.println("Is admin: " + SessionManager.getInstance().isAdmin());
            }

            // Test 4: Logout
            authService.logout();
            System.out.println("Logged in after logout: " + authService.isLoggedIn()); // false

        } catch (SQLException e) {
            System.err.println("Auth test error: " + e.getMessage());
        }

        // Minimal window so app doesn't crash
        primaryStage.setTitle("VaxTrack — Auth Test");
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
