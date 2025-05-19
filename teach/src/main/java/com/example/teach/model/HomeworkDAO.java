package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing Homework records in the database.
 * <p>
 * Provides methods to retrieve homework assignments by subject and to add new homework records.
 */
public class HomeworkDAO {

    /** Connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /**
     * Retrieves all homework entries for the specified subject ID.
     *
     * @param subjectId the ID of the subject whose homework to fetch
     * @return a list of {@link Homework} objects for the subject
     * @throws SQLException if a database access error occurs
     */
    public List<Homework> getBySubject(String subjectId) throws SQLException {
        List<Homework> out = new ArrayList<>();
        String sql = "SELECT id, week, title, description, due_date, release_date, open_date FROM Homework WHERE subject_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Homework hw = new Homework(
                            subjectId,
                            rs.getString("week"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getString("due_date"),
                            rs.getString("release_date"),
                            rs.getString("open_date"),
                            rs.getString("id")  // 最后是 ID
                    );
                    out.add(hw);
                }
            }
        }
        return out;
    }


    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM Homework WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Inserts a new homework record into the database.
     *
     * @param h the {@link Homework} object to add
     * @throws SQLException if a database access error occurs
     */
    public void add(Homework h) throws SQLException {
        String sql = "INSERT INTO Homework(subject_id, week, title, description, due_date, release_date, open_date,id) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(8,h.getId());
            ps.setString(1, h.getSubjectId());
            ps.setString(2, h.getWeek());
            ps.setString(3, h.getTitle());
            ps.setString(4, h.getDescription());
            ps.setString(5, h.getDueDate());
            ps.setString(6, h.getReleaseDate());
            ps.setString(7, h.getOpenDate());
            ps.executeUpdate();
        }
    }
}