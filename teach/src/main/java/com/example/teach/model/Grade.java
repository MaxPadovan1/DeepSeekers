package com.example.teach.model;

public class Grade {
    private String id;
    private String assignmentId;
    private String studentId;
    private String grade;
    private String feedback;
    private String submittedTime;

    public Grade(String id, String assignmentId, String studentId, String grade, String feedback, String submittedTime) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.grade = grade;
        this.feedback = feedback;
        this.submittedTime = submittedTime;
    }

    public String getId() {
        return id;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getGrade() {
        return grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getSubmittedTime() {
        return submittedTime;
    }

    // Optionally add setters if needed
}
