package com.example.teach.model;
import com.example.teach.TempBackendTesting.MockDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class User
{
    private String Id;
    private String PasswordHash;
    private String FirstName;
    private String LastName;
    private String Email;

    public void   setId(String id) { this.Id = id; }
    public String getId() { return Id; }

    public void   setPasswordHash(String passwordHash) { this.PasswordHash = passwordHash; }
    public String getPasswordHash() { return PasswordHash; }

    public void setFirstName(String firstName) { this.FirstName = firstName; }
    public String getFirstName() { return FirstName; }

    public void setLastName(String lastName) { this.LastName = lastName; }

    /**
     * Look up a user by credentials.
     * Returns a Student or Teacher if found, or null on bad creds.
     */
    public static User login(String id, String passwordHash) {
        return new UserDAO().findByCredentials(id, passwordHash);
    }

    /**
     * Attempt to register a new Student or Teacher.
     * Returns the new User on success, or null if the ID is invalid
     * (wrong prefix) or already exists.
     */
    public static User signUp(String id,
                              String passwordHash,
                              String firstName,
                              String lastName,
                              String email) {
        // 1) enforce prefix
        char prefix = Character.toUpperCase(id.charAt(0));
        User newUser;
        if (prefix == 'S') {
            newUser = new Student(id, passwordHash, firstName, lastName, email);
        } else if (prefix == 'T') {
            newUser = new Teacher(id, passwordHash, firstName, lastName, email);
        } else {
            return null;
        }

        // 2) delegate the insert
        boolean created = new UserDAO().signUp(newUser);
        return created ? newUser : null;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public String getLastName() {
        return LastName;
    }



}