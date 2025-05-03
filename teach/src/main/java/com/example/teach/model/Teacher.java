package com.example.teach.model;

/**
 * Represents a teacher user in the system.
 * <p>
 * Extends {@link User} and includes an assigned {@link Subject}.
 * Enforces ID prefix 'T' for teacher identifiers.
 */
public class Teacher extends User {

    /** The subject that this teacher is responsible for. */
    private Subject subject;

    /**
     * Constructs a new Teacher instance.
     *
     * @param id            unique teacher ID, must start with 'T'
     * @param passwordHash  hashed password value
     * @param firstName     teacher's first name
     * @param lastName      teacher's last name
     * @param email         teacher's email address
     * @param subject       the {@link Subject} assigned to this teacher
     * @throws IllegalArgumentException if the provided ID does not start with 'T'
     */
    public Teacher(String id,
                   String passwordHash,
                   String firstName,
                   String lastName,
                   String email,
                   Subject subject) {
        super(id, passwordHash, firstName, lastName, email);
        this.subject = subject;
    }

    /**
     * Sets the teacher ID. Overrides {@link User#setId(String)} to enforce prefix 'T'.
     *
     * @param id the new ID, which must start with 'T'
     * @throws IllegalArgumentException if the ID is null or does not start with 'T'
     */
    @Override public void setId(String id) {
        if (id != null && id.startsWith("T")) {
            super.setId(id);
        } else {
            throw new IllegalArgumentException("Teacher ID must start with 'T'");
        }
    }

    /**
     * Returns the subject that the teacher is responsible for.
     *
     * @return assigned {@link Subject}
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * Updates the assigned subject for this teacher.
     *
     * @param subject new {@link Subject} assignment
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}