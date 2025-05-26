package com.example.teach.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton utility for obtaining a JDBC Connection to the SQLite database.
 * <p>
 * Ensures only one Connection instance is created for the application.
 */
public class SQliteConnection {

    /**
     * The single shared Connection instance.
     * Initialized lazily on first request via {@link #getInstance()}.
     */
    private static Connection instance = null;

    /**
     * Private constructor to initialize the SQLite Connection.
     * <p>
     * Connects to the database file 'teach.db' in the working directory.
     * Errors are printed to stderr if the connection fails.
     */
    private SQliteConnection() {
        String url = "jdbc:sqlite:teach.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println("Failed to connect to SQLite database: " + sqlEx.getMessage());
        }
    }

    /**
     * Returns the singleton Connection instance.
     * <p>
     * If the connection has not yet been established, the constructor
     * is invoked to create it.
     *
     * @return the shared {@link Connection} to the SQLite database
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SQliteConnection();
        }
        return instance;
    }

    private static final String URL = "jdbc:sqlite:teach.db";  // ✅ 类级变量

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}