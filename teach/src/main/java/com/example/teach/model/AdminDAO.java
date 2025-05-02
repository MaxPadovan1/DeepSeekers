package com.example.teach.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple utility to purge (“truncate”) large tables in your schema.
 * All SQLExceptions are caught and rethrown as unchecked RuntimeExceptions.
 */
public class AdminDAO {
    private final Connection conn = SQliteConnection.getInstance();

    /** Remove every row from Users */
    private void clearUsers() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("DELETE FROM Users");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear users", e);
        }
    }

    /** Delete all rows from the Students table (but keep the table). */
    private void clearStudents() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM StudentSubjects");
            st.executeUpdate("DELETE FROM Students");
            st.executeUpdate("DELETE FROM Users WHERE role = 'Student'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear students", e);
        }
    }

    /** Delete all rows from the Teachers table (but keep the table). */
    private void clearTeachers() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM Teachers");
            st.executeUpdate("DELETE FROM Users WHERE role = 'Teacher'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear teachers", e);
        }
    }

    /** Clear the join‐table of Student ↔ Subject mappings. */
    private void clearStudentSubjects() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM StudentSubjects");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear student‐subject mappings", e);
        }
    }

    /** (Optional) reclaim disk space after big deletes. */
    private void vacuum() {
        try (Statement st = conn.createStatement()) {
            st.execute("VACUUM");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to VACUUM database", e);
        }
    }

    public void CLEAN_DB()
    {
        try {
            clearStudentSubjects();
            clearStudents();
            clearTeachers();
            clearUsers();
            vacuum();
            System.out.println("!!! DB Cleaned !!!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("!!! DB Failed To Be Cleaned !!!");
        }
    }
}
