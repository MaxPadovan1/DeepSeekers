package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for retrieving and managing student-related data from the database.
 * <p>
 * Provides methods to retrieve students enrolled in a subject or by their ID.
 */
public class StudentDAO {
    private final Connection conn;

    /**
     * Constructs a new StudentDAO with a connection to the SQLite database.
     */
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
                SELECT u.id, u.passwordHash, u.firstName, u.lastName, u.email
                FROM Students s
                JOIN StudentSubjects ss ON s.id = ss.student_id
                JOIN Users u ON u.id = s.id
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

    /**
     * Retrieves a student by their unique user ID.
     * <p>
     * Uses a join between the Users and Students tables to verify student status.
     *
     * @param id the ID of the student to retrieve
     * @return the corresponding {@link Student} object, or {@code null} if not found
     */
    public Student getStudentById(String id) {
        String sql = """
            SELECT u.id, u.passwordHash, u.firstName, u.lastName, u.email
            FROM Students s
            JOIN Users u ON u.id = s.id
            WHERE u.id = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getString("id"),
                        rs.getString("passwordHash"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        new ArrayList<>()  // no subject list used here
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
