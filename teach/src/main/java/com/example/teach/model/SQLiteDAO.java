package com.example.teach.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDAO {
    private final Connection connection;

    public SQLiteDAO() {
        // grab the shared JDBC connection
        this.connection = SQliteConnection.getInstance();
        // ensure all of our tables and columns exist
        createSchema();
    }

    private void createSchema() {
        try (Statement stmt = connection.createStatement()) {
            // 1) Users table
            String createUsers =
                    "CREATE TABLE IF NOT EXISTS Users (" +
                            "  id           TEXT PRIMARY KEY, " +
                            "  passwordHash TEXT NOT NULL,      " +
                            "  firstName    TEXT NOT NULL,      " +
                            "  lastName     TEXT NOT NULL,      " +
                            "  role         TEXT NOT NULL,      " +
                            "  email        TEXT,               " +   // ← email column
                            "  subject_ids  TEXT                " +   // ← CSV list of subject IDs
                            ")";
            stmt.execute(createUsers);

            // 2) Students table
            String createStudents =
                    "CREATE TABLE IF NOT EXISTS Students (" +
                            "  id TEXT PRIMARY KEY REFERENCES Users(id)" +
                            ")";
            stmt.execute(createStudents);

            // 3) Teachers table
            String createTeachers =
                    "CREATE TABLE IF NOT EXISTS Teachers (" +
                            "  id TEXT PRIMARY KEY REFERENCES Users(id)," +
                            "  subject_id TEXT REFERENCES Subjects(id)" +
                            ")";
            stmt.execute(createTeachers);

            // 4) Subjects master list
            String createSubjects =
                    "CREATE TABLE IF NOT EXISTS Subjects (" +
                            "  id   TEXT PRIMARY KEY, " +
                            "  name TEXT NOT NULL     " +
                            ")";
            stmt.execute(createSubjects);

            // 5) Join table for Student ↔ Subjects
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS StudentSubjects (" +
                            "  student_id TEXT NOT NULL REFERENCES Users(id)," +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  PRIMARY KEY (student_id, subject_id)" +
                            ")"
            );
            // 5) Assignments
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Assignments (" +
                            "  id         TEXT PRIMARY KEY,               " +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  title      TEXT NOT NULL,                  " +
                            "  description TEXT,                          " +
                            "  due_date   TEXT                            " +
                            ")"
            );

            // 6) Homeworks
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Homeworks (" +
                            "  id         TEXT PRIMARY KEY,               " +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  title      TEXT NOT NULL,                  " +
                            "  description TEXT,                          " +
                            "  due_date   TEXT                            " +
                            ")"
            );

            // 7) Study
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Study (" +
                            "  id         TEXT PRIMARY KEY,               " +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  title      TEXT NOT NULL,                  " +
                            "  content    TEXT NOT NULL                   " +
                            ")"

            );

            // 6) In case we're upgrading an old DB, attempt to add missing columns
            try {
                stmt.executeUpdate("ALTER TABLE Users ADD COLUMN email TEXT");
            } catch (SQLException ignore) {
                // already exists → safe to ignore
            }
            try {
                stmt.executeUpdate("ALTER TABLE Users ADD COLUMN subject_ids TEXT");
            } catch (SQLException ignore) {
                // already exists → safe to ignore
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
