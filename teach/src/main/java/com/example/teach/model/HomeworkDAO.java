package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing {@link Homework} records in the database.
 * <p>
 * Provides CRUD operations and utility methods to retrieve, add, update, and manage
 * homework entries associated with specific subjects.
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
    /**
     * Deletes a specific homework entry by its ID.
     *
     * @param id the unique identifier of the homework to delete
     * @throws SQLException if a database access error occurs
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM Homework WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
    protected Connection getConnection() {
        return conn;
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
    /**
     * Updates an existing homework record in the database.
     *
     * @param h the {@link Homework} object with updated values
     * @throws SQLException if a database access error occurs
     */
    public void update(Homework h) throws SQLException {
        String sql = "UPDATE Homework SET week = ?, title = ?, description = ?, due_date = ?, release_date = ?, open_date = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getWeek());
            ps.setString(2, h.getTitle());
            ps.setString(3, h.getDescription());
            ps.setString(4, h.getDueDate());
            ps.setString(5, h.getReleaseDate());
            ps.setString(6, h.getOpenDate());
            ps.setString(7, h.getId());
            ps.executeUpdate();
        }
    }
    /**
     * Releases a homework assignment by setting its release date to the current date.
     *
     * @param homeworkId the ID of the homework to release
     * @throws SQLException if a database access error occurs
     */
    public void releaseHomework(String homeworkId) throws SQLException {
        String sql = "UPDATE Homework SET release_date = date('now') WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, homeworkId);
            ps.executeUpdate();
        }
    }
    /**
     * Unreleases a homework assignment by setting its release date to NULL.
     *
     * @param homeworkId the ID of the homework to unrelease
     * @throws SQLException if a database access error occurs
     */
    public void unreleaseHomework(String homeworkId) throws SQLException {
        String sql = "UPDATE Homework SET release_date = NULL WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, homeworkId);
            ps.executeUpdate();
        }
    }
    /**
     * Retrieves all released homework assignments for a given subject.
     *
     * @param subjectId the ID of the subject whose released homework is to be fetched
     * @return a list of {@link Homework} entries with non-null release dates
     * @throws SQLException if a database access error occurs
     */
    public List<Homework> getReleasedBySubject(String subjectId) throws SQLException {
        List<Homework> out = new ArrayList<>();
        String sql = "SELECT * FROM Homework WHERE subject_id = ? AND release_date IS NOT NULL";
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
                            rs.getString("id")
                    );
                    out.add(hw);
                }
            }
        }
        return out;
    }

}