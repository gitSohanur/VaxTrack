package com.vaxtrack;

import com.vaxtrack.database.DatabaseConnection;
import com.vaxtrack.ui.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginScreen loginScreen = new LoginScreen(primaryStage);
        primaryStage.setTitle("VaxTrack — Login");
        primaryStage.setScene(loginScreen.getScene());
        primaryStage.setResizable(false);
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