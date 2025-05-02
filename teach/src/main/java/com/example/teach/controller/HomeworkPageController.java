// src/main/java/com/example/teach/controller/HomeWorkPageController.java
package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class HomeworkPageController implements SectionControllerBase {
    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    @FXML private Accordion accordion;

    @Override public void setUser(User user) {
        this.currentUser = user;
    }

    @Override public void setSubject(Subject subject) {
        this.currentSubject = subject;
        // you could dynamically rename your panes here, e.g.:
        accordion.getPanes().forEach(p ->
                p.setText(currentSubject.getName() + " / " + p.getText())
        );
    }

    @Override public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /** If you want a “home” button in your section: */
    @FXML private void onGoHome() {
        dashboardController.goToDashboard(null);  // or delegate back to subject home
    }
}
