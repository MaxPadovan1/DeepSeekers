package com.example.teach.model;

import java.util.List;

public class Student extends User
{

    private List<Subject> subjects;

    public Student(String id,
                   String passwordHash,
                   String firstName,
                   String lastName,
                   String email,
                   List<Subject> subjects) {
        super(id, passwordHash, firstName, lastName, email);
        this.subjects = subjects;
    }

    @Override public void setId(String id)
    {
        if (id != null && id.startsWith("S")) {
            super.setId(id);
        }
        else {
            throw new IllegalArgumentException("Student ID must start with 'S'");
        }
    }
    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

}