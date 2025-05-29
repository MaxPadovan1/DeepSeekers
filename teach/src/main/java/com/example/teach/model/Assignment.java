package com.example.teach.model;

/**
 * Represents an academic assignment belonging to a specific subject.
 * <p>
 * Contains identifying information, descriptive details, and a due date.
 */
public class Assignment {

    /** Unique identifier for this assignment. */
    private final String id;

    /** Identifier of the subject to which this assignment belongs. */
    private final String subjectId;

    /** Short title of the assignment. */
    private final String title;

    /** Detailed description or instructions for the assignment. */
    private final String description;

    /** Due date for the assignment, formatted as YYYY-MM-DD or similar. */
    private final String dueDate;

    private final boolean isReleased;

    /**
     * Constructs a new Assignment instance.
     *
     * @param id           unique assignment ID
     * @param subjectId    ID of the associated subject
     * @param title        human-readable title of the assignment
     * @param description  detailed description or instructions
     * @param dueDate      due date string (e.g., "2025-05-10")
     */
    public Assignment(String id, String subjectId, String title, String description, String dueDate, boolean isReleased) {
        this.id = id;
        this.subjectId = subjectId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isReleased = isReleased;
    }

    /**
     * Returns the unique assignment ID.
     *
     * @return assignment ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the subject ID associated with this assignment.
     *
     * @return subject ID
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * Returns the title of the assignment.
     *
     * @return assignment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the detailed description or instructions for the assignment.
     *
     * @return assignment description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the due date for the assignment.
     *
     * @return due date string
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Returns whether the assignment has been released to students.
     *
     * @return true if released, false otherwise
     */
    public boolean isReleased() {
        return isReleased;
    }

    /**
     * Returns a string representation of the assignment for UI display.
     *
     * @return formatted title and due date string
     */
    @Override
    public String toString() {
        return title + " (Due: " + dueDate + ")";
    }

}