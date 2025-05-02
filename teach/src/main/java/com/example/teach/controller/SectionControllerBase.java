package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;

/**
 * Common API for any “section” controller that lives
 * in the SubjectHomePage center pane.
 */
public interface SectionControllerBase
{
    void setUser(User user);
    void setSubject(Subject subject);
    void setDashboardController(DashboardController dashboardController);
}