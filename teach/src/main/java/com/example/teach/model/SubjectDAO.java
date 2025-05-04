package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing {@link Subject} entities.
 * <p>
 * Provides methods to retrieve all subjects, find subjects by ID,
 * and lookup the teacher or students associated with a subject.
 */
public class SubjectDAO {

    /** Shared JDBC connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /**
     * Retrieves all subjects from the database.
     * <p>
     * Typically used to populate UI components such as ComboBox or ListView.
     *
     * @return a list of all {@link Subject} records
     * @throws SQLException if a database access error occurs
     */
    public List<Subject> getAll() throws SQLException {
        List<Subject> out = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name FROM Subjects")) {
            while (rs.next()) {
                out.add(new Subject(
                        rs.getString("id"),
                        rs.getString("name")
                ));
            }
        }
        return out;
    }

    /**
     * Looks up a single subject by its unique ID.
     *
     * @param id the subject ID to search for
     * @return a {@link Subject} if found, or null if no matching record exists
     * @throws SQLException if a database access error occurs
     */
    public Subject findById(String id) throws SQLException {
        String sql = "SELECT name FROM Subjects WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Subject(
                        id,
                        rs.getString("name")
                );
            }
        }
    }

    /**
     * Finds the teacher assigned to a given subject.
     * <p>
     * Joins the Teachers and Users tables to construct a {@link Teacher} object.
     *
     * @param subjectId the ID of the subject
     * @return a {@link Teacher} if one is assigned, or null if none exists
     * @throws SQLException if a database access error occurs
     */
    public Teacher findTeacherBySubject(String subjectId) throws SQLException {
        String sql =
                "SELECT u.id, u.passwordHash, u.firstName, u.lastName, u.email, t.subject_id " +
                        "FROM Teachers t " +
                        "JOIN Users u ON t.id = u.id " +
                        "WHERE t.subject_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Subject subj = new Subject(subjectId, null);
                return new Teacher(
                        rs.getString("id"),
                        rs.getString("passwordHash"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        subj
                );
            }
        }
    }

    /**
     * Retrieves all students enrolled in a given subject.
     * <p>
     * Joins StudentSubjects and Users tables to construct {@link Student} objects.
     *
     * @param subjectId the ID of the subject
     * @return a list of {@link Student} objects enrolled in the subject
     * @throws SQLException if a database access error occurs
     */
    public List<Student> findStudentsBySubject(String subjectId) throws SQLException {
        String sql =
                "SELECT u.id, u.passwordHash, u.firstName, u.lastName, u.email " +
                        "FROM StudentSubjects ss " +
                        "JOIN Users u ON ss.student_id = u.id " +
                        "WHERE ss.subject_id = ?";
        List<Student> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Student(
                            rs.getString("id"),
                            rs.getString("passwordHash"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("email"),
                            List.of()  // enrolled subjects can be fetched separately
                    ));
                }
            }
        }
        return out;
    }
}