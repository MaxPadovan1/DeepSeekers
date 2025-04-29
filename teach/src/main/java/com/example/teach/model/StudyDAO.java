// StudyDAO.java
package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudyDAO {
    private final Connection conn = SQliteConnection.getInstance();

    public List<Study> getBySubject(String subjectId) throws SQLException {
        List<Study> out = new ArrayList<>();
        String sql = "SELECT id,title,content FROM Study WHERE subject_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Study(
                            rs.getString("id"),
                            subjectId,
                            rs.getString("title"),
                            rs.getString("content")
                    ));
                }
            }
        }
        return out;
    }

    public void add(Study c) throws SQLException {
        String sql = "INSERT INTO Study(id,subject_id,title,content) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getId());
            ps.setString(2, c.getSubjectId());
            ps.setString(3, c.getTitle());
            ps.setString(4, c.getContent());
            ps.executeUpdate();
        }
    }
}

