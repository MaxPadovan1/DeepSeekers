package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing User persistence and retrieval.
 * <p>
 * Provides methods for user authentication, registration, profile updates,
 * password management, and lookup of users by credentials or ID patterns.
 */
public class UserDAO {

    /** Shared JDBC Connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /** DAO for retrieving Subject entities when constructing Users. */
    private final SubjectDAO subjectDao = new SubjectDAO();

    /**
     * Authenticates a user by ID and password hash.
     * <p>
     * If credentials match a student, returns a {@link Student} with enrolled subjects;
     * if a teacher, returns a {@link Teacher} with their assigned subject.
     *
     * @param id           the user ID to authenticate
     * @param passwordHash the SHA-256 hash of the user's password
     * @return a {@link User} (Student or Teacher) on success, or null if authentication fails
     */
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
                char role = rs.getString("role").charAt(0);

                if (role == 'S') {
                    // Load student subjects
                    List<Subject> subs = new ArrayList<>();
                    String subSql = "SELECT subject_id FROM StudentSubjects WHERE student_id=?";
                    try (PreparedStatement ps2 = conn.prepareStatement(subSql)) {
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
                    // Load teacher subject
                    Subject subj = null;
                    String teachSql = "SELECT subject_id FROM Teachers WHERE id=?";
                    try (PreparedStatement ps3 = conn.prepareStatement(teachSql)) {
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

    public User findByUserId(String id) {
        String sql = "SELECT passwordHash, firstName, lastName, role, email FROM Users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String passwordHash = rs.getString("passwordHash");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String role = rs.getString("role");
                String email = rs.getString("email");

                if (role.equals("S")) {
                    // 加载学生科目
                    List<Subject> subs = new ArrayList<>();
                    String subSql = "SELECT subject_id FROM StudentSubjects WHERE student_id = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(subSql)) {
                        ps2.setString(1, id);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while (rs2.next()) {
                                Subject s = subjectDao.findById(rs2.getString("subject_id"));
                                if (s != null) subs.add(s);
                            }
                        }
                    }
                    return new Student(id, passwordHash, firstName, lastName, email, subs);
                } else {
                    // 加载教师科目
                    Subject subject = null;
                    String sql2 = "SELECT subject_id FROM Teachers WHERE id = ?";
                    try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                        ps2.setString(1, id);
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            if (rs2.next()) {
                                subject = subjectDao.findById(rs2.getString("subject_id"));
                            }
                        }
                    }
                    return new Teacher(id, passwordHash, firstName, lastName, email, subject);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }





    /**
     * Finds the maximum user ID matching a given prefix, e.g. highest student or teacher ID.
     *
     * @param prefix the character prefix to filter IDs (e.g., "S" or "T")
     * @return the maximum ID string, or null if none found
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

    /**
     * Registers a new user (Student or Teacher) in the database.
     * <p>
     * Enforces unique ID, subject count constraints for students (<=4),
     * and single-teacher-per-subject for teachers.
     *
     * @param u the {@link User} instance to persist
     * @return true if registration succeeds; false otherwise
     */
    public boolean signUp(User u) {
        if (exists(u.getId())) return false;

        // Pre-insert business rules
        if (u instanceof Student student && student.getSubjects() != null && student.getSubjects().size() > 4) {
            return false;
        }

        // Insert into Users table
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

        // Role-specific inserts
        try {
            if (u instanceof Student) {
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Students(id) VALUES(?)")) {
                    ps.setString(1, u.getId());
                    ps.executeUpdate();
                }
                List<Subject> subs = ((Student) u).getSubjects();
                if (subs != null) {
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
                Subject s = ((Teacher) u).getSubject();
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

    /**
     * Checks whether a user ID already exists in the database.
     *
     * @param id the user ID to check
     * @return true if the ID exists; false otherwise
     */
    public boolean exists(String id) {
        String sql = "SELECT 1 FROM Users WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a user's profile information (first name, last name, email).
     * ID and password remain unchanged.
     *
     * @param u the {@link User} with updated profile fields
     * @return true if the update affected exactly one record; false otherwise
     */
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

    /**
     * Changes a user's password given the old hash and new hash.
     *
     * @param id      the user ID
     * @param oldHash the current stored password hash
     * @param newHash the new password hash to set
     * @return true if the password was updated; false otherwise
     */
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

    /**
     * Resets a user's password using email verification.
     *
     * @param id      the user ID
     * @param email   the user's registered email
     * @param newHash the new password hash
     * @return true if the password was updated; false otherwise
     */
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