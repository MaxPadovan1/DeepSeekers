package com.example.teach.model;

import com.example.teach.model.Grade;
import com.example.teach.model.SQliteConnection;

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
        createGradeTable();
    }

    /**
     * Creates the Grades table if it doesn't exist.
     */
    private void createGradeTable() {
        try (Statement stmt = conn.createStatement()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts or updates a grade entry.
     */
    public boolean saveOrUpdateGrade(Grade grade) {
        String sql = "INSERT OR REPLACE INTO Grades(id, assignment_id, student_id, grade, feedback, submitted_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, grade.getId());
            ps.setString(2, grade.getAssignmentId());
            ps.setString(3, grade.getStudentId());
            ps.setString(4, grade.getGrade());
            ps.setString(5, grade.getFeedback());
            ps.setString(6, grade.getSubmittedTime());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
