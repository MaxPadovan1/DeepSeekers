package com.example.teach.model;

public class Student extends User
{
    public Student(String id, String passwordHash, String firstname, String lastName)
    {
        setId(id);
        setPasswordHash(passwordHash);
        setFirstName(firstname);
        setLastName(lastName);
    }

    @Override public void setId(String id)
    {
        if (id.startsWith("S")) {
            super.setId(id);
        }
    }
}