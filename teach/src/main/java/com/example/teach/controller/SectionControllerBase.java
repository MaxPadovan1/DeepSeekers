package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;

/**
 * Common API for any "section" controller displayed in the SubjectHomePage center pane.
 * <p>
 * Allows injection of the current user, current subject, and a reference to the DashboardController
 * for navigation and UI context updates.
 */
public interface SectionControllerBase {

    /**
     * Injects the authenticated user into the section controller.
     *
     * @param user the currently logged-in {@link User}
     */
    void setUser(User user);

    /**
     * Injects the subject context for this section controller.
     *
     * @param subject the {@link Subject} being viewed
     */
    void setSubject(Subject subject);

    /**
     * Injects the DashboardController for cross-controller navigation and UI updates.
     *
     * @param dashboardController reference to the parent {@link DashboardController}
     */
    void setDashboardController(DashboardController dashboardController);
}