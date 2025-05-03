package com.example.teach.model;

/**
 * Represents a homework assignment for a specific subject.
 * <p>
 * Contains identifying information, descriptive details, and a due date.
 */
public class Homework {

    /** Unique identifier for this homework. */
    private final String id;

    /** Identifier of the subject to which this homework belongs. */
    private final String subjectId;

    /** Short title of the homework. */
    private final String title;

    /** Detailed description or instructions for the homework. */
    private final String description;

    /** Due date for the homework, formatted as YYYY-MM-DD or similar. */
    private final String dueDate;

    /**
     * Constructs a new Homework instance.
     *
     * @param id           unique homework ID
     * @param subjectId    ID of the associated subject
     * @param title        human-readable title of the homework
     * @param description  detailed description or instructions
     * @param dueDate      due date string (e.g., "2025-05-10")
     */
    public Homework(String id,
                    String subjectId,
                    String title,
                    String description,
                    String dueDate) {
        this.id = id;
        this.subjectId = subjectId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    /**
     * Returns the unique homework ID.
     *
     * @return homework ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the subject ID associated with this homework.
     *
     * @return subject ID
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * Returns the title of the homework.
     *
     * @return homework title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the detailed description or instructions for the homework.
     *
     * @return homework description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the due date for the homework.
     *
     * @return due date string
     */
    public String getDueDate() {
        return dueDate;
    }
}