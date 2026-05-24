package com.vaxtrack.controller;

import com.vaxtrack.model.SearchResult;
import com.vaxtrack.service.SearchService;
import com.vaxtrack.ui.SearchScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

/**
 * Wires SearchScreen to SearchService.
 */
public class SearchController {

    private final SearchScreen        searchScreen;
    private final SearchService       searchService;
    private final DashboardController dashboardCtrl;
    private final Stage               stage;

    public SearchController(Stage stage, DashboardController dashboardCtrl) {
        this.stage         = stage;
        this.dashboardCtrl = dashboardCtrl;
        this.searchScreen  = new SearchScreen(stage);
        this.searchService = new SearchService();
    }

    public Scene getScene() {
        return searchScreen.getScene();
    }

    public void init() {
        wireButtons();
    }

    // ── Wire buttons ──────────────────────────────────────
    private void wireButtons() {

        // ── Search button ─────────────────────────────────
        searchScreen.getSearchBtn().setOnAction(e -> handleSearch());

        // ── Enter key ─────────────────────────────────────
        searchScreen.getSearchField().setOnAction(e -> handleSearch());

        // ── Back to Dashboard ─────────────────────────────
        searchScreen.getBackBtn().setOnAction(e ->
                dashboardCtrl.returnToDashboard()
        );
    }

    // ── SEARCH ────────────────────────────────────────────
    private void handleSearch() {
        String keyword = searchScreen.getSearchField().getText();

        if (keyword == null || keyword.isBlank()) {
            searchScreen.getResultCountLabel().setText("Please enter a search keyword.");
            searchScreen.setStatus("⚠ Search keyword is empty.", true);
            return;
        }

        try {
            List<SearchResult> results = searchService.search(keyword);
            searchScreen.getTableData().setAll(results);

            if (results.isEmpty()) {
                searchScreen.getResultCountLabel()
                        .setText("No results found for: \"" + keyword + "\"");
                searchScreen.setStatus("No results found.", true);
            } else {
                searchScreen.getResultCountLabel()
                        .setText("Found " + results.size()
                                + " result(s) for: \"" + keyword + "\"");
                searchScreen.setStatus("Search complete.", false);
            }

        } catch (IllegalArgumentException e) {
            searchScreen.setStatus("⚠ " + e.getMessage(), true);
            searchScreen.getResultCountLabel().setText(e.getMessage());
        } catch (SQLException e) {
            searchScreen.setStatus("DB Error: " + e.getMessage(), true);
        }
    }
}