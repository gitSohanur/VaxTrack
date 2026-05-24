package com.vaxtrack.service;

import com.vaxtrack.database.dao.UserDAO;
import com.vaxtrack.model.User;
import com.vaxtrack.utils.SessionManager;

import java.sql.SQLException;

/**
 * Business logic for authentication.
 * Sits between the UI and the DAO.
 * Demonstrates: Service layer, Separation of concerns
 */
public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Validates credentials and creates a session if successful.
     *
     * @return true if login succeeded, false otherwise
     * @throws SQLException if a DB error occurs
     */
    public boolean login(String username, String password) throws SQLException {

        // Basic input validation
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;

        User user = userDAO.findByCredentials(username.trim(), password.trim());

        if (user != null) {
            SessionManager.getInstance().setCurrentUser(user);
            System.out.println("[Auth] Login successful: " + user.getDisplayInfo());
            return true;
        }

        System.out.println("[Auth] Login failed for username: " + username);
        return false;
    }

    /**
     * Clears the current session (logout).
     */
    public void logout() {
        System.out.println("[Auth] Logging out: " +
                SessionManager.getInstance().getCurrentUser().getUsername());
        SessionManager.getInstance().clear();
    }

    /**
     * Checks if anyone is currently logged in.
     */
    public boolean isLoggedIn() {
        return SessionManager.getInstance().getCurrentUser() != null;
    }
}
