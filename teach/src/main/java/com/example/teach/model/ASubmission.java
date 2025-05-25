package com.example.teach.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a submission made by a student for an assignment.
 * <p>
 * Stores information including submission ID, assignment ID, student ID,
 * file path, and timestamp. Includes JavaFX property methods for UI binding.
 */
public class ASubmission {
    private final String id;
    private final String assignmentId;
    private final String studentId;
    private final String filePath;
    private final String timestamp;

    /**
     * Constructs a new ASubmission instance.
     *
     * @param id           unique submission ID
     * @param assignmentId ID of the assignment being submitted to
     * @param studentId    ID of the student submitting
     * @param filePath     full file path to the submitted file
     * @param timestamp    submission timestamp (e.g., ISO format)
     */
    public ASubmission(String id, String assignmentId, String studentId, String filePath, String timestamp) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.filePath = filePath;
        this.timestamp = timestamp;
    }

    /**
     * Returns the submission ID.
     *
     * @return submission ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the assignment ID associated with this submission.
     *
     * @return assignment ID
     */
    public String getAssignmentId() {
        return assignmentId;
    }

    /**
     * Returns the student ID who made the submission.
     *
     * @return student ID
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Returns the file path of the submitted file.
     *
     * @return file path string
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Returns the timestamp of when the submission was made.
     *
     * @return timestamp string
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Property for the student name column in a TableView.
     * <p>
     * Currently returns a placeholder name derived from the student ID.
     *
     * @return StringProperty with display name
     */
    public StringProperty studentNameProperty() {
        // Replace this with actual mapping if available
        String displayName = "Student: " + studentId;
        return new SimpleStringProperty(displayName);
    }

    /**
     * Property for the file path column in a TableView.
     *
     * @return StringProperty with file path
     */
    public StringProperty filePathProperty() {
        return new SimpleStringProperty(filePath);
    }

    /**
     * Property for the timestamp column in a TableView.
     *
     * @return StringProperty with timestamp
     */
    public StringProperty timestampProperty() {
        return new SimpleStringProperty(timestamp);
    }
}
