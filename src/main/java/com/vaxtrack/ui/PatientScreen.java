package com.vaxtrack.ui;

import com.vaxtrack.model.Patient;
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


public class PatientScreen {

    private final Stage stage;
    private final Scene scene;

    // Table
    private TableView<Patient>         table;
    private ObservableList<Patient>    tableData;

    // Form fields
    private TextField         fullNameField;
    private DatePicker        dobPicker;
    private ComboBox<String>  genderCombo;
    private TextField         phoneField;
    private TextField         addressField;

    // Buttons
    private Button addBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private Button clearBtn;
    private Button backBtn;
    private Button searchBtn;

    // Search
    private TextField searchField;

    // Status
    private Label statusLabel;

    // ── Constructor ───────────────────────────────────────
    public PatientScreen(Stage stage) {
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

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.65);
        splitPane.getItems().addAll(buildTablePane(), buildFormPane());
        splitPane.setStyle("-fx-background-color: transparent;");

        root.setCenter(splitPane);
        root.setBottom(buildStatusBar());

        return new Scene(root, 980, 640);
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

        Label title = new Label("👤 Patient Management");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("Search by name or phone...");
        searchField.setPrefWidth(220);
        searchField.setStyle(UIConstants.INPUT_STYLE);

        searchBtn = new Button("Search");
        searchBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + UIConstants.PRIMARY + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 7 14 7 14;"
        );

        bar.getChildren().addAll(backBtn, title, spacer, searchField, searchBtn);
        return bar;
    }

    // ── Table Pane ────────────────────────────────────────
    private VBox buildTablePane() {
        VBox pane = new VBox(12);
        pane.setPadding(new Insets(20));

        Label header = new Label("Patient List");
        header.setFont(Font.font("System", FontWeight.BOLD, 14));
        header.setStyle("-fx-text-fill: " + UIConstants.TEXT_DARK + ";");

        table = new TableView<>(tableData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-radius: 8;");
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Patient, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Patient, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<Patient, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDateOfBirth().toString()));

        TableColumn<Patient, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getAge()));
        ageCol.setPrefWidth(55);

        TableColumn<Patient, String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderCol.setPrefWidth(70);

        TableColumn<Patient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Patient, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        table.getColumns().addAll(
                idCol, nameCol, dobCol, ageCol, genderCol, phoneCol, addressCol
        );

        // Row click → populate form
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> populateForm(selected)
        );

        pane.getChildren().addAll(header, table);
        return pane;
    }

    // ── Form Pane ─────────────────────────────────────────
    private VBox buildFormPane() {
        VBox pane = new VBox(14);
        pane.setPadding(new Insets(20));
        pane.setStyle(UIConstants.CARD_STYLE + "-fx-padding: 20;");

        Label header = new Label("Patient Details");
        header.setFont(Font.font("System", FontWeight.BOLD, 14));
        header.setStyle("-fx-text-fill: " + UIConstants.TEXT_DARK + ";");

        fullNameField = new TextField();
        fullNameField.setPromptText("Enter full name");
        fullNameField.setStyle(UIConstants.INPUT_STYLE);

        dobPicker = new DatePicker();
        dobPicker.setPromptText("Select date of birth");
        dobPicker.setStyle(UIConstants.INPUT_STYLE);
        dobPicker.setMaxWidth(Double.MAX_VALUE);

        genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Select gender");
        genderCombo.setStyle(UIConstants.INPUT_STYLE);
        genderCombo.setMaxWidth(Double.MAX_VALUE);

        phoneField = new TextField();
        phoneField.setPromptText("e.g. 01711000000");
        phoneField.setStyle(UIConstants.INPUT_STYLE);

        addressField = new TextField();
        addressField.setPromptText("Enter address");
        addressField.setStyle(UIConstants.INPUT_STYLE);

        // Buttons
        addBtn    = new Button("➕ Add Patient");
        updateBtn = new Button("✏ Update");
        deleteBtn = new Button("🗑 Delete");
        clearBtn  = new Button("✕ Clear");

        addBtn.setStyle(UIConstants.SUCCESS_BTN);
        updateBtn.setStyle(UIConstants.PRIMARY_BTN);
        deleteBtn.setStyle(UIConstants.DANGER_BTN);
        clearBtn.setStyle(
                "-fx-background-color: #E2E8F0; -fx-text-fill: #475569;" +
                        "-fx-font-size: 13px; -fx-background-radius: 6;" +
                        "-fx-cursor: hand; -fx-padding: 8 16 8 16;"
        );

        HBox btnRow = new HBox(8, addBtn, updateBtn, deleteBtn, clearBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        pane.getChildren().addAll(
                header,
                labeledNode("Full Name",     fullNameField),
                labeledNode("Date of Birth", dobPicker),
                labeledNode("Gender",        genderCombo),
                labeledNode("Phone",         phoneField),
                labeledNode("Address",       addressField),
                btnRow
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

    private void populateForm(Patient p) {
        if (p == null) return;
        fullNameField.setText(p.getFullName());
        dobPicker.setValue(p.getDateOfBirth());
        genderCombo.setValue(p.getGender());
        phoneField.setText(p.getPhone());
        addressField.setText(p.getAddress());
    }

    // ── Public helpers called by controller ───────────────
    public void clearForm() {
        fullNameField.clear();
        dobPicker.setValue(null);
        genderCombo.setValue(null);
        phoneField.clear();
        addressField.clear();
        table.getSelectionModel().clearSelection();
    }

    public void setStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: " +
                        (isError ? UIConstants.DANGER : UIConstants.SUCCESS) + ";"
        );
    }

    // ── Getters for PatientController ─────────────────────
    public TableView<Patient>        getTable()        { return table; }
    public ObservableList<Patient>   getTableData()    { return tableData; }
    public TextField                 getFullNameField(){ return fullNameField; }
    public DatePicker                getDobPicker()    { return dobPicker; }
    public ComboBox<String>          getGenderCombo()  { return genderCombo; }
    public TextField                 getPhoneField()   { return phoneField; }
    public TextField                 getAddressField() { return addressField; }
    public Button                    getAddBtn()       { return addBtn; }
    public Button                    getUpdateBtn()    { return updateBtn; }
    public Button                    getDeleteBtn()    { return deleteBtn; }
    public Button                    getClearBtn()     { return clearBtn; }
    public Button                    getBackBtn()      { return backBtn; }
    public TextField                 getSearchField()  { return searchField; }
    public Button                    getSearchBtn()    { return searchBtn; }
    public Stage                     getStage()        { return stage; }
}