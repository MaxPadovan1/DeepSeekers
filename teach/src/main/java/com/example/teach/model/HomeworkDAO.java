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
        String sql = "SELECT id,title,description,due_date FROM Homeworks WHERE subject_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Homework(
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

    /**
     * Inserts a new homework record into the database.
     *
     * @param h the {@link Homework} object to add
     * @throws SQLException if a database access error occurs
     */
    public void add(Homework h) throws SQLException {
        String sql = "INSERT INTO Homeworks(id,subject_id,title,description,due_date) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getId());
            ps.setString(2, h.getSubjectId());
            ps.setString(3, h.getTitle());
            ps.setString(4, h.getDescription());
            ps.setString(5, h.getDueDate());
            ps.executeUpdate();
        }
    }
}