package com.example.teach.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a submission made by a student for an assignment.
 */
public class ASubmission {
    private final String id;
    private final String assignmentId;
    private final String studentId;
    private final String filePath;
    private final String timestamp;

    public ASubmission(String id, String assignmentId, String studentId, String filePath, String timestamp) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.filePath = filePath;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // JavaFX Properties for TableView binding

    public StringProperty studentNameProperty() {
        // Replace this with actual mapping if available
        String displayName = "Student: " + studentId;
        return new SimpleStringProperty(displayName);
    }

    public StringProperty filePathProperty() {
        return new SimpleStringProperty(filePath);
    }

    public StringProperty timestampProperty() {
        return new SimpleStringProperty(timestamp);
    }
}
