package com.example.teach.model;

import java.sql.SQLException;
import java.util.List;

/**
 * Service layer for retrieving study materials associated with a subject.
 * <p>
 * Aggregates multiple DAOs (AssignmentDAO, HomeworkDAO, StudyDAO) to provide
 * a unified API for fetching assignments, homeworks, and study contents by subject ID.
 */
public class SubjectService {

    /** DAO for accessing assignment data. */
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();

    /** DAO for accessing homework data. */
    private final HomeworkDAO homeworkDAO = new HomeworkDAO();

    /** DAO for accessing study content data. */
    private final StudyDAO contentDAO = new StudyDAO();

    /**
     * Retrieves all assignments for the specified subject.
     *
     * @param subjectId the ID of the subject to fetch assignments for
     * @return a list of {@link Assignment} objects
     * @throws SQLException if a database access error occurs
     */
    public List<Assignment> getAssignments(String subjectId) throws SQLException {
        return assignmentDAO.getBySubject(subjectId);
    }

    /**
     * Retrieves all homework entries for the specified subject.
     *
     * @param subjectId the ID of the subject to fetch homeworks for
     * @return a list of {@link Homework} objects
     * @throws SQLException if a database access error occurs
     */
    public List<Homework> getHomeworks(String subjectId) throws SQLException {
        return homeworkDAO.getBySubject(subjectId);
    }

    /**
     * Retrieves all study content entries for the specified subject.
     *
     * @param subjectId the ID of the subject to fetch study contents for
     * @return a list of {@link Study} objects
     * @throws SQLException if a database access error occurs
     */
    public List<Study> getContents(String subjectId) throws SQLException {
        return contentDAO.getBySubject(subjectId);
    }
}