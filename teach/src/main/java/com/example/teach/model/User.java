package com.example.teach.model;

public abstract class User {
    private final String id;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String email;

    public User(String id, String password, String firstName, String lastName, String email) {
        this.id = id;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getId() { return id; }
    public String getPasswordHash() { return String.valueOf(password.hashCode()); }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
}

