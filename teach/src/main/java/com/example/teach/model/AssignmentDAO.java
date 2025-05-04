package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing Assignment records in the database.
 * <p>
 * Provides methods to retrieve assignments by subject and to add new assignments.
 */
public class AssignmentDAO {

    /** Connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /**
     * Fetches all assignments for the given subject ID.
     *
     * @param subjectId the ID of the subject whose assignments to retrieve
     * @return a list of {@link Assignment} objects belonging to the subject
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Inserts a new assignment record into the database.
     *
     * @param a the {@link Assignment} object to add
     * @throws SQLException if a database access error occurs
     */
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