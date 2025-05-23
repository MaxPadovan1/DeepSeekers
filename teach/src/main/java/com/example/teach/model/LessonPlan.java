package com.example.teach.model;

public class LessonPlan
{
    private String id;
    private String subjectId;
    private String title;
    private String details;

    public LessonPlan(String id, String subjectId, String title, String details) {
        this.id         = id;
        this.subjectId  = subjectId;
        this.title      = title;
        this.details    = details;
    }

    // — Getters & Setters —
    public String getId() {
        return id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}