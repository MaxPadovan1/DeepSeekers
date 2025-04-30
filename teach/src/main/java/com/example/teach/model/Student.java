package com.example.teach.model;

import java.util.List;

public class Student extends User {
    private List<String> subjects;

    public Student(String id, String password, String firstName, String lastName, String email) {
        super(id, password, firstName, lastName, email);
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getSubjects() {
        return subjects;
    }
}
