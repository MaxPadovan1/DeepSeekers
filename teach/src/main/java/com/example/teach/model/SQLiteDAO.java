package com.example.teach.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDAO
{
    public static void initialize() {
        Connection conn = SQliteConnection.getInstance();
        try (Statement stmt = conn.createStatement()) {
            // 1) Create Users table if missing
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Users (" +
                            " id           TEXT    PRIMARY KEY," +
                            " passwordHash TEXT    NOT NULL," +
                            " firstName    TEXT    NOT NULL," +
                            " lastName     TEXT    NOT NULL," +
                            " role         TEXT    NOT NULL," +
                            " email        TEXT" +              // ← include email here
                            ")"
            );

            // 2) Create role-specific tables if you want them
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Students (" +
                            " id TEXT PRIMARY KEY REFERENCES Users(id)" +
                            ")"
            );
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Teachers (" +
                            " id TEXT PRIMARY KEY REFERENCES Users(id)" +
                            ")"
            );
            stmt.executeUpdate(
                            "CREATE TABLE IF NOT EXISTS Subjects ("+
                               "id   TEXT PRIMARY KEY,"+
                               "name TEXT NOT NULL)"
            );

            // 3) If you’re adding email to an existing DB, try an ALTER
            try {
                stmt.executeUpdate("ALTER TABLE Users ADD COLUMN email TEXT");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                stmt.executeUpdate("ALTER TABLE Users ADD COLUMN subject_ids TEXT");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
