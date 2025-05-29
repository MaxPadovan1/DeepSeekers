package com.example.teach.model;

/**
 * Represents a single lesson plan associated with a subject.
 * <p>
 * Each lesson plan includes a title and detailed content,
 * and is linked to a subject by its subject ID.
 */
public class LessonPlan
{
    /** Unique identifier for the lesson plan. */
    private String id;
    /** Identifier of the subject this lesson plan belongs to. */
    private String subjectId;
    /** Title or name of the lesson plan (e.g., "Week 1 - Introduction"). */
    private String title;
    /** Detailed content or description of the lesson plan. */
    private String details;
    /**
     * Constructs a new LessonPlan instance with the given values.
     *
     * @param id         unique lesson plan ID
     * @param subjectId  ID of the subject the lesson plan is tied to
     * @param title      title of the lesson plan
     * @param details    full lesson content or description
     */

    public LessonPlan(String id, String subjectId, String title, String details) {
        this.id         = id;
        this.subjectId  = subjectId;
        this.title      = title;
        this.details    = details;
    }

    // — Getters & Setters —
    /**
     * Returns the unique ID of the lesson plan.
     *
     * @return lesson plan ID
     */
    public String getId() {
        return id;
    }
    /**
     * Returns the subject ID this lesson plan belongs to.
     *
     * @return subject ID
     */
    public String getSubjectId() {
        return subjectId;
    }
    /**
     * Sets the subject ID this lesson plan belongs to.
     *
     * @param subjectId the new subject ID
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    /**
     * Returns the title of the lesson plan.
     *
     * @return lesson plan title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Sets the title of the lesson plan.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Returns the detailed content of the lesson plan.
     *
     * @return lesson plan details
     */
    public String getDetails() {
        return details;
    }
    /**
     * Sets the detailed content of the lesson plan.
     *
     * @param details the new lesson plan details
     */
    public void setDetails(String details) {
        this.details = details;
    }
}