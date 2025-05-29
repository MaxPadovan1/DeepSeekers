package com.example.teach.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Grade} class.
 */
public class GradeTest {

    @Test
    public void testGradeConstructorAndGetters() {
        // Arrange
        String id = "G-001";
        String assignmentId = "A-123";
        String studentId = "S-456";
        String gradeValue = "A+";
        String feedback = "Excellent work!";
        String submittedTime = "2025-05-29T10:00:00";

        // Act
        Grade grade = new Grade(id, assignmentId, studentId, gradeValue, feedback, submittedTime);

        // Assert
        assertEquals(id, grade.getId());
        assertEquals(assignmentId, grade.getAssignmentId());
        assertEquals(studentId, grade.getStudentId());
        assertEquals(gradeValue, grade.getGrade());
        assertEquals(feedback, grade.getFeedback());
        assertEquals(submittedTime, grade.getSubmittedTime());
    }
}

