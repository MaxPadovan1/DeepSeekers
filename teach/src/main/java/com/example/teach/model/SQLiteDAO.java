package com.example.teach.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.example.teach.model.StudyFile;
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

            // Lesson Plan table
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS LessonPlans (" +
                            "  id           TEXT PRIMARY KEY, " +
                            "  subject_id   TEXT NOT NULL REFERENCES Subjects(id), " +
                            "  title        TEXT NOT NULL, " +
                            "  details      TEXT" +
                            ")"
            );

            // Homework table (fixed)
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Homework (" +
                            "  id TEXT PRIMARY KEY, " +
                            "  subject_id TEXT NOT NULL REFERENCES Subjects(id), " +
                            "  week TEXT NOT NULL, " +
                            "  title TEXT NOT NULL, " +
                            "  description TEXT, " +
                            "  due_date TEXT, " +
                            "  release_date TEXT, " +
                            "  open_date TEXT " +
                            ")"
            );


            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS study_files (" +
                            "  id           INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "  subject_id   TEXT NOT NULL REFERENCES Subjects(id)," +
                            "  week         TEXT," +
                            "  file_name    TEXT NOT NULL," +
                            "  title        TEXT NOT NULL," +
                            "  file_data    BLOB NOT NULL," +
                            "  is_released  INTEGER NOT NULL DEFAULT 0" +
                            ")"
            );

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS Grades (" +
                            "id TEXT PRIMARY KEY, " +
                            "assignment_id TEXT NOT NULL, " +
                            "student_id TEXT NOT NULL, " +
                            "grade TEXT, " +
                            "feedback TEXT, " +
                            "submitted_time TEXT, " +
                            "FOREIGN KEY(assignment_id) REFERENCES Assignments(id), " +
                            "FOREIGN KEY(student_id) REFERENCES Students(id), " +
                            "UNIQUE (assignment_id, student_id)" +
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

            }try {
                stmt.execute("ALTER TABLE Homeworks ADD COLUMN week TEXT");
            } catch (SQLException ignore) {}

            try {
                stmt.execute("ALTER TABLE Homeworks ADD COLUMN release_date TEXT");
            } catch (SQLException ignore) {}

            try {
                stmt.execute("ALTER TABLE Homeworks ADD COLUMN open_date TEXT");
            } catch (SQLException ignore) {}

            try {
                stmt.execute("UPDATE Subjects SET id = 'BIO150', name = 'General Biology' WHERE id = 'TESTSUBJECT';");
            } catch (SQLException ignore) {}



        } catch (SQLException e) {
            e.printStackTrace();
            // In production, consider logging to a file or system logger
        }
    }
    /**
     * Saves a study file to the {@code study_files} table.
     *
     * @param subjectId   the subject this file belongs to
     * @param week        the week label associated with the file
     * @param fileName    original filename
     * @param title       display title for the file
     * @param data        binary data of the file
     * @param isReleased  whether the file should be marked as released
     */
    public static void saveStudyFile(String subjectId, String week, String fileName, String title, byte[] data, boolean isReleased) {
        String sql = "INSERT INTO study_files (subject_id, week, file_name, title, file_data, is_released) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = SQliteConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subjectId);
            stmt.setString(2, week);
            stmt.setString(3, fileName);
            stmt.setString(4, title);
            stmt.setBytes(5, data);
            stmt.setInt(6, isReleased ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Marks all pending (unreleased) study files for a given subject as released and sets their week.
     *
     * @param subjectId the subject whose files are to be updated
     * @param week      the week label to assign to the files
     */
    public static void releasePendingFiles(String subjectId, String week) {
        String sql = "UPDATE study_files SET week = ?, is_released = 1 WHERE subject_id = ? AND is_released = 0";
        try (Connection conn = SQliteConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, week);
            stmt.setString(2, subjectId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves all released study files for a given subject and week.
     *
     * @param subjectId the ID of the subject
     * @param week      the week for which files are released
     * @return a list of {@link StudyFile} objects containing file data
     */
    public static List<StudyFile> getReleasedFilesForWeek(String subjectId, String week) {
        List<StudyFile> files = new ArrayList<>();
        String sql = "SELECT file_name, title, file_data FROM study_files WHERE subject_id = ? AND week = ? AND is_released = 1";
        try (Connection conn = SQliteConnection.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subjectId);
            stmt.setString(2, week);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("file_name");
                String title = rs.getString("title");
                byte[] data = rs.getBytes("file_data");
                files.add(new StudyFile(name, title, data));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

}