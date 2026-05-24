package com.vaxtrack.model;

import java.time.LocalDateTime;

/**
 * Shared base for all database-backed entities.
 * Demonstrates: Abstraction + Inheritance
 */
public abstract class BaseEntity implements Identifiable, Displayable {

    private int id;
    private LocalDateTime createdAt;

    // --- Constructors ---

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public BaseEntity(int id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
    }

    // --- Identifiable implementation ---

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    // --- Getters / Setters ---

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // --- Abstract method — subclasses MUST implement ---
    // Demonstrates: Abstraction

    @Override
    public abstract String getDisplayInfo();

    @Override
    public String toString() {
        return getDisplayInfo();
    }
}