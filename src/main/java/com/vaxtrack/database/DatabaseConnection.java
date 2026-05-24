package com.vaxtrack.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton pattern — only one DB connection is shared across the app.
 */
public class DatabaseConnection {

    private static Connection connection = null;

    // Private constructor — no one can instantiate this class
    private DatabaseConnection() {}

    /**
     * Returns the single shared connection.
     * Creates it on first call.
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(
                        DatabaseConfig.URL,
                        DatabaseConfig.USERNAME,
                        DatabaseConfig.PASSWORD
                );
                System.out.println("[DB] Connection established.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL Driver not found: " + e.getMessage());
            }
        }
        return connection;
    }

    /**
     * Closes the connection safely. Call this on app exit.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("[DB] Connection closed.");
            } catch (SQLException e) {
                System.err.println("[DB] Failed to close connection: " + e.getMessage());
            }
        }
    }
}
