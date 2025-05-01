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

    /**
     * Return the MAX(id) for entries starting with that prefix,
     * e.g. highest “S” student ID. Null if none.
     */
    public String findMaxIdWithPrefix(String prefix) {
        String sql = "SELECT id FROM Users WHERE id LIKE ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("id") : null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean signUp(User u) {
        if (exists(u.getId())) return false;

        // 👉 First, enforce business rules BEFORE inserting into database
        if (u instanceof Student student) {
            if (student.getSubjects() != null && student.getSubjects().size() > 4) {
                return false;  // too many subjects
            }
        }

        // 1) Insert into Users table
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

        // 2) Role-specific inserts
        try {
            if (u instanceof Student) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO Students(id) VALUES(?)")) {
                    ps.setString(1, u.getId());
                    ps.executeUpdate();
                }
                List<Subject> subs = ((Student)u).getSubjects();
                if (subs != null && !subs.isEmpty()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO StudentSubjects(student_id,subject_id) VALUES(?,?)")) {
                        for (Subject s : subs) {
                            ps.setString(1, u.getId());
                            ps.setString(2, s.getId());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }
            } else {
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
    /** Update firstName, lastName, email for a given user. ID is immutable. */
    public boolean updateProfile(User u) {
        String sql = "UPDATE Users SET firstName=?, lastName=?, email=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** Change password when user knows their old password. */
    public boolean changePassword(String id, String oldHash, String newHash) {
        String sql = "UPDATE Users SET passwordHash=? WHERE id=? AND passwordHash=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setString(2, id);
            ps.setString(3, oldHash);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /** Reset password via “forgot password” flow (verify by email). */
    public boolean resetPassword(String id, String email, String newHash) {
        String sql = "UPDATE Users SET passwordHash=? WHERE id=? AND email=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHash);
            ps.setString(2, id);
            ps.setString(3, email);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
