package com.vaxtrack;

import com.vaxtrack.database.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        // --- DB Connection Test ---
        String status;
        try {
            Connection conn = DatabaseConnection.getConnection();
            status = (conn != null) ? "✅ DB Connected!" : "❌ Connection null";
        } catch (SQLException e) {
            status = "❌ DB Error: " + e.getMessage();
        }

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
