package com.example.teach.model;
import java.util.List;

public abstract class User
{
    private String Id;
    private String PasswordHash;

    private String FirstName;
    private String LastName;

    private List<Subject> Subjects;



    public void setId(String id)
    {
        this.Id = id;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.PasswordHash = passwordHash;
    }

    public void setFirstName(String firstName)
    {
        this.FirstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.LastName = lastName;
    }
}