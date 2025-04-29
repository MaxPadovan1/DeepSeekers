
package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {
    private final Connection conn = SQliteConnection.getInstance();

    /** Fetch all assignments for a given subject */
    public List<Assignment> getBySubject(String subjectId) throws SQLException {
        List<Assignment> out = new ArrayList<>();
        String sql = "SELECT id,title,description,due_date FROM Assignments WHERE subject_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Assignment(
                            rs.getString("id"),
                            subjectId,
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("due_date")
                    ));
                }
            }
        }
        return out;
    }

    /** (Optional) Add a new assignment */
    public void add(Assignment a) throws SQLException {
        String sql = "INSERT INTO Assignments(id,subject_id,title,description,due_date) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getId());
            ps.setString(2, a.getSubjectId());
            ps.setString(3, a.getTitle());
            ps.setString(4, a.getDescription());
            ps.setString(5, a.getDueDate());
            ps.executeUpdate();
        }
    }
}

