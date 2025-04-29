package com.example.teach.model;

public class Teacher extends User
{
    private Subject subject;
    public Teacher(String id, String passwordHash, String firstname, String lastName, String email)
    {
        setId(id);
        setPasswordHash(passwordHash);
        setFirstName(firstname);
        setLastName(lastName);
    }

    @Override public void setId(String id)
    {
        if (id.startsWith("T")) {
            super.setId(id);
        }
    }
}