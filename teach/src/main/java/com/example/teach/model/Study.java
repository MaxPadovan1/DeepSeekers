package com.example.teach.model;

/**
 * Represents study material for a specific subject.
 * <p>
 * Contains identifying information, a title, and content for study purposes.
 */
public class Study {

    /** Unique identifier for this study entry. */
    private final String id;

    /** Identifier of the subject to which this study entry belongs. */
    private final String subjectId;

    /** Title for the study entry. */
    private final String title;

    /** Detailed content for the study entry. */
    private final String content;

    /**
     * Constructs a new Study instance.
     *
     * @param id         unique study ID
     * @param subjectId  ID of the associated subject
     * @param title      title of the study content
     * @param content    detailed study content
     */
    public Study(String id,
                 String subjectId,
                 String title,
                 String content) {
        this.id = id;
        this.subjectId = subjectId;
        this.title = title;
        this.content = content;
    }

    /**
     * Returns the unique study ID.
     *
     * @return study ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the subject ID associated with this study entry.
     *
     * @return subject ID
     */
    public String getSubjectId() {
        return subjectId;
    }

    /**
     * Returns the title of this study entry.
     *
     * @return study title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the detailed content of this study entry.
     *
     * @return study content
     */
    public String getContent() {
        return content;
    }
}