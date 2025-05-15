package com.example.teach.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object that ensures the database schema exists and is up-to-date.
 * <p>
 * On instantiation, this class retrieves a shared JDBC connection
 * via {@link SQliteConnection} and invokes {@link #createSchema()}
 * to create tables, indexes, and seed initial data if necessary.
 */
public class SQLiteDAO {

    /** Shared JDBC connection to the SQLite database. */
    private final Connection connection;

    /**
     * Constructs the DAO, retrieves a connection, and ensures the schema is created.
     */
    public SQLiteDAO() {
        this.connection = SQliteConnection.getInstance();
        createSchema();
    }

    /**
     * Creates all required tables and seeds initial data if not present.
     * <p>
     * Executes DDL statements to create subjects, users, students, teachers,
     * join tables, assignments, homework, and study tables. Also attempts to
     * alter the Users table to add missing columns for backward compatibility.
     * Errors during execution are printed but not rethrown, as schema setup
     * should not block application startup if it fails repeatedly.
     */
    private void createSchema() {
        try (Statement stmt = connection.createStatement()) {
            // 4) Subjects master list
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Subjects (" +
                            "  id   TEXT PRIMARY KEY, " +
                            "  name TEXT NOT NULL     " +
                            ")"
            );

            // 4.1) Populate subjects table with initial data
            stmt.execute(
                    "INSERT OR IGNORE INTO Subjects(id, name) VALUES " +
                            "('MATH101','Calculus I')," +
                            "('ENG202','English Literature')," +
                            "('CS102','Intro to Programming')," +
                            "('HIS215','World History')," +
                            "('TESTSUBJECT','To see if you can select 5 subjects')"
            );

            // 1) Users table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Users (" +
                            "  id           TEXT PRIMARY KEY, " +
                            "  passwordHash TEXT NOT NULL,      " +
                            "  firstName    TEXT NOT NULL,      " +
                            "  lastName     TEXT NOT NULL,      " +
                            "  role         TEXT NOT NULL,      " +
                            "  email        TEXT,               " +
                            "  subject_ids  TEXT                " +
                            ")"
            );

            // 2) Students table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Students (" +
                            "  id TEXT PRIMARY KEY REFERENCES Users(id)" +
                            ")"
            );

            // 3) Teachers table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Teachers (" +
                            "  id         TEXT PRIMARY KEY REFERENCES Users(id)," +
                            "  subject_id TEXT REFERENCES Subjects(id)," +
                            "  UNIQUE(subject_id)" +
                            ")"
            );

            // 5) Student ↔ Subject join table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS StudentSubjects (" +
                            "  student_id TEXT NOT NULL REFERENCES Users(id)," +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  PRIMARY KEY (student_id, subject_id)" +
                            ")"
            );

            // Assignments table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Assignments (" +
                            "  id         TEXT PRIMARY KEY," +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  title      TEXT NOT NULL," +
                            "  description TEXT," +
                            "  due_date   TEXT," +
                            "is_released BOOLEAN DEFAULT 0"+
                            ")"
            );

            // Homework table (fixed)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Homework (" +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id), " +
                            "  week TEXT NOT NULL, " +
                            "  title TEXT NOT NULL, " +
                            "  description TEXT, " +
                            "  due_date TEXT, " +
                            "  release_date TEXT, " +
                            "  open_date TEXT " +
                            "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            ")"
            );

            // Study table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Study (" +
                            "  id         TEXT PRIMARY KEY," +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  title      TEXT NOT NULL," +
                            "  content    TEXT NOT NULL" +
                            ")"
            );
            // Submission table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Submissions ("+
                            "id TEXT PRIMARY KEY,"+
                            "assignment_id TEXT NOT NULL REFERENCES Assignments(id),"+
                            "student_id TEXT NOT NULL REFERENCES Students(id),"+
                            "file_path TEXT,"+
                            "timestamp TEXT"+
                            ")"

            );
            stmt.execute(
                    "CREATE UNIQUE INDEX IF NOT EXISTS idx_submission_unique ON Submissions(assignment_id, student_id)"
            );


            // 6) Add missing columns to Users table for existing schemas
            try {
                stmt.execute("ALTER TABLE Users ADD COLUMN email TEXT");
            } catch (SQLException ignore) {
                // column already exists
            }
            try {
                stmt.execute("ALTER TABLE Users ADD COLUMN subject_ids TEXT");
            } catch (SQLException ignore) {
                // column already exists
            }
            try{
                stmt.execute("ALTER TABLE Assignments ADD COLUMN is_released BOOLEAN DEFAULT 0");
            } catch (SQLException ignore){

            }

            // 🧪 Development: reset tables to allow clean testing
            stmt.execute("DELETE FROM Teachers");
            stmt.execute("DELETE FROM Users");
            stmt.execute("DELETE FROM Homework");

        } catch (SQLException e) {
            e.printStackTrace();
            // In production, consider logging to a file or system logger
        }


    }
}