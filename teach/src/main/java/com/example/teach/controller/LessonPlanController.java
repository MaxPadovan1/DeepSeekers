package com.example.teach.controller;

import com.example.teach.model.User;
import javafx.fxml.Initializable;

public class LessonPlanController implements SectionControllerBase, Initializable {

    private User currentUser;
    private DashboardController dashboardController;


    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    @Override
    public void setSubject(com.example.teach.model.Subject subject) {
        // Optional future implementation
    }
}