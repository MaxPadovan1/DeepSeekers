// SubjectService.java
package com.example.teach.model;

import java.sql.SQLException;
import java.util.List;

public class SubjectService {
    private final AssignmentDAO    assignmentDAO    = new AssignmentDAO();
    private final HomeworkDAO      homeworkDAO      = new HomeworkDAO();
    private final StudyDAO contentDAO       = new StudyDAO();

    public List<Assignment>    getAssignments(String subjectId) throws SQLException {
        return assignmentDAO.getBySubject(subjectId);
    }
    public List<Homework>      getHomeworks(String subjectId)   throws SQLException {
        return homeworkDAO.getBySubject(subjectId);
    }
    public List<Study> getContents(String subjectId)    throws SQLException {
        return contentDAO.getBySubject(subjectId);
    }
}
