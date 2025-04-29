// UserDAO.java
package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private final Connection conn = SQliteConnection.getInstance();
    private final SubjectDAO subjectDao = new SubjectDAO();

    /** LOGIN: loads Student (with all subjects) or Teacher (with one). */
    public User findByCredentials(String id, String passwordHash) {
        String sql = "SELECT firstName,lastName,role,email FROM Users WHERE id=? AND passwordHash=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String fn = rs.getString("firstName");
                String ln = rs.getString("lastName");
                String em = rs.getString("email");
                char   role = rs.getString("role").charAt(0);

                if (role == 'S') {
                    // load up to 4 subjects
                    List<Subject> subs = new ArrayList<>();
                    try (PreparedStatement ps2 = conn.prepareStatement(
                            "SELECT subject_id FROM StudentSubjects WHERE student_id=?")) {
                        ps2.setString(1, id);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while (rs2.next()) {
                                Subject s = subjectDao.findById(rs2.getString("subject_id"));
                                if (s != null) subs.add(s);
                            }
                        }
                    }
                    return new Student(id, passwordHash, fn, ln, em, subs);
                } else {
                    // load single teacher subject
                    Subject subj = null;
                    try (PreparedStatement ps3 = conn.prepareStatement(
                            "SELECT subject_id FROM Teachers WHERE id=?")) {
                        ps3.setString(1, id);
                        try (ResultSet rs3 = ps3.executeQuery()) {
                            if (rs3.next()) {
                                subj = subjectDao.findById(rs3.getString("subject_id"));
                            }
                        }
                    }
                    return new Teacher(id, passwordHash, fn, ln, em, subj);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /** SIGN-UP: inserts into Users + Students/Teachers + StudentSubjects */
    public boolean signUp(User u) {
        if (exists(u.getId())) return false;

        // 1) Users table
        String insU = "INSERT INTO Users(id,passwordHash,firstName,lastName,role,email) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(insU)) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getFirstName());
            ps.setString(4, u.getLastName());
            ps.setString(5, (u instanceof Student ? "S" : "T"));
            ps.setString(6, u.getEmail());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

        // 2) Role-specific tables
        try {
            if (u instanceof Student) {
                // Students table
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Students(id) VALUES(?)")) {
                    ps.setString(1, u.getId());
                    ps.executeUpdate();
                }
                // StudentSubjects join
                List<Subject> subs = ((Student)u).getSubjects();
                if (subs.size() > 4) return false;
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO StudentSubjects(student_id,subject_id) VALUES(?,?)")) {
                    for (Subject s : subs) {
                        ps.setString(1, u.getId());
                        ps.setString(2, s.getId());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            } else {
                // Teachers table
                Subject s = ((Teacher)u).getSubject();
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Teachers(id,subject_id) VALUES(?,?)")) {
                    ps.setString(1, u.getId());
                    ps.setString(2, s.getId());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /** Helper to check for duplicate IDs */
    public boolean exists(String id) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Users WHERE id=?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
