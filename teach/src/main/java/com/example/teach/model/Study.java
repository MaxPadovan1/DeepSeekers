package com.example.teach.model;

public class Study {
    private final String id, subjectId, title, content;

    public Study (String id, String subjectId,
                         String title, String content) {
        this.id        = id;
        this.subjectId = subjectId;
        this.title     = title;
        this.content   = content;
    }

    public String getId()        { return id; }
    public String getSubjectId() { return subjectId; }
    public String getTitle()     { return title; }
    public String getContent()   { return content; }
}
