package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

/**
 * Controller for the “subject home” panel that lives in the Dashboard's center.
 * All navigation is handed back to DashboardController.
 */
public class SubjectHomePageController {
    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboard;

    /** Inject the logged‐in user. */
    public void setUser(User user) {
        this.currentUser = user;
        // TODO: populate any user‐specific UI here if needed
    }

    /** Inject the current subject. */
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        // TODO: update your UI labels based on subject.getName(), etc.
    }

    /** Give us back‐pointer to the dashboard so we can ask it to swap views. */
    public void setDashboardController(DashboardController dash) {
        this.dashboard = dash;
    }

    /** “Study” tab: reload this same view. */
    @FXML private void goToStudyPage(MouseEvent ev) {
        dashboard.goToClassInfo(ev);   // re‐loads this page
    }

    @FXML private void goToAssignmentPage(MouseEvent ev) {
        dashboard.goToLessonPlan(ev);   // or your real assignment‐page method
    }

    @FXML private void goToGradePage(MouseEvent ev) {
        dashboard.goToLessonPlan(ev);   // swap in your “grade” view instead
    }

    @FXML private void goToHomeworkPage(MouseEvent ev) {
        dashboard.goToLessonPlan(ev);   // swap in your “homework” view instead
    }

    @FXML private void goToTestPage(MouseEvent ev) {
        dashboard.goToLessonPlan(ev);   // swap in your “test” view instead
    }
}
