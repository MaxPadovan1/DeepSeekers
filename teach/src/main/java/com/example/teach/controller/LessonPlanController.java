package com.example.teach.controller;

import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LessonPlanController implements SectionControllerBase, Initializable {

    private User currentUser;
    private DashboardController dashboardController;

    @FXML private Accordion weekAccordion;
    @FXML private Button removeWeekButton;

    private int weekCount = 0;

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
