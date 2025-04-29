package com.example.teach.model;
import java.util.List;

public class Assignment
{
    private final String id, subjectId, title, description, dueDate;

    public Assignment(String id, String subjectId,
                      String title, String description,
                      String dueDate) {
        this.id         = id;
        this.subjectId  = subjectId;
        this.title      = title;
        this.description = description;
        this.dueDate    = dueDate;
    }

    public String getId()          { return id; }
    public String getSubjectId()   { return subjectId; }
    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public String getDueDate()     { return dueDate; }
}
