package com.example.teach.model;

import java.util.List;

/**
 * Represents a student user in the system.
 * <p>
 * Extends {@link User} and includes a list of enrolled {@link Subject}s.
 */
public class Student extends User {

    /** List of subjects in which the student is enrolled. */
    private List<Subject> subjects;

    /**
     * Constructs a new Student instance.
     *
     * @param id            unique student ID, must start with 'S'
     * @param passwordHash  hashed password value
     * @param firstName     student's first name
     * @param lastName      student's last name
     * @param email         student's email address
     * @param subjects      list of enrolled subjects
     * @throws IllegalArgumentException if the provided ID does not start with 'S'
     */
    public Student(String id,
                   String passwordHash,
                   String firstName,
                   String lastName,
                   String email,
                   List<Subject> subjects) {
        super(id, passwordHash, firstName, lastName, email);
        this.subjects = subjects;
    }

    /**
     * Sets the student ID. Overrides {@link User#setId(String)} to enforce prefix 'S'.
     *
     * @param id the new ID, which must start with 'S'
     * @throws IllegalArgumentException if the ID is null or does not start with 'S'
     */
    @Override public void setId(String id) {
        if (id != null && id.startsWith("S")) {
            super.setId(id);
        } else {
            throw new IllegalArgumentException("Student ID must start with 'S'");
        }
    }

    /**
     * Returns the list of subjects the student is enrolled in.
     *
     * @return list of {@link Subject} objects
     */
    public List<Subject> getSubjects() {
        return subjects;
    }

    /**
     * Updates the student's enrolled subjects.
     *
     * @param subjects new list of subjects
     */
    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}