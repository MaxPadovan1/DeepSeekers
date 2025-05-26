package com.example.teach.model;

import java.util.List;

/**
 * Represents a week of coursework, containing one or more tasks (assignments, homework, etc.).
 * <p>
 * Each Week instance aggregates a list of {@link Task} objects for scheduling and display.
 */
public class Week {

    /**
     * The list of tasks scheduled for this week.
     */
    private List<Task> tasks;

    /**
     * Constructs a new Week with the given list of tasks.
     *
     * @param tasks the list of {@link Task} instances for this week
     */
    public Week(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Returns the tasks scheduled for this week.
     *
     * @return list of {@link Task} objects
     */
    public List<Task> getTasks() {
        return tasks;
    }


    private int number;

    /**
     * Constructs a Week object with a specific week number.
     * Tasks can be assigned later using {@link #setTasks(List)}.
     *
     * @param number the number representing the week (e.g., 1 for Week 1)
     */
    public Week(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    /**
     * Returns a human-readable label for the week, such as "Week 1".
     *
     * @return string representation of the week
     */
    @Override
    public String toString() {
        return "Week " + number;
    }

    /**
     * Sets or updates the list of tasks for this week.
     *
     * @param tasks new list of {@link Task} instances
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}