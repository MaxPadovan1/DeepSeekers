package com.example.teach.model;

/**
 * Represents an academic subject in the system.
 * <p>
 * Each subject has a unique identifier and a human-readable name.
 */
public class Subject {

    /** Unique identifier for the subject (e.g., "CS102"). */
    private String id;

    /** Human-readable name of the subject (e.g., "Intro to Programming"). */
    private String name;

    /**
     * Constructs a new Subject instance.
     *
     * @param id   unique subject ID
     * @param name human-readable subject name
     */
    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the unique subject ID.
     *
     * @return subject ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the human-readable name of the subject.
     *
     * @return subject name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the subject for display purposes.
     * Overrides {@link Object#toString()} to return the subject name,
     * which is useful in UI components like ComboBox or ListView.
     *
     * @return subject name
     */
    @Override public String toString() {
        return name;
    }
}