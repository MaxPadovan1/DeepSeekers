package com.example.teach.model;

/**
 * Abstract base class representing a generic task (homework, test, or assignment).
 * <p>
 * Encapsulates common properties such as details, instructions, release date,
 * due date, and weight (percentage).
 * Provides validation in setters to ensure correct value ranges.
 */
public class Task {

    /** Detailed description of the task. */
    private String details;

    /** Instructions for completing the task. */
    private String instructions;

    /** Release date of the task (e.g., "2025-05-10"). */
    private String releaseDate;

    /** Due date of the task (e.g., "2025-05-17"). */
    private String dueDate;

    /** Weight of the task as a percentage (0-100). */
    private double weight;

    /**
     * Constructs a new Task instance with the given properties.
     *
     * @param details      detailed description of the task
     * @param instruction  instructions for completing the task
     * @param releaseDate  release date string (e.g., "YYYY-MM-DD")
     * @param dueDate      due date string (e.g., "YYYY-MM-DD")
     * @param weight       weight percentage as a string (parsed to double)
     * @throws IllegalArgumentException if weight is not a number or out of range
     */
    public Task(String details,
                String instruction,
                String releaseDate,
                String dueDate,
                String weight) {
        setDetails(details);
        setInstruction(instruction);
        setReleaseDate(releaseDate);
        setDueDate(dueDate);
        setWeight(weight);
    }

    /**
     * Sets the detailed description of the task.
     *
     * @param details non-null description text
     */
    public void setDetails(String details) {
        // TODO: add input validation (e.g., non-empty)
        this.details = details;
    }

    /**
     * Sets the instructions for completing the task.
     *
     * @param instruction non-null instruction text
     */
    public void setInstruction(String instruction) {
        // TODO: add input validation (e.g., non-empty)
        this.instructions = instruction;
    }

    /**
     * Sets the release date of the task.
     *
     * @param releaseDate date string (format enforced externally)
     */
    public void setReleaseDate(String releaseDate) {
        // TODO: add date format validation
        this.releaseDate = releaseDate;
    }

    /**
     * Sets the due date of the task.
     *
     * @param dueDate date string (format enforced externally)
     */
    public void setDueDate(String dueDate) {
        // TODO: add date format validation
        this.dueDate = dueDate;
    }

    /**
     * Parses and sets the weight of the task.
     * Must be a numeric value between 0 and 100.
     *
     * @param weight string representation of percentage (0-100)
     * @throws IllegalArgumentException if parsing fails or value out of range
     */
    public void setWeight(String weight) {
        double parsed;
        try {
            parsed = Double.parseDouble(weight);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Weight must be a number: " + weight, e);
        }

        if (parsed < 0 || parsed > 100) {
            throw new IllegalArgumentException(
                    "Weight must be between 0 and 100: " + weight
            );
        }

        this.weight = parsed;
    }

    /**
     * Returns the task details description.
     *
     * @return details description
     */
    public String getDetails() {
        return details;
    }

    /**
     * Returns the task instructions.
     *
     * @return instruction text
     */
    public String getInstruction() {
        return instructions;
    }

    /**
     * Returns the release date of the task.
     *
     * @return release date string
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Returns the due date of the task.
     *
     * @return due date string
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Returns the weight of the task as a percentage.
     *
     * @return weight 0-100
     */
    public double getWeight() {
        return weight;
    }
}