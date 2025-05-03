package com.example.teach.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Administrative DAO for purging large tables and reclaiming disk space.
 * <p>
 * Provides methods to clear user, student, teacher, and student-subject mapping tables,
 * and an optional VACUUM operation to optimize the SQLite database file.
 * All SQLExceptions are caught and rethrown as unchecked RuntimeExceptions,
 * except in the CLEAN_DB orchestration method where they are logged.
 */
public class AdminDAO {

    /** Connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /**
     * Removes every row from the Users table.
     *
     * @throws SQLException if a database access error occurs
     */
    private void clearUsers() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("DELETE FROM Users");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear users", e);
        }
    }

    /**
     * Deletes all rows related to students, including join entries in StudentSubjects
     * and user records with role 'Student'.
     */
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

    /**
     * Deletes all rows related to teachers, including their user records.
     */
    private void clearTeachers() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM Teachers");
            st.executeUpdate("DELETE FROM Users WHERE role = 'Teacher'");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear teachers", e);
        }
    }

    /**
     * Clears the join table linking students to subjects.
     */
    private void clearStudentSubjects() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM StudentSubjects");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to clear student-subject mappings", e);
        }
    }

    /**
     * Performs a VACUUM operation to reclaim unused space and defragment the database file.
     */
    private void vacuum() {
        try (Statement st = conn.createStatement()) {
            st.execute("VACUUM");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to VACUUM database", e);
        }
    }

    /**
     * Orchestrates a database cleanup: student-subject mappings, student records,
     * teacher records, user records, and finally performs VACUUM.
     * <p>
     * Errors during cleanup are caught and logged; a summary message is printed to console.
     */
    public void CLEAN_DB() {
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