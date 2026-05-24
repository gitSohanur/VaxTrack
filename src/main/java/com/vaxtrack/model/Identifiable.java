package com.vaxtrack.model;

/**
 * Every entity that has a database ID must implement this.
 * Demonstrates: Interface usage
 */
public interface Identifiable {
    int getId();
    void setId(int id);
}
