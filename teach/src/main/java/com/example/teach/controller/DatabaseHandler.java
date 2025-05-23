package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.User;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseHandler {
    private static final Logger logger = Logger.getLogger(DatabaseHandler.class.getName());
    private Connection conn;

    public DatabaseHandler() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:users.db");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Users (id TEXT PRIMARY KEY, passwordHash TEXT, firstName TEXT, lastName TEXT, role TEXT, email TEXT)");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to database or creating table", e);
        }
    }

    public boolean exists(String id) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT id FROM Users WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error checking if user exists", e);
            return false;
        }
    }

    public boolean signUp(User u) {
        if (exists(u.getId())) return false;

        if (u instanceof Student student) {
            if (student.getSubjects() != null && student.getSubjects().size() > 4) return false;
        }

        String sql = "INSERT INTO Users(id,passwordHash,firstName,lastName,role,email) VALUES(?,?,?,?,?,?)";
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
            logger.log(Level.SEVERE, "Error inserting user during sign-up", ex);
            return false;
        }
    }
}


