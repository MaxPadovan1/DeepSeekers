package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Study section. Implements the common SectionControllerBase
 * so Dashboard/SubjectHomePage injects User & Subject correctly.
 */
public class StudyPageController implements SectionControllerBase, Initializable {
    private DashboardController dashboardController;
    private User currentUser;
    private Subject currentSubject;

    @Override public void initialize(URL location, ResourceBundle resources) {
        // any initialization goes here
    }

    @Override public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    @Override public void setUser(User u) {
        this.currentUser = u;
    }

    @Override public void setSubject(Subject s) {
        this.currentSubject = s;
    }

    // add any @FXML handlers you need for upload/edit buttons, etc.
}
