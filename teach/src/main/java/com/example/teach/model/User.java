package com.example.teach.model;
import com.example.teach.TempBackendTesting.MockDB;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class User {
    private String Id;
    private final static int ID_DIGIT_SIZE = 8;
    private String PasswordHash;
    private String FirstName;
    private String LastName;
    private List<Subject> Subjects;

    public void setId(String id) {
        this.Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setPasswordHash(String passwordHash) { this.PasswordHash = passwordHash; }
    public String getPasswordHash() { return PasswordHash; }

    public void setFirstName(String firstName) { this.FirstName = firstName; }
    public String getFirstName() { return FirstName; }

    public void setLastName(String lastName) { this.LastName = lastName; }
    public String getLastName() { return LastName; }

    public static Optional<User> login(String id, String password, MockDB db) {
        System.out.println("\n ----- Sign in button pressed ----- ");
        System.out.printf("login() called with id='%s', password='%s'%n", id, password);

        // 1) Null-checks
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(password, "password must not be null");
        Objects.requireNonNull(db, "db must not be null");

        // 2) ID format: one letter + N digits
        final int EXPECTED_LENGTH = ID_DIGIT_SIZE + 1;
        System.out.printf("-> ID length is %d, expected %d%n", id.length(), EXPECTED_LENGTH);
        if (id.length() != EXPECTED_LENGTH) {
            System.out.println("-> Wrong ID length");
            return Optional.empty();
        }

        char prefix = id.charAt(0);
        System.out.println("-> Prefix char = " + prefix);
        if (prefix != 'S' && prefix != 'T') {
            System.out.println("-> Prefix not S or T");
            return Optional.empty();
        }

        String digits = id.substring(1);
        System.out.println("-> Digits part = " + digits);
        if (!digits.matches("\\d{" + ID_DIGIT_SIZE + "}")) {
            System.out.println("-> Digits don’t match expected pattern");
            return Optional.empty();
        }

        // 3) Look up the user in the correct collection
        List<? extends User> pool = (prefix == 'S') ? db.getStudents() : db.getTeachers();
        System.out.println("-> Pool size = " + pool.size());

        // 6) Find & check password
        for (User u : pool) {
            System.out.println("   checking user: " + u.getId());
            if (u.getId().equals(id)) {
                System.out.println("   -> Found ID; stored pw = " + u.getPasswordHash());
                if (u.getPasswordHash().equals(password)) {
                    System.out.println("   -> Password match!");
                    return Optional.of(u);
                } else {
                    System.out.println("   -> Password mismatch");
                    return Optional.empty();
                }
            }
        }

        System.out.println("-> No user with that ID");
        return Optional.empty();
    }
}