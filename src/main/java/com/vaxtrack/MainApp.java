package com.vaxtrack;

import com.vaxtrack.controller.LoginController;
import com.vaxtrack.database.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginController loginController = new LoginController(primaryStage);
        primaryStage.setTitle("VaxTrack — Login");
        primaryStage.setScene(loginController.getScene());
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
        System.out.println("[App] Shutdown complete.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}