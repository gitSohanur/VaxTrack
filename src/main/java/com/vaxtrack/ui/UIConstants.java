package com.vaxtrack.ui;
/**
 * Shared colors and styles across all screens.
 */
public class UIConstants {

    public static final String PRIMARY   = "#2563EB"; // Blue
    public static final String SUCCESS   = "#16A34A"; // Green
    public static final String DANGER    = "#DC2626"; // Red
    public static final String BG_LIGHT  = "#F1F5F9"; // Light gray bg
    public static final String WHITE     = "#FFFFFF";
    public static final String TEXT_DARK = "#1E293B";
    public static final String TEXT_GRAY = "#64748B";
    public static final String BORDER    = "#CBD5E1";

    public static final String CARD_STYLE =
            "-fx-background-color: white;" +
                    "-fx-background-radius: 8;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);";

    public static final String PRIMARY_BTN =
            "-fx-background-color: #2563EB;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 8 20 8 20;";

    public static final String DANGER_BTN =
            "-fx-background-color: #DC2626;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 8 20 8 20;";

    public static final String SUCCESS_BTN =
            "-fx-background-color: #16A34A;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 13px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;" +
                    "-fx-padding: 8 20 8 20;";

    public static final String INPUT_STYLE =
            "-fx-background-color: white;" +
                    "-fx-border-color: #CBD5E1;" +
                    "-fx-border-radius: 6;" +
                    "-fx-background-radius: 6;" +
                    "-fx-padding: 8 12 8 12;" +
                    "-fx-font-size: 13px;";

    public static final String LABEL_STYLE =
            "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #475569;";

    private UIConstants() {}
}
