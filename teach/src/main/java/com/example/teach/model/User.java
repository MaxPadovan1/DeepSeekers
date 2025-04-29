package com.example.teach.model;
import com.example.teach.TempBackendTesting.MockDB;

import java.util.List;

public abstract class User
{
    private String Id;
    private String PasswordHash;
    private String FirstName;
    private String LastName;
    private List<Subject> Subjects;

    public void   setId(String id) { this.Id = id; }
    public String getId() { return Id; }

    public void   setPasswordHash(String passwordHash) { this.PasswordHash = passwordHash; }
    public String getPasswordHash() { return PasswordHash; }

    public void setFirstName(String firstName) { this.FirstName = firstName; }
    public String getFirstName() { return FirstName; }

    public void setLastName(String lastName) { this.LastName = lastName; }

    public static User Login(String id, String password, MockDB db)
    {
        // Guard: ID must be at least 2 characters (one prefix letter + at least one digit)
        if (id == null || id.length() < 2) { return null; }

        // Extract the first character as the “prefix” (should be 'S' or 'T')
        char prefix = id.charAt(0);

        // Get the rest of the string after the prefix; must be numeric
        String numericPart = id.substring(1);

        // If the suffix contains anything other than digits, reject immediately
        if (!numericPart.matches("\\d+")) { return null; }

        if (prefix == 'S')
        {
            // Search only students when the ID starts with 'S'
            for (Student s : db.students) {
                // Check full ID match and password match
                if (s.getId().equals(id) && s.getPasswordHash().equals(password)) { return s; }
            }
        }
        else if (prefix == 'T')
        {
            // Search only teachers when the ID starts with 'T'
            for (Teacher t : db.teachers) {
                // Check full ID match and password match
                if (t.getId().equals(id) && t.getPasswordHash().equals(password)) { return t; }
            }
        }
        // If no matching user found or prefix was invalid, return null
        return null;
    }
}