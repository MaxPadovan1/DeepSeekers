package com.example.teach.model;
import java.util.List;

/**
 * Contains a list of weeks in which multiple tasks can be assigned
 */
public class Homework
{
    private final String id, subjectId, title, description, dueDate;

    public Homework(String id, String subjectId,
                    String title, String description,
                    String dueDate) {
        this.id          = id;
        this.subjectId   = subjectId;
        this.title       = title;
        this.description = description;
        this.dueDate     = dueDate;
    }

    public String getId()          { return id; }
    public String getSubjectId()   { return subjectId; }
    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public String getDueDate()     { return dueDate; }
}