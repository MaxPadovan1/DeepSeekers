// SubjectDAO.java
package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    private final Connection conn = SQliteConnection.getInstance();

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
}
