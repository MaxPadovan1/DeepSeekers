package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing Study material records in the database.
 * <p>
 * Provides methods to retrieve study entries by subject and to add new study content.
 */
public class StudyDAO {

    /** Connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /**
     * Retrieves all study entries for the specified subject ID.
     *
     * @param subjectId the ID of the subject whose study entries to fetch
     * @return a list of {@link Study} objects for the subject
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Inserts a new study entry into the database.
     *
     * @param c the {@link Study} object to add
     * @throws SQLException if a database access error occurs
     */
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