package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GradeDAO handles CRUD operations for the Grades table.
 */
public class GradeDAO {
    private final Connection conn;

    public GradeDAO() {
        this.conn = SQliteConnection.getInstance();
    }

    /**
     * Inserts or updates a grade entry.
     */
    public boolean saveOrUpdateGrade(Grade grade) {
        String sql = """
            INSERT INTO Grades(id, assignment_id, student_id, grade, feedback, submitted_time)
            VALUES (?, ?, ?, ?, ?, ?)
            ON CONFLICT(assignment_id, student_id) DO UPDATE SET
                grade = excluded.grade,
                feedback = excluded.feedback,
                submitted_time = excluded.submitted_time
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            System.out.println("\uD83D\uDCDD Attempting to save grade:");
//            System.out.println("ID: " + grade.getId());
//            System.out.println("Assignment ID: " + grade.getAssignmentId());
//            System.out.println("Student ID: " + grade.getStudentId());
//            System.out.println("Grade: " + grade.getGrade());
//            System.out.println("Feedback: " + grade.getFeedback());
//            System.out.println("Submitted Time: " + grade.getSubmittedTime());

            ps.setString(1, grade.getId());
            ps.setString(2, grade.getAssignmentId());
            ps.setString(3, grade.getStudentId());
            ps.setString(4, grade.getGrade());
            ps.setString(5, grade.getFeedback());
            ps.setString(6, grade.getSubmittedTime());

            int result = ps.executeUpdate();
//            System.out.println(" Rows affected: " + result);
            return result > 0;

        } catch (SQLException e) {
            System.err.println(" SQL ERROR while saving grade:");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Grade getGradeByAssignmentAndStudent(String assignmentId, String studentId) {
        String sql = "SELECT * FROM Grades WHERE assignment_id = ? AND student_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignmentId);
            ps.setString(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToGrade(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all grades for a specific student.
     */
    public List<Grade> getGradesForStudent(String studentId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM Grades WHERE student_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                grades.add(mapRowToGrade(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    /**
     * Retrieves all grades for a specific assignment (useful for teachers).
     */
    public List<Grade> getGradesForAssignment(String assignmentId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM Grades WHERE assignment_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, assignmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                grades.add(mapRowToGrade(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grades;
    }

    /**
     * Converts a database row into a Grade object.
     */
    private Grade mapRowToGrade(ResultSet rs) throws SQLException {
        return new Grade(
                rs.getString("id"),
                rs.getString("assignment_id"),
                rs.getString("student_id"),
                rs.getString("grade"),
                rs.getString("feedback"),
                rs.getString("submitted_time")
        );
    }
}
