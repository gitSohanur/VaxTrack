package com.vaxtrack.controller;

import com.vaxtrack.service.AuthService;
import com.vaxtrack.ui.LoginScreen;
import com.vaxtrack.ui.UIConstants;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class LoginController {

    private final LoginScreen loginScreen;
    private final AuthService authService;
    private final Stage       stage;

    public LoginController(Stage stage) {
        this.stage       = stage;
        this.authService = new AuthService();
        this.loginScreen = new LoginScreen(stage);
        wireHandlers();
    }

    public Scene getScene() {
        return loginScreen.getScene();
    }

    private void wireHandlers() {
        loginScreen.getLoginButton()
                .setOnAction(e -> attemptLogin());
        loginScreen.getPasswordField()
                .setOnAction(e -> attemptLogin());
        loginScreen.getUsernameField()
                .setOnAction(e -> loginScreen.getPasswordField().requestFocus());
    }

    private void attemptLogin() {
        String username = loginScreen.getUsernameField().getText().trim();
        String password = loginScreen.getPasswordField().getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter username and password.", true);
            return;
        }

        try {
            boolean success = authService.login(username, password);

            if (success) {
                showMessage("Login successful! Loading...", false);
                Platform.runLater(() -> {
                    DashboardController dashCtrl = new DashboardController(stage);
                    stage.setTitle("VaxTrack — Dashboard");
                    stage.setScene(dashCtrl.getScene());
                    stage.setResizable(true);
                    stage.centerOnScreen();
                    dashCtrl.init();
                });
            } else {
                showMessage("Invalid username or password.", true);
                loginScreen.getPasswordField().clear();
                loginScreen.getPasswordField().requestFocus();
            }

        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        loginScreen.getMessageLabel().setText(msg);
        loginScreen.getMessageLabel().setStyle(
                "-fx-font-size: 12px; -fx-text-fill: " +
                        (isError ? UIConstants.DANGER : UIConstants.SUCCESS) + ";"
        );
    }
}