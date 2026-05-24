package com.vaxtrack.database;

/**
 * Central place for all DB connection settings.
 * Change these values to match your local MySQL setup.
 */
public class DatabaseConfig {

    public static final String HOST     = "localhost";
    public static final int    PORT     = 3306;
    public static final String DB_NAME  = "vaxtrack_db";
    public static final String USERNAME = "vaxtrack_user";
    public static final String PASSWORD = "vaxtrack123";

    public static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME +
                    "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // Prevent instantiation
    private DatabaseConfig() {}
}


