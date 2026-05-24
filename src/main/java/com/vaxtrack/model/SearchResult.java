package com.vaxtrack.model;

/**
 * Lightweight wrapper returned by search queries.
 * Holds display-ready data without exposing raw DB rows.
 * Demonstrates: Encapsulation, Single Responsibility
 */
public class SearchResult {

    public enum ResultType {
        PATIENT,
        VACCINE_RECORD
    }

    private int        id;
    private ResultType type;
    private String     title;       // Primary display text
    private String     subtitle;    // Secondary display text
    private String     tag;         // e.g. gender, vaccine name
    private String     date;        // formatted date string

    // --- Constructor ---
    public SearchResult(int id, ResultType type,
                        String title, String subtitle,
                        String tag, String date) {
        this.id       = id;
        this.type     = type;
        this.title    = title;
        this.subtitle = subtitle;
        this.tag      = tag;
        this.date     = date;
    }

    // --- Getters ---
    public int getId()           { return id; }
    public ResultType getType()  { return type; }
    public String getTitle()     { return title; }
    public String getSubtitle()  { return subtitle; }
    public String getTag()       { return tag; }
    public String getDate()      { return date; }

    @Override
    public String toString() {
        return "[" + type + "] " + title + " | " + subtitle;
    }
}
