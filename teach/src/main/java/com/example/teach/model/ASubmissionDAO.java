package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database operations related to assignment submissions.
 */
public class ASubmissionDAO {

    private final Connection conn = SQliteConnection.getInstance();

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
