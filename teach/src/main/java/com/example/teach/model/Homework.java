package com.example.teach.model;

public class Homework {

    private String id;
    private String subjectId;
    private String week;
    private String title;
    private String description;
    private String dueDate;
    private String releaseDate;
    private String openDate;

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
    public String getId() { return id; }
    public String getSubjectId() { return subjectId; }
    public String getWeek() { return week; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public String getReleaseDate() { return releaseDate; }
    public String getOpenDate() { return openDate; }

    // === Setters ===
    public void setId(String id) { this.id = id; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public void setWeek(String week) { this.week = week; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public void setOpenDate(String openDate) { this.openDate = openDate; }
}
