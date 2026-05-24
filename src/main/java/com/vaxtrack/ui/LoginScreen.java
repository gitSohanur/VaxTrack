package com.vaxtrack.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import com.vaxtrack.ui.UIConstants;

public class LoginScreen {

    private final Stage stage;

    // ── Fields declared at class level ────────────────────
    private TextField     usernameField;
    private PasswordField passwordField;
    private Label         messageLabel;
    private Button        loginBtn;       // ← class field, not local
    private Scene         scene;

    public LoginScreen(Stage stage) {
        this.stage = stage;
        buildScene();                     // ← build immediately in constructor
    }

    // ── Build once, reuse ─────────────────────────────────
    private void buildScene() {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UIConstants.BG_LIGHT + ";");

        VBox card = new VBox(20);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setStyle(UIConstants.CARD_STYLE + "-fx-padding: 40;");

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

        Label userLabel = new Label("Username");
        userLabel.setStyle(UIConstants.LABEL_STYLE);
        userLabel.setMaxWidth(Double.MAX_VALUE);

        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle(UIConstants.INPUT_STYLE);
        usernameField.setMaxWidth(Double.MAX_VALUE);

        Label passLabel = new Label("Password");
        passLabel.setStyle(UIConstants.LABEL_STYLE);
        passLabel.setMaxWidth(Double.MAX_VALUE);

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle(UIConstants.INPUT_STYLE);
        passwordField.setMaxWidth(Double.MAX_VALUE);

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-text-fill: " + UIConstants.DANGER +
                "; -fx-font-size: 12px;");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setWrapText(true);

        // ── loginBtn is now a field, initialized here ─────
        loginBtn = new Button("Login");
        loginBtn.setStyle(UIConstants.PRIMARY_BTN);
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        Label footer = new Label("VaxTrack v1.0 — OOP University Project");
        footer.setStyle("-fx-text-fill: " + UIConstants.TEXT_GRAY +
                "; -fx-font-size: 10px;");

        card.getChildren().addAll(
                logo, title, subtitle, sep,
                userLabel, usernameField,
                passLabel, passwordField,
                messageLabel, loginBtn, footer
        );

        StackPane center = new StackPane(card);
        root.setCenter(center);

        this.scene = new Scene(root, 480, 560);
    }

    // ── Always returns the same scene ─────────────────────
    public Scene getScene() {
        return scene;
    }

    // ── Getters ───────────────────────────────────────────
    public TextField      getUsernameField()  { return usernameField; }
    public PasswordField  getPasswordField()  { return passwordField; }
    public Label          getMessageLabel()   { return messageLabel; }
    public Button         getLoginButton()    { return loginBtn; }   // ← never null
    public Stage          getStage()          { return stage; }
}