package com.example.teach.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing Week entities and their relations.
 * <p>
 * Provides methods to retrieve weeks for a subject and to insert or update weeks.
 */
public class WeekDAO {

    /** JDBC connection to the SQLite database. */
    private final Connection conn = SQliteConnection.getInstance();

    /** DAO for loading Study entries associated with a week. */
    private final StudyDAO studyDAO = new StudyDAO();

    /**
     * Retrieves all weeks (with their Study entries) for the given subject ID.
     *
     * @param subjectId the subject ID whose weeks to fetch
     * @return list of {@link Week} objects populated with their studies
     * @throws SQLException if a database access error occurs
     */
    public List<Week> getBySubject(String subjectId) throws SQLException {
        List<Week> weeks = new ArrayList<>();
        String sql = "SELECT id, name FROM Weeks WHERE subject_id = ? ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String weekId = rs.getString("id");
                    String weekName = rs.getString("name");
                    List<Study> studies = studyDAO.getByWeek(weekId);
                    weeks.add(new Week(weekId, weekName, studies));
                }
            }
        }
        return weeks;
    }

    /**
     * Inserts a new week record into the database.
     *
     * @param week the {@link Week} to insert
     * @throws SQLException if a database access error occurs
     */
    public void insert(Week week) throws SQLException {
        String sql = "INSERT INTO Weeks(id, subject_id, name) VALUES(?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, week.getId());
            // assume Week stores subjectId via getStudies first element
            // or add subjectId property to Week
            throw new UnsupportedOperationException("Week.subjectId must be supplied or stored in Week model.");
        }
    }

    /**
     * Updates the name of an existing week.
     *
     * @param week the {@link Week} to update
     * @throws SQLException if a database access error occurs
     */
    public void update(Week week) throws SQLException {
        String sql = "UPDATE Weeks SET name = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, week.getName());
            ps.setString(2, week.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Saves or updates the given week: if it exists, update it; otherwise insert it.
     *
     * @param week the {@link Week} to save or update
     * @throws SQLException if a database access error occurs
     */
    public void saveOrUpdate(Week week) throws SQLException {
        if (exists(week.getId())) {
            update(week);
        } else {
            String sql = "INSERT INTO Weeks(id, subject_id, name) VALUES(?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, week.getId());
                // Using the subject_id from the first Study entry or from a separate property
                if (week.getStudies().isEmpty()) {
                    throw new IllegalArgumentException("Week must have at least one Study to infer subject_id");
                }
                ps.setString(2, week.getStudies().get(0).getSubjectId());
                ps.setString(3, week.getName());
                ps.executeUpdate();
            }
        }
    }

    /**
     * Checks if a Week with the given ID exists in the database.
     *
     * @param weekId the Week ID to check
     * @return true if exists, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean exists(String weekId) throws SQLException {
        String sql = "SELECT 1 FROM Weeks WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, weekId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void delete(String weekId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Weeks WHERE id = ?")) {
            ps.setString(1, weekId);
            ps.executeUpdate();
        }
        // (optionally) delete orphaned Study rows:
        conn.createStatement().execute("DELETE FROM Study WHERE week_id = '" + weekId + "'");
    }
}
