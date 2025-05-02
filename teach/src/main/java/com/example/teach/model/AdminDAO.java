package com.example.teach.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple utility to purge (“truncate”) large tables in your schema.
 * You can call these from a debug/admin screen or even from a CLI main().
 */
public class AdminDAO {
    private final Connection conn = SQliteConnection.getInstance();

    /** Delete all rows from the Students table (but keep the table). */
    public void clearStudents() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM Students");
            st.executeUpdate("DELETE FROM Users WHERE role = 'Student'");
        }
    }

    /** Delete all rows from the Teachers table (but keep the table). */
    public void clearTeachers() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM Teachers");
            st.executeUpdate("DELETE FROM Users WHERE role = 'Teacher'");
        }
    }

    /** Clear the join‐table of Student ↔ Subject mappings. */
    public void clearStudentSubjects() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM StudentSubjects");
        }
    }

    /** (Optional) reclaim disk space after big deletes. */
    public void vacuum() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("VACUUM");
        }
    }
}