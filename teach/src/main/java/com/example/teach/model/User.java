package com.example.teach.model;

import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class User
{
    private String Id;
    private String PasswordHash;
    private String FirstName;
    private String LastName;
    private String Email;

    public User(String id, String passwordHash, String firstName, String lastName, String email) {
        setId(id);
        setPasswordHash(passwordHash);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    public void   setId(String id) { this.Id = id; }
    public String getId() { return Id; }

    public void   setPasswordHash(String passwordHash) { this.PasswordHash = passwordHash; }
    public String getPasswordHash() { return PasswordHash; }

    public void setFirstName(String firstName) { this.FirstName = firstName; }
    public String getFirstName() { return FirstName; }

    public void setLastName(String lastName) { this.LastName = lastName; }

    // -------------------
    // Authentication
    // -------------------

    /**
     * Lookup a user by ID & passwordHash.
     * Returns a Student (with all assigned subjects)
     * or a Teacher (with their single subject), or null.
     */
    public static User login(String id, String passwordHash) {
        return new UserDAO().findByCredentials(id, passwordHash);
    }

    /**
     * Sign-up:
     *  - Students supply up to 4 subjectIds
     *  - Teachers supply exactly 1 subjectId
     */
    public static User signUp(String id,
                              String passwordHash,
                              String firstName,
                              String lastName,
                              String email,
                              List<String> subjectIds) {
        char p = Character.toUpperCase(id.charAt(0));
        try {
            if (p == 'S') {
                if (subjectIds.size() != 4) return null;
                // resolve to Subject objects
                List<Subject> subs = new ArrayList<>();
                for (String sid : subjectIds) {
                    Subject s = new SubjectDAO().findById(sid);
                    if (s == null) return null; // invalid ID
                    subs.add(s);
                }
                Student stu = new Student(id, passwordHash, firstName, lastName, email, subs);
                return new UserDAO().signUp(stu) ? stu : null;
            }
            else if (p == 'T') {
                if (subjectIds.size() != 1) return null;
                Subject s = new SubjectDAO().findById(subjectIds.get(0));
                if (s == null) return null;
                Teacher tch = new Teacher(id, passwordHash, firstName, lastName, email, s);
                return new UserDAO().signUp(tch) ? tch : null;
            }
            else {
                return null; // bad prefix
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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

    /**
     * Hashes the given plain‐text password with SHA-256.
     * Returns the hex‐encoded digest.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[]   bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override public String toString() {
        String base = String.format("id=%s, name=%s %s, email=%s",
                getId(), getFirstName(), getLastName(), getEmail());

        if (this instanceof Student s) {
            // List out all subject IDs for a student
            List<String> ids = s.getSubjects().stream()
                    .map(Subject::getId)
                    .toList();
            return "[Student " + base +
                    ", subjects=" + ids + "]";
        }
        else if (this instanceof Teacher t) {
            // Show the single subject for a teacher
            String subj = t.getSubject() != null ? t.getSubject().getId() : "none";
            return "[Teacher " + base +
                    ", subject=" + subj + "]";
        }
        else {
            return "[User " + base + "]";
        }
    }
}