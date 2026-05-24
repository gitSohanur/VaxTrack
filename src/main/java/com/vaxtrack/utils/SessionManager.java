package com.vaxtrack.utils;

import com.vaxtrack.model.User;

/**
 * Singleton that holds the currently logged-in user.
 * Accessible from anywhere in the app.
 * Demonstrates: Singleton pattern, Global state management
 */
public class SessionManager {

    private static SessionManager instance = null;
    private User currentUser = null;

    // Private constructor — no one creates this externally
    private SessionManager() {}

    /**
     * Returns the single global instance.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // --- Getters & Setters ---

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Wipes the session on logout.
     */
    public void clear() {
        this.currentUser = null;
    }

    /**
     * Quick role-check helper used by UI screens.
     */
    public boolean isAdmin() {
        return currentUser != null &&
                currentUser.getRole() == Role.ADMIN;
    }
}
