package com.example.teach.model;
/**
 * Represents a student's grade for a particular assignment.
 * <p>
 * This model includes details such as assignment ID, student ID,
 * grade score, teacher feedback, and the submission timestamp.
 */
public class Grade {
    /** Unique identifier for this grade record. */
    private String id;
    /** Identifier for the assignment this grade is associated with. */
    private String assignmentId;
    /** Identifier of the student who received this grade. */
    private String studentId;
    /** The actual grade or score awarded (e.g., "A", "85%", etc.). */
    private String grade;
    /** Teacher's feedback or comments on the assignment submission. */
    private String feedback;
    /** Timestamp indicating when the assignment was submitted. */
    private String submittedTime;
    /**
     * Constructs a new Grade object with the given properties.
     *
     * @param id             unique grade record ID
     * @param assignmentId   ID of the assignment graded
     * @param studentId      ID of the student who submitted the assignment
     * @param grade          the grade or score awarded
     * @param feedback       optional feedback from the teacher
     * @param submittedTime  timestamp of submission
     */
    public Grade(String id, String assignmentId, String studentId, String grade, String feedback, String submittedTime) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.grade = grade;
        this.feedback = feedback;
        this.submittedTime = submittedTime;
    }

    /**
     * @return the unique ID of the grade
     */
    public String getId() {
        return id;
    }
    /**
     * @return the ID of the related assignment
     */
    public String getAssignmentId() {
        return assignmentId;
    }
    /**
     * @return the ID of the student receiving the grade
     */
    public String getStudentId() {
        return studentId;
    }
    /**
     * @return the grade or score awarded
     */
    public String getGrade() {
        return grade;
    }
    /**
     * @return the teacher's feedback for the assignment
     */
    public String getFeedback() {
        return feedback;
    }
    /**
     * @return the time the assignment was submitted
     */
    public String getSubmittedTime() {
        return submittedTime;
    }

    // Optionally add setters if needed
}
