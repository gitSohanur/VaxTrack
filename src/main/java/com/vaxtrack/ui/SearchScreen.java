package com.vaxtrack.ui;

import com.vaxtrack.model.SearchResult;
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

public class SearchScreen {

    private final Stage stage;
    private final Scene scene;

    private TextField                    searchField;
    private Button                       searchBtn;
    private Button                       backBtn;
    private Label                        resultCountLabel;
    private Label                        statusLabel;
    private TableView<SearchResult>      table;
    private ObservableList<SearchResult> tableData;

    // ── Constructor ───────────────────────────────────────
    public SearchScreen(Stage stage) {
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
        root.setCenter(buildContent());
        root.setBottom(buildStatusBar());
        return new Scene(root, 900, 580);
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

        Label title = new Label("🔍 Search");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setStyle("-fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        searchField = new TextField();
        searchField.setPromptText("Search patients, vaccines...");
        searchField.setPrefWidth(280);
        searchField.setStyle(UIConstants.INPUT_STYLE);

        searchBtn = new Button("🔍 Search");
        searchBtn.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: " + UIConstants.PRIMARY + ";" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 6;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 7 16 7 16;"
        );

        searchField.setOnAction(e -> searchBtn.fire());

        bar.getChildren().addAll(backBtn, title, spacer, searchField, searchBtn);
        return bar;
    }

    // ── Content ───────────────────────────────────────────
    private VBox buildContent() {
        VBox content = new VBox(16);
        content.setPadding(new Insets(24));

        resultCountLabel = new Label("Enter a keyword above to search");
        resultCountLabel.setStyle(
                "-fx-text-fill: " + UIConstants.TEXT_GRAY + "; -fx-font-size: 13px;"
        );

        // Results table
        table = new TableView<>(tableData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No results found."));
        VBox.setVgrow(table, Priority.ALWAYS);

        // Type column with color coding
        TableColumn<SearchResult, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getType().toString()));
        typeCol.setPrefWidth(120);
        typeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle(item.equals("PATIENT")
                            ? "-fx-text-fill: " + UIConstants.PRIMARY +
                              "; -fx-font-weight: bold;"
                            : "-fx-text-fill: " + UIConstants.SUCCESS +
                              "; -fx-font-weight: bold;"
                    );
                }
            }
        });

        TableColumn<SearchResult, String> titleCol = new TableColumn<>("Name");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<SearchResult, String> subtitleCol = new TableColumn<>("Details");
        subtitleCol.setCellValueFactory(new PropertyValueFactory<>("subtitle"));

        TableColumn<SearchResult, String> tagCol = new TableColumn<>("Tag");
        tagCol.setCellValueFactory(new PropertyValueFactory<>("tag"));
        tagCol.setPrefWidth(130);

        TableColumn<SearchResult, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(110);

        table.getColumns().addAll(typeCol, titleCol, subtitleCol, tagCol, dateCol);

        content.getChildren().addAll(resultCountLabel, table);
        return content;
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

    // ── Public helpers called by controller ───────────────
    public void setStatus(String msg, boolean isError) {
        statusLabel.setText(msg);
        statusLabel.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: " +
                        (isError ? UIConstants.DANGER : UIConstants.SUCCESS) + ";"
        );
    }

    // ── Getters for SearchController ──────────────────────
    public TextField                    getSearchField()      { return searchField; }
    public Button                       getSearchBtn()        { return searchBtn; }
    public Button                       getBackBtn()          { return backBtn; }
    public Label                        getResultCountLabel() { return resultCountLabel; }
    public TableView<SearchResult>      getTable()            { return table; }
    public ObservableList<SearchResult> getTableData()        { return tableData; }
    public Stage                        getStage()            { return stage; }
}