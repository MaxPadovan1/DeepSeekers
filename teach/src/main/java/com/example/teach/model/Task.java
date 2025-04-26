package com.example.teach.model;

/**
 * A base class that provides the structure for each homework, test, and assignment instances
 */
public class Task
{
    private String Details;
    private String Instrucions;
    private String ReleaseDate;
    private String DueDate;
    private double Weight;

    public Task(String details, String instruction, String releaseDate, String dueDate, String weight)
    {
        setDetails(details);
        setInstruction(instruction);
        setReleaseDate(releaseDate);
        setWeight(weight);
    }

    public void setDetails(String details)
    {
        //TODO: add safety checks
        this.Details = details;
    }

    public void setInstruction(String instruction)
    {
        //TODO: add safety checks
        this.Instrucions = instruction;
    }

    public void setReleaseDate(String releaseDate)
    {
        //TODO: add safety checks
        this.ReleaseDate = releaseDate;
    }

    public void setDueDate(String dueDate)
    {
        //TODO: add safety checks
        this.DueDate = dueDate;
    }

    public void setWeight(String weight)
    {
        //TODO: add safety checks
        double parsed;
        try { parsed = Double.parseDouble(weight); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Weight must be a number: " + weight + " ", e);
        }

        if (parsed < 0 || parsed > 100)
        {
            throw new IllegalArgumentException("Weight: " + weight + " must be between 0 and 100");
        }

        this.Weight = parsed;
    }
}