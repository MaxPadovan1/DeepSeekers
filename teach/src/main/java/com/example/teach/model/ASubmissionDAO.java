package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing assignment submissions in the database.
 * <p>
 * Provides methods to insert, update, retrieve, and query student submissions.
 */
public class ASubmissionDAO {

    private final Connection conn = SQliteConnection.getInstance();
    /**
     * Inserts a new submission record into the database.
     *
     * @param s the {@link ASubmission} to insert
     * @throws SQLException if a database error occurs
     */
    public void submitAssignment(ASubmission s) throws SQLException {
        String sql = "INSERT INTO Submissions(id, assignment_id, student_id, file_path, timestamp) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getId());
            ps.setString(2, s.getAssignmentId());
            ps.setString(3, s.getStudentId());
            ps.setString(4, s.getFilePath());
            ps.setString(5, s.getTimestamp());
            ps.executeUpdate();
        }
    }

    /**
     * Retrieves all submissions for a specific assignment.
     *
     * @param assignmentId the assignment ID to query
     * @return a list of {@link ASubmission} objects submitted for the assignment
     * @throws SQLException if a database error occurs
     */
    public List<ASubmission> getSubmissionsByAssignmentId(String assignmentId) throws SQLException {
        List<ASubmission> list = new ArrayList<>();
        String sql = "SELECT id, assignment_id, student_id, file_path, timestamp FROM Submissions WHERE assignment_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ASubmission(
                            rs.getString("id"),
                            rs.getString("assignment_id"),
                            rs.getString("student_id"),
                            rs.getString("file_path"),
                            rs.getString("timestamp")
                    ));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves a specific submission by student ID and assignment ID.
     *
     * @param studentId    the student who submitted
     * @param assignmentId the related assignment
     * @return the {@link ASubmission} object if found, or {@code null} if none exists
     * @throws SQLException if a database error occurs
     */
    public ASubmission getSubmissionByStudentAndAssignment(String studentId, String assignmentId) throws SQLException {
        String sql = "SELECT * FROM Submissions WHERE student_id = ? AND assignment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ps.setString(2, assignmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ASubmission(
                            rs.getString("id"),
                            assignmentId,
                            studentId,
                            rs.getString("file_path"),
                            rs.getString("timestamp")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Inserts a new submission or updates an existing one (upsert pattern).
     * <p>
     * If a submission for the given (assignment_id, student_id) already exists,
     * its file path and timestamp will be updated.
     *
     * @param s the {@link ASubmission} to insert or update
     * @throws SQLException if a database error occurs
     */
    public void upsertSubmission(ASubmission s) throws SQLException {
        String sql = """
        INSERT INTO Submissions(id, assignment_id, student_id, file_path, timestamp)
        VALUES(?,?,?,?,?)
        ON CONFLICT(assignment_id, student_id) DO UPDATE SET
            file_path = excluded.file_path,
            timestamp = excluded.timestamp
    """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getId());
            ps.setString(2, s.getAssignmentId());
            ps.setString(3, s.getStudentId());
            ps.setString(4, s.getFilePath());
            ps.setString(5, s.getTimestamp());
            ps.executeUpdate();
        }
    }
}
