package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for retrieving student-related data.
 */
public class StudentDAO {
    private final Connection conn;

    public StudentDAO() {
        this.conn = SQliteConnection.getInstance();
    }

    /**
     * Finds all students who are enrolled in the given subject.
     *
     * @param subjectId the subject ID to filter by
     * @return list of Student objects
     */
    public List<Student> getStudentsBySubject(String subjectId) {
        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT s.id, s.passwordHash, s.firstName, s.lastName, s.email
                FROM Students s
                JOIN StudentSubjects ss ON s.id = ss.student_id
                WHERE ss.subject_id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student student = new Student(
                        rs.getString("id"),
                        rs.getString("passwordHash"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        new ArrayList<>()  // Empty subject list by default
                );
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
}
