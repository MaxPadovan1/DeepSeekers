package com.example.teach.model;

public class Teacher extends User
{
    private Subject subject;

    public Teacher(String id,
                   String passwordHash,
                   String firstName,
                   String lastName,
                   String email,
                   Subject subject) {
        super(id, passwordHash, firstName, lastName, email);
        this.subject = subject;
    }

    @Override public void setId(String id)
    {
        if (id != null && id.startsWith("T")) {
            super.setId(id);
        }else {
            throw new IllegalArgumentException("Teacher ID must start with 'T'");
        }
    }
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}