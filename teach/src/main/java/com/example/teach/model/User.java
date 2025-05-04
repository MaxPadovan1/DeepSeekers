package com.example.teach.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all users in the DeepSeekers system (students and teachers).
 * <p>
 * Encapsulates common properties such as ID, password hash, first name, last name, and email.
 * Provides authentication and registration logic, as well as password hashing utility.
 */
public abstract class User {

    private String id;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Constructs a new User with the given credentials and personal information.
     *
     * @param id            unique user identifier (prefix enforced by subclass)
     * @param passwordHash  SHA-256 hash of the user's password
     * @param firstName     user's first name
     * @param lastName      user's last name
     * @param email         user's email address
     */
    public User(String id,
                String passwordHash,
                String firstName,
                String lastName,
                String email) {
        setId(id);
        setPasswordHash(passwordHash);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
    }

    /** Sets the unique ID for this user. Subclasses may enforce prefix rules. */
    public void setId(String id) {
        this.id = id;
    }

    /** Retrieves the user's unique ID. */
    public String getId() {
        return id;
    }

    /** Sets the stored password hash for this user. */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /** Retrieves the stored password hash for this user. */
    public String getPasswordHash() {
        return passwordHash;
    }

    /** Sets the user's first name. */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /** Retrieves the user's first name. */
    public String getFirstName() {
        return firstName;
    }

    /** Sets the user's last name. */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** Retrieves the user's last name. */
    public String getLastName() {
        return lastName;
    }

    /** Sets the user's email address. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Retrieves the user's email address. */
    public String getEmail() {
        return email;
    }

    /**
     * Attempts to authenticate a user with the given ID and password hash.
     *
     * @param id            user ID
     * @param passwordHash  SHA-256 hash of the provided password
     * @return a {@link User} instance (Student or Teacher) if authentication succeeds; null otherwise
     */
    public static User login(String id, String passwordHash) {
        return new UserDAO().findByCredentials(id, passwordHash);
    }

    /**
     * Registers a new user in the system, enforcing role-based rules:
     * students must supply exactly 4 subjects; teachers exactly 1.
     *
     * @param id            new user ID (must start with 'S' or 'T')
     * @param passwordHash  SHA-256 hash of the password
     * @param firstName     user's first name
     * @param lastName      user's last name
     * @param email         user's email address
     * @param subjectIds    list of subject IDs (size 4 for students, 1 for teachers)
     * @return the created {@link User} (Student or Teacher) on success; null on failure
     */
    public static User signUp(String id,
                              String passwordHash,
                              String firstName,
                              String lastName,
                              String email,
                              List<String> subjectIds) {
        char prefix = Character.toUpperCase(id.charAt(0));
        try {
            if (prefix == 'S') {
                if (subjectIds.size() != 4) return null;
                List<Subject> subjects = new ArrayList<>();
                for (String sid : subjectIds) {
                    Subject s = new SubjectDAO().findById(sid);
                    if (s == null) return null;
                    subjects.add(s);
                }
                Student stu = new Student(id, passwordHash, firstName, lastName, email, subjects);
                return new UserDAO().signUp(stu) ? stu : null;
            } else if (prefix == 'T') {
                if (subjectIds.size() != 1) return null;
                String sid = subjectIds.get(0);
                Subject subj = new SubjectDAO().findById(sid);
                if (subj == null) return null;
                if (new SubjectDAO().findTeacherBySubject(sid) != null) return null;
                Teacher tch = new Teacher(id, passwordHash, firstName, lastName, email, subj);
                return new UserDAO().signUp(tch) ? tch : null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Hashes the given plain-text password using SHA-256 and returns its hex-encoded digest.
     *
     * @param password the plain-text password to hash
     * @return hex-encoded SHA-256 digest
     * @throws RuntimeException if SHA-256 algorithm is not available
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Converts a byte array to its hex string representation.
     *
     * @param bytes array of bytes
     * @return hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Returns a string representation including user details and role-specific info.
     *
     * @return formatted string for Student or Teacher
     */
    @Override
    public String toString() {
        String base = String.format("id=%s, name=%s %s, email=%s",
                getId(), getFirstName(), getLastName(), getEmail());
        if (this instanceof Student s) {
            List<String> ids = s.getSubjects().stream().map(Subject::getId).toList();
            return "[Student " + base + ", subjects=" + ids + "]";
        } else if (this instanceof Teacher t) {
            String sid = t.getSubject() != null ? t.getSubject().getId() : "none";
            return "[Teacher " + base + ", subject=" + sid + "]";
        }
        return "[User " + base + "]";
    }
}