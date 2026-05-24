package com.vaxtrack.ui;

import com.vaxtrack.model.Patient;
import com.vaxtrack.model.Vaccine;
import com.vaxtrack.model.VaccineRecord;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.vaxtrack.ui.UIConstants;

public class VaccineRecordScreen {

    private final Stage stage;
    private final Scene scene;

    // Table
    private TableView<VaccineRecord>       table;
    private ObservableList<VaccineRecord>  tableData;

    // Form fields
    private ComboBox<Patient>  patientCombo;
    private ComboBox<Vaccine>  vaccineCombo;
    private Spinner<Integer>   doseSpinner;
    private DatePicker         dateGivenPicker;
    private TextField          administeredByField;
    private TextField          notesField;

    // Buttons
    private Button addBtn;
    private Button deleteBtn;
    private Button clearBtn;
    private Button backBtn;
    private Button refreshBtn;

    // Status
    private Label statusLabel;

    // ── Constructor ───────────────────────────────────────
    public VaccineRecordScreen(Stage stage) {
        this.stage     = stage;
        this.tableData = FXCollections.observableArrayList();
        this.scene     = createScene();
    }

    public Scene getScene() {
        return scene;
    }

    // ── Private scene builder ─────────────────────────────
    private Scene createScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UIConstants.BG_LIGHT + ";");
        root.setTop(buildTopBar());

        SplitPane split = new SplitPane();
        split.setDividerPositions(0.62);
        split.getItems().addAll(buildTablePane(), buildFormPane());
        split.setStyle("-fx-background-color: transparent;");

        root.setCenter(split);
        root.setBottom(buildStatusBar());

        return new Scene(root, 1000, 640);
    }

    // ── Top Bar ───────────────────────────────────────────
    private HBox buildTopBar() {
        HBox bar = new HBox(12);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(14, 24, 14, 24));
        bar.setStyle("-fx-background-color: " + UIConstants.PRIMARY + ";");

        backBtn = new Button("← Back");
        backBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white; -fx-background-radius: 6;" +
                        "-fx-cursor: hand; -fx-padding: 6 12 6 12;"
        );

        Label title = new Label("💉 Vaccine Records");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        refreshBtn = new Button("🔄 Refresh");
        refreshBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white; -fx-background-radius: 6;" +
                        "-fx-cursor: hand; -fx-padding: 6 12 6 12;"
        );

        bar.getChildren().addAll(backBtn, title, spacer, refreshBtn);
        return bar;
    }

    // ── Table Pane ────────────────────────────────────────
    private VBox buildTablePane() {
        VBox pane = new VBox(12);
        pane.setPadding(new Insets(20));

        Label header = new Label("Vaccination Records");
        header.setFont(Font.font("System", FontWeight.BOLD, 14));
        header.setStyle("-fx-text-fill: " + UIConstants.TEXT_DARK + ";");

        table = new TableView<>(tableData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<VaccineRecord, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(45);

        TableColumn<VaccineRecord, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPatientName()));

        TableColumn<VaccineRecord, String> vaccineCol = new TableColumn<>("Vaccine");
        vaccineCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getVaccineName()));

        TableColumn<VaccineRecord, Integer> doseCol = new TableColumn<>("Dose");
        doseCol.setCellValueFactory(new PropertyValueFactory<>("doseNumber"));
        doseCol.setPrefWidth(55);

        TableColumn<VaccineRecord, String> dateCol = new TableColumn<>("Date Given");
        dateCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateGiven().toString()));

        TableColumn<VaccineRecord, String> byCol = new TableColumn<>("Administered By");
        byCol.setCellValueFactory(new PropertyValueFactory<>("administeredBy"));

        TableColumn<VaccineRecord, String> notesCol = new TableColumn<>("Notes");
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        table.getColumns().addAll(
                idCol, patientCol, vaccineCol, doseCol, dateCol, byCol, notesCol
        );

        pane.getChildren().addAll(header, table);
        return pane;
    }

    // ── Form Pane ─────────────────────────────────────────
    private VBox buildFormPane() {
        VBox pane = new VBox(14);
        pane.setPadding(new Insets(20));
        pane.setStyle(UIConstants.CARD_STYLE + "-fx-padding: 20;");

        Label header = new Label("Add Vaccine Record");
        header.setFont(Font.font("System", FontWeight.BOLD, 14));
        header.setStyle("-fx-text-fill: " + UIConstants.TEXT_DARK + ";");

        // Patient ComboBox
        patientCombo = new ComboBox<>();
        patientCombo.setPromptText("Select patient");
        patientCombo.setMaxWidth(Double.MAX_VALUE);
        patientCombo.setStyle(UIConstants.INPUT_STYLE);
        patientCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null :
                        p.getId() + " — " + p.getFullName());
            }
        });
        patientCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Patient p, boolean empty) {
                super.updateItem(p, empty);
                setText(empty || p == null ? null :
                        p.getId() + " — " + p.getFullName());
            }
        });

        // Vaccine ComboBox
        vaccineCombo = new ComboBox<>();
        vaccineCombo.setPromptText("Select vaccine");
        vaccineCombo.setMaxWidth(Double.MAX_VALUE);
        vaccineCombo.setStyle(UIConstants.INPUT_STYLE);
        vaccineCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Vaccine v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null :
                        v.getVaccineName() + " (" + v.getDosesRequired() + " doses)");
            }
        });
        vaccineCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Vaccine v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null :
                        v.getVaccineName() + " (" + v.getDosesRequired() + " doses)");
            }
        });

        // Dose Spinner
        doseSpinner = new Spinner<>(1, 10, 1);
        doseSpinner.setStyle(UIConstants.INPUT_STYLE);
        doseSpinner.setMaxWidth(Double.MAX_VALUE);
        doseSpinner.setEditable(true);

        // Date Picker
        dateGivenPicker = new DatePicker();
        dateGivenPicker.setPromptText("Select date");
        dateGivenPicker.setStyle(UIConstants.INPUT_STYLE);
        dateGivenPicker.setMaxWidth(Double.MAX_VALUE);

        // Text fields
        administeredByField = new TextField();
        administeredByField.setPromptText("e.g. Dr. Rahman");
        administeredByField.setStyle(UIConstants.INPUT_STYLE);
        administeredByField.setMaxWidth(Double.MAX_VALUE);

        notesField = new TextField();
        notesField.setPromptText("Optional notes");
        notesField.setStyle(UIConstants.INPUT_STYLE);
        notesField.setMaxWidth(Double.MAX_VALUE);

        // Buttons
        addBtn    = new Button("➕ Add Record");
        deleteBtn = new Button("🗑 Delete Selected");
        clearBtn  = new Button("✕ Clear");

        addBtn.setStyle(UIConstants.SUCCESS_BTN);
        deleteBtn.setStyle(UIConstants.DANGER_BTN);
        clearBtn.setStyle(
                "-fx-background-color: #E2E8F0; -fx-text-fill: #475569;" +
                        "-fx-font-size: 13px; -fx-background-radius: 6;" +
                        "-fx-cursor: hand; -fx-padding: 8 16 8 16;"
        );

        VBox btnBox = new VBox(8, addBtn, deleteBtn, clearBtn);

        pane.getChildren().addAll(
                header,
                labeledNode("Patient",         patientCombo),
                labeledNode("Vaccine",         vaccineCombo),
                labeledNode("Dose Number",     doseSpinner),
                labeledNode("Date Given",      dateGivenPicker),
                labeledNode("Administered By", administeredByField),
                labeledNode("Notes",           notesField),
                btnBox
        );
        return pane;
    }

    // ── Status Bar ────────────────────────────────────────
    private HBox buildStatusBar() {
        HBox bar = new HBox();
        bar.setPadding(new Insets(8, 16, 8, 16));
        bar.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: " + UIConstants.BORDER + " transparent transparent;"
        );
        statusLabel = new Label("Ready");
        statusLabel.setStyle(
                "-fx-text-fill: " + UIConstants.TEXT_GRAY + "; -fx-font-size: 12px;"
        );
        bar.getChildren().add(statusLabel);
        return bar;
    }

    // ── Helpers ───────────────────────────────────────────
    private VBox labeledNode(String labelText, Control node) {
        Label lbl = new Label(labelText);
        lbl.setStyle(UIConstants.LABEL_STYLE);
        node.setMaxWidth(Double.MAX_VALUE);
        return new VBox(4, lbl, node);
    }

    // ── Public helpers called by controller ───────────────
    public void clearForm() {
        patientCombo.setValue(null);
        vaccineCombo.setValue(null);
        doseSpinner.getValueFactory().setValue(1);
        dateGivenPicker.setValue(null);
        administeredByField.clear();
        notesField.clear();
        table.getSelectionModel().clearSelection();
    }

    public void setStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: " +
                        (isError ? UIConstants.DANGER : UIConstants.SUCCESS) + ";"
        );
    }

    // ── Getters for VaccineRecordController ───────────────
    public TableView<VaccineRecord>       getTable()              { return table; }
    public ObservableList<VaccineRecord>  getTableData()          { return tableData; }
    public ComboBox<Patient>              getPatientCombo()       { return patientCombo; }
    public ComboBox<Vaccine>              getVaccineCombo()       { return vaccineCombo; }
    public Spinner<Integer>              getDoseSpinner()         { return doseSpinner; }
    public DatePicker                    getDateGivenPicker()     { return dateGivenPicker; }
    public TextField                     getAdministeredByField() { return administeredByField; }
    public TextField                     getNotesField()          { return notesField; }
    public Button                        getAddBtn()              { return addBtn; }
    public Button                        getDeleteBtn()           { return deleteBtn; }
    public Button                        getClearBtn()            { return clearBtn; }
    public Button                        getBackBtn()             { return backBtn; }
    public Button                        getRefreshBtn()          { return refreshBtn; }
    public Stage                         getStage()               { return stage; }
}