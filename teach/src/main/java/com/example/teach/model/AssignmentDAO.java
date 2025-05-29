package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing Assignment records in the database.
 * <p>
 * Provides methods to retrieve, add, update, release, unrelease, and delete assignments.
 * Communicates with the underlying SQLite database via JDBC.
 */
public class AssignmentDAO {

    /** Connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /**
     * Fetches all assignments for the given subject ID, regardless of release status.
     *
     * @param subjectId the ID of the subject whose assignments to retrieve
     * @return a list of {@link Assignment} objects belonging to the subject
     * @throws SQLException if a database access error occurs
     */
    public List<Assignment> getBySubject(String subjectId) throws SQLException {
        List<Assignment> out = new ArrayList<>();
        String sql = "SELECT id, title, description, due_date, is_released FROM Assignments WHERE subject_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Assignment(
                            rs.getString("id"),
                            subjectId,
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("due_date"),
                            rs.getBoolean("is_released")
                    ));
                }
            }
        }
        return out;
    }

    /**
     * Inserts a new assignment record into the database.
     *
     * @param a the {@link Assignment} object to add
     * @throws SQLException if a database access error occurs
     */
    public void add(Assignment a) throws SQLException {
        String sql = "INSERT INTO Assignments(id, subject_id, title, description, due_date, is_released) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getId());
            ps.setString(2, a.getSubjectId());
            ps.setString(3, a.getTitle());
            ps.setString(4, a.getDescription());
            ps.setString(5, a.getDueDate());
            ps.setBoolean(6, a.isReleased());
            ps.executeUpdate();
        }
    }

    /**
     * Marks the assignment with the given ID as released (is_released = 1).
     *
     * @param assignmentId the ID of the assignment to release
     * @throws SQLException if a database access error occurs
     */
    public void releaseAssignment(String assignmentId) throws SQLException {
        String sql = "UPDATE Assignments SET is_released = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignmentId);
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves only the released assignments for the given subject.
     *
     * @param subjectId the subject ID to filter by
     * @return list of released assignments
     * @throws SQLException if a database access error occurs
     */
    public List<Assignment> getReleasedAssignments(String subjectId) throws SQLException {
        List<Assignment> out = new ArrayList<>();
        String sql = "SELECT id, title, description, due_date, is_released FROM Assignments WHERE subject_id=? AND is_released=1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Assignment(
                            rs.getString("id"),
                            subjectId,
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("due_date"),
                            rs.getBoolean("is_released")
                    ));
                }
            }
        }
        return out;
    }

    /**
     * Deletes the assignment with the given ID from the database.
     *
     * @param assignmentId the ID of the assignment to delete
     * @throws SQLException if a database access error occurs
     */
    public void removeAssignment(String assignmentId) throws SQLException {
        String sql = "DELETE FROM Assignments WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignmentId);
            ps.executeUpdate();
        }
    }

    /**
     * Updates the title, description, and due date of an existing assignment.
     * This does not modify the is_released status.
     *
     * @param assignment the assignment object containing updated values
     * @throws SQLException if a database access error occurs
     */
    public void updateAssignment(Assignment assignment) throws SQLException {
        String sql = "UPDATE Assignments SET title = ?, description = ?, due_date = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignment.getTitle());
            ps.setString(2, assignment.getDescription());
            ps.setString(3, assignment.getDueDate());
            ps.setString(4, assignment.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Marks the assignment with the given ID as not released (is_released = 0).
     *
     * @param assignmentId the ID of the assignment to unrelease
     * @throws SQLException if a database access error occurs
     */
    public void unreleaseAssignment(String assignmentId) throws SQLException {
        String sql = "UPDATE Assignments SET is_released = 0 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignmentId);
            ps.executeUpdate();
        }
    }
}