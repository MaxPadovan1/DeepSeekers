package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;

/**
 * Controller for the "Homework" section of a subject.
 * <p>
 * Implements {@link SectionControllerBase} to allow dependency injection of
 * the current user, subject, and dashboard controller. Manages an Accordion
 * UI component to display homework-related content.
 */
public class HomeworkPageController implements SectionControllerBase {

    /** The authenticated user viewing this section. */
    private User currentUser;
    /** The subject context for this Homework page. */
    private Subject currentSubject;
    /** Parent DashboardController for navigation and UI control. */
    private DashboardController dashboardController;
    /** Accordion UI container for organizing homework panes. */
    @FXML private Accordion accordion;

    /**
     * Injects the authenticated user into this controller.
     *
     * @param user the logged-in {@link User}
     */
    @Override public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Injects the current subject into this controller.
     * Updates each titled pane's text to include the subject context.
     *
     * @param subject the {@link Subject} being viewed
     */
    @Override public void setSubject(Subject subject) {
        this.currentSubject = subject;
        // Prefix each pane title with the subject name
        accordion.getPanes().forEach(pane ->
                pane.setText(currentSubject.getName() + " / " + pane.getText())
        );
    }

    /**
     * Injects the DashboardController for navigation actions.
     *
     * @param dash the parent {@link DashboardController}
     */
    @Override public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /**
     * Handler for the "Home" button in the Homework section.
     * Navigates back to the dashboard or subject home view.
     */
    @FXML private void onGoHome() {
        dashboardController.goToDashboard(null);
    }
}