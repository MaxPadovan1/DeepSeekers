// SubjectDAO.java
package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    private final Connection conn = SQliteConnection.getInstance();
    private final SubjectDAO subjectDao = this;  // for reuse in these methods

    /** Fetch all subjects (for populating your UI). */
    public List<Subject> getAll() throws SQLException {
        List<Subject> out = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name FROM Subjects")) {
            while (rs.next()) {
                out.add(new Subject(rs.getString("id"), rs.getString("name")));
            }
        }
        return out;
    }

    /** Lookup one subject by its ID. */
    public Subject findById(String id) throws SQLException {
        String sql = "SELECT name FROM Subjects WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Subject(id, rs.getString("name"));
            }
        }
    }

    /** Lookup the teacher assigned to this subject (there is exactly one). */
    public Teacher findTeacherBySubject(String subjectId) throws SQLException {
        String sql =
                "SELECT u.id, u.passwordHash, u.firstName, u.lastName, u.email, t.subject_id " +
                        "FROM Teachers t " +
                        "  JOIN Users u ON t.id = u.id " +
                        "WHERE t.subject_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                // note: Teacher constructor is (id, pwHash, first, last, email, Subject)
                Subject subj = new Subject(rs.getString("subject_id"), null);
                // we only need id here, the Dashboard/Controller will fill the name
                Teacher t = new Teacher(
                        rs.getString("id"),
                        rs.getString("passwordHash"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        subj
                );
                return t;
            }
        }
    }

    /** Lookup all students enrolled in this subject. */
    public List<Student> findStudentsBySubject(String subjectId) throws SQLException {
        String sql =
                "SELECT u.id, u.passwordHash, u.firstName, u.lastName, u.email " +
                        "FROM StudentSubjects ss " +
                        "  JOIN Users u ON ss.student_id = u.id " +
                        "WHERE ss.subject_id = ?";
        List<Student> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Student constructor is (id, pwHash, first, last, email, List<Subject>)
                    // here we don't know their full 4-subject list, so just pass an empty list
                    Student s = new Student(
                            rs.getString("id"),
                            rs.getString("passwordHash"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("email"),
                            List.of()   // or you could do a separate lookup if you need all 4
                    );
                    out.add(s);
                }
            }
        }
        return out;
    }
}