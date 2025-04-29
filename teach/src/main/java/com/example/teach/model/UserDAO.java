package com.example.teach.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn;

    public UserDAO() {
        // Ensure the SQLite connection is initialized
        this.conn = SQliteConnection.getInstance();
    }

    /**
     * Attempt to find a user by their credentials.
     *
     * @param id            The user's ID.
     * @param passwordHash  The hashed password.
     * @return              A Student or Teacher instance on success, null otherwise.
     */
    public User findByCredentials(String id, String passwordHash) {
        String sql =
                "SELECT firstName, lastName, role, email " +
                        "FROM Users " +
                        "WHERE id = ? AND passwordHash = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    // No matching row
                    return null;
                }

                String firstName = rs.getString("firstName");
                String lastName  = rs.getString("lastName");
                String email     = rs.getString("email");
                char   role      = rs.getString("role").charAt(0);

                if (role == 'S') {
                    return new Student(id, passwordHash, firstName, lastName, email);
                } else if (role == 'T') {
                    return new Teacher(id, passwordHash, firstName, lastName, email);
                } else {
                    // Unknown role
                    return null;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Check whether a user ID already exists in the database.
     *
     * @param id  The user ID to check.
     * @return    True if the ID exists, false otherwise.
     */
    public boolean exists(String id) {
        String sql = "SELECT 1 FROM Users WHERE id = ?";
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
     * Register a new user (Student or Teacher).
     *
     * @param u  The User object to insert.
     * @return   True on successful insert, false if the ID already exists or an error occurs.
     */
    public boolean signUp(User u) {
        // Prevent duplicate IDs
        if (exists(u.getId())) {
            return false;
        }

        String sql =
                "INSERT INTO Users " +
                        "(id, passwordHash, firstName, lastName, role, email) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getId());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getFirstName());
            ps.setString(4, u.getLastName());
            ps.setString(5, (u instanceof Student ? "S" : "T"));
            ps.setString(6, u.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

