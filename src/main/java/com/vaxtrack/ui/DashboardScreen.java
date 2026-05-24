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

public class DashboardScreen {

    private final Stage stage;

    private Label  patientCountLabel;
    private Label  vaccinationCountLabel;
    private Label  vaccineCountLabel;
    private Label  welcomeLabel;
    private Button patientsBtn;
    private Button recordsBtn;
    private Button searchBtn;
    private Button logoutBtn;

    private final Scene scene;   // built once, reused

    // ── Constructor: build scene immediately ──────────────
    public DashboardScreen(Stage stage) {
        this.stage = stage;
        this.scene = createScene();   // ← private builder
    }

    // ── Public getter — never null ────────────────────────
    public Scene getScene() {
        return scene;
    }

    // ── Private scene builder ─────────────────────────────
    private Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UIConstants.BG_LIGHT + ";");
        root.setTop(buildNavBar());

        ScrollPane scrollPane = new ScrollPane(buildContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );
        root.setCenter(scrollPane);

        return new Scene(root, 900, 620);
    }

    // ── Nav Bar ───────────────────────────────────────────
    private HBox buildNavBar() {
        HBox nav = new HBox(16);
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setPadding(new Insets(14, 24, 14, 24));
        nav.setStyle("-fx-background-color: " + UIConstants.PRIMARY + ";");

        Label brand = new Label("💉 VaxTrack");
        brand.setFont(Font.font("System", FontWeight.BOLD, 18));
        brand.setStyle("-fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        welcomeLabel = new Label("Welcome, Admin");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px;");

        logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 6 14 6 14;"
        );

        nav.getChildren().addAll(brand, spacer, welcomeLabel, logoutBtn);
        return nav;
    }

    // ── Main Content ──────────────────────────────────────
    private VBox buildContent() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(28));

        Label pageTitle = new Label("Dashboard");
        pageTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
        pageTitle.setStyle("-fx-text-fill: " + UIConstants.TEXT_DARK + ";");

        Label pageSub = new Label("Overview of the vaccination system");
        pageSub.setStyle(
                "-fx-text-fill: " + UIConstants.TEXT_GRAY + "; -fx-font-size: 13px;"
        );

        // ── Stats Row ─────────────────────────────────────
        HBox statsRow = new HBox(16);
        statsRow.getChildren().addAll(
                buildStatCard("👥", "Total Patients",
                        patientCountLabel = new Label("—"), UIConstants.PRIMARY),
                buildStatCard("💉", "Vaccinations Given",
                        vaccinationCountLabel = new Label("—"), UIConstants.SUCCESS),
                buildStatCard("🧪", "Vaccine Types",
                        vaccineCountLabel = new Label("—"), "#7C3AED")
        );

        // ── Quick Actions ─────────────────────────────────
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        actionsTitle.setStyle("-fx-text-fill: " + UIConstants.TEXT_DARK + ";");

        HBox actionsRow = new HBox(16);
        actionsRow.getChildren().addAll(
                buildActionCard("👤", "Manage Patients",
                        "Add, edit, delete patients",
                        patientsBtn = buildNavBtn("Open Patients")),
                buildActionCard("📋", "Vaccine Records",
                        "Add and view vaccination records",
                        recordsBtn = buildNavBtn("Open Records")),
                buildActionCard("🔍", "Search",
                        "Search patients and records",
                        searchBtn = buildNavBtn("Open Search"))
        );

        content.getChildren().addAll(
                pageTitle, pageSub, statsRow, actionsTitle, actionsRow
        );
        return content;
    }

    // ── Stat Card ─────────────────────────────────────────
    private VBox buildStatCard(String icon, String label,
                               Label valueLabel, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setStyle(UIConstants.CARD_STYLE);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(28));

        Label lbl = new Label(label);
        lbl.setStyle(
                "-fx-text-fill: " + UIConstants.TEXT_GRAY + "; -fx-font-size: 12px;"
        );

        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        card.getChildren().addAll(iconLabel, lbl, valueLabel);
        return card;
    }

    // ── Action Card ───────────────────────────────────────
    private VBox buildActionCard(String icon, String title,
                                 String desc, Button btn) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle(UIConstants.CARD_STYLE);
        card.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(28));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: " + UIConstants.TEXT_DARK + ";");

        Label descLabel = new Label(desc);
        descLabel.setStyle(
                "-fx-text-fill: " + UIConstants.TEXT_GRAY + "; -fx-font-size: 12px;"
        );
        descLabel.setWrapText(true);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(iconLabel, titleLabel, descLabel, spacer, btn);
        return card;
    }

    private Button buildNavBtn(String text) {
        Button btn = new Button(text);
        btn.setStyle(UIConstants.PRIMARY_BTN);
        return btn;
    }

    // ── Getters for DashboardController ──────────────────
    public Label  getPatientCountLabel()     { return patientCountLabel; }
    public Label  getVaccinationCountLabel() { return vaccinationCountLabel; }
    public Label  getVaccineCountLabel()     { return vaccineCountLabel; }
    public Label  getWelcomeLabel()          { return welcomeLabel; }
    public Button getPatientsBtn()           { return patientsBtn; }
    public Button getRecordsBtn()            { return recordsBtn; }
    public Button getSearchBtn()             { return searchBtn; }
    public Button getLogoutBtn()             { return logoutBtn; }
    public Stage  getStage()                 { return stage; }
}