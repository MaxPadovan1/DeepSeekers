package com.example.teach.model;
/**
 * Represents a homework assignment associated with a subject and week.
 * <p>
 * This class encapsulates the homework's metadata including title, description,
 * due dates, and visibility windows (release and open dates).
 */
public class Homework {
    /** Unique identifier for the homework entry. */
    private String id;
    /** ID of the subject this homework belongs to. */
    private String subjectId;
    /** Week associated with the homework (e.g., "Week 1"). */
    private String week;
    /** Title of the homework, shown in lists and headers. */
    private String title;
    /** Full description or instruction set for the homework. */
    private String description;
    /** Due date in ISO format (e.g., "2025-05-31"). */
    private String dueDate;
/** Date on which the homework becomes visible to students. */
    private String releaseDate;
    /** Date on which the homework becomes open for submissions. */
    private String openDate;
    /**
     * Constructs a new {@code Homework} object.
     *
     * @param subjectId    ID of the subject
     * @param week         The academic week (e.g., "Week 1")
     * @param title        Title of the homework
     * @param description  Description or content of the homework
     * @param dueDate      The due date for submission
     * @param releaseDate  When the homework is released (optional)
     * @param openDate     When submissions can begin (optional)
     * @param id           Unique ID (can be generated with UUID)
     */
    public Homework(String subjectId,
                    String week,
                    String title,
                    String description,
                    String dueDate,
                    String releaseDate,
                    String openDate,
                    String id) {
        this.subjectId = subjectId;
        this.week = week;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.releaseDate = releaseDate;
        this.openDate = openDate;
        this.id = id;
    }

    // === Getters ===
    /** @return the homework ID */
    public String getId() { return id; }
    /** @return the subject ID this homework is linked to */
    public String getSubjectId() { return subjectId; }
    /** @return the week label for this homework */
    public String getWeek() { return week; }
    /** @return the homework title */
    public String getTitle() { return title; }
    /** @return the full description or instructions */
    public String getDescription() { return description; }
    /** @return the due date of the homework */
    public String getDueDate() { return dueDate; }
    /** @return the date the homework is released to students */
    public String getReleaseDate() { return releaseDate; }
    /** @return the date the homework becomes open for submissions */
    public String getOpenDate() { return openDate; }

    // === Setters ===
    /** @param id the unique homework ID to set */
    public void setId(String id) { this.id = id; }
    /** @param subjectId the subject ID to associate with this homework */
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    /** @param week the academic week label to set */
    public void setWeek(String week) { this.week = week; }
    /** @param title the title to set */
    public void setTitle(String title) { this.title = title; }
    /** @param description the homework instructions to set */
    public void setDescription(String description) { this.description = description; }
    /** @param dueDate the due date (ISO format) to set */
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    /** @param releaseDate the release date to set */
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    /** @param openDate the date homework becomes open for submissions */
    public void setOpenDate(String openDate) { this.openDate = openDate; }
}
