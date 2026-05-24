package com.vaxtrack.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Login screen — first screen the user sees.
 * Business logic connected in Phase 9.
 */
public class LoginScreen {

    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginScreen(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {

        // ── Root ──────────────────────────────────────────
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UIConstants.BG_LIGHT + ";");

        // ── Center Card ───────────────────────────────────
        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setStyle(UIConstants.CARD_STYLE + "-fx-padding: 40;");

        // ── Logo / Title ──────────────────────────────────
        Label logo = new Label("💉");
        logo.setFont(Font.font(48));

        Label title = new Label("VaxTrack");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: " + UIConstants.PRIMARY + ";");

        Label subtitle = new Label("Vaccine Tracking System");
        subtitle.setStyle("-fx-text-fill: " + UIConstants.TEXT_GRAY +
                "; -fx-font-size: 13px;");

        Separator sep = new Separator();
        sep.setPadding(new Insets(4, 0, 4, 0));

        // ── Username Field ────────────────────────────────
        Label userLabel = new Label("Username");
        userLabel.setStyle(UIConstants.LABEL_STYLE);
        userLabel.setMaxWidth(Double.MAX_VALUE);

        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle(UIConstants.INPUT_STYLE);
        usernameField.setMaxWidth(Double.MAX_VALUE);

        // ── Password Field ────────────────────────────────
        Label passLabel = new Label("Password");
        passLabel.setStyle(UIConstants.LABEL_STYLE);
        passLabel.setMaxWidth(Double.MAX_VALUE);

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle(UIConstants.INPUT_STYLE);
        passwordField.setMaxWidth(Double.MAX_VALUE);

        // ── Message Label (errors) ────────────────────────
        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: " + UIConstants.DANGER +
                "; -fx-font-size: 12px;");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setWrapText(true);

        // ── Login Button ──────────────────────────────────
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(UIConstants.PRIMARY_BTN);
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> handleLogin());

        // Allow Enter key to submit
        passwordField.setOnAction(e -> handleLogin());
        usernameField.setOnAction(e -> passwordField.requestFocus());

        // ── Footer ────────────────────────────────────────
        Label footer = new Label("VaxTrack v1.0 — OOP University Project");
        footer.setStyle("-fx-text-fill: " + UIConstants.TEXT_GRAY +
                "; -fx-font-size: 10px;");

        // ── Assemble Card ─────────────────────────────────
        card.getChildren().addAll(
                logo, title, subtitle, sep,
                userLabel, usernameField,
                passLabel, passwordField,
                messageLabel, loginBtn, footer
        );

        StackPane center = new StackPane(card);
        root.setCenter(center);

        return new Scene(root, 480, 560);
    }

    // ── Called from Phase 9 ───────────────────────────────
    public TextField getUsernameField()   { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Label getMessageLabel()        { return messageLabel; }
    public Stage getStage()               { return stage; }

    private void handleLogin() {
        // Placeholder — wired in Phase 9
        messageLabel.setStyle("-fx-text-fill: " + UIConstants.TEXT_GRAY +
                "; -fx-font-size: 12px;");
        messageLabel.setText("Connecting to backend in Phase 9...");
    }
}
