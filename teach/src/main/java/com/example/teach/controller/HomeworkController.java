package com.example.teach.controller;

import com.example.teach.model.Homework;
import com.example.teach.model.HomeworkDAO;

import java.sql.SQLException;
import java.util.List;

public class HomeworkController {

    private final HomeworkDAO homeworkDAO;

    public HomeworkController() {
        this.homeworkDAO = new HomeworkDAO();
    }

    public List<Homework> getHomeworksForSubject(String subjectId) throws SQLException {
        return homeworkDAO.getBySubject(subjectId);
    }

    public void addHomework(Homework hw) throws SQLException {
        homeworkDAO.add(hw);
    }

    public void deleteHomework(String id) throws SQLException {
        homeworkDAO.delete(id);
    }
}