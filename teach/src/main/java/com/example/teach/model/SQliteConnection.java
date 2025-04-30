package com.example.teach.model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQliteConnection {
    private static Connection instance = null;

    private SQliteConnection() {
        String url = "jdbc:sqlite:teach.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new SQliteConnection();
        }
        return instance;
    }
}