package com.example.teach.model;

public class Teacher extends User
{
    public Teacher(String id, String passwordHash, String firstname, String lastName)
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