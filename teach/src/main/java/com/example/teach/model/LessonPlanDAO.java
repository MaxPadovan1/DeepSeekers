package com.example.teach.model;

import com.example.teach.model.LessonPlan;
import com.example.teach.model.SQliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LessonPlanDAO {
    private final Connection conn;

    public LessonPlanDAO() {
        this.conn = SQliteConnection.getInstance();
    }

    /**
     * Fetch all lesson plans in the database.
     */
    public List<LessonPlan> findAll() throws SQLException {
        String sql = "SELECT id, subject_id, title, details FROM LessonPlans";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<LessonPlan> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        }
    }

    /**
     * Fetch all lesson plans for a specific subject.
     */
    public List<LessonPlan> findBySubject(String subjectId) throws SQLException {
        String sql = "SELECT id, subject_id, title, details FROM LessonPlans WHERE subject_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, subjectId);
            try (ResultSet rs = stmt.executeQuery()) {
                List<LessonPlan> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        }
    }

    /**
     * Find a lesson plan by its ID.
     */
    public Optional<LessonPlan> findById(String id) throws SQLException {
        String sql = "SELECT id, subject_id, title, details FROM LessonPlans WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    /**
     * Insert a new lesson plan into the database.
     */
    public void save(LessonPlan lp) throws SQLException {
        String sql = "INSERT INTO LessonPlans (id, subject_id, title, details) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lp.getId());
            stmt.setString(2, lp.getSubjectId());
            stmt.setString(3, lp.getTitle());
            stmt.setString(4, lp.getDetails());
            stmt.executeUpdate();
        }
    }

    /**
     * Update an existing lesson plan.
     */
    public void update(LessonPlan lp) throws SQLException {
        String sql = "UPDATE LessonPlans SET subject_id = ?, title = ?, details = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lp.getSubjectId());
            stmt.setString(2, lp.getTitle());
            stmt.setString(3, lp.getDetails());
            stmt.setString(4, lp.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Delete a lesson plan by ID.
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM LessonPlans WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Map a result-set row to a LessonPlan object.
     */
    private LessonPlan mapRow(ResultSet rs) throws SQLException {
        return new LessonPlan(
                rs.getString("id"),
                rs.getString("subject_id"),
                rs.getString("title"),
                rs.getString("details")
        );
    }
}