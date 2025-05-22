package com.example.teach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.teach.model.Subject;
import com.example.teach.model.User;

import java.io.IOException;

public class TestPageController implements SectionControllerBase{


    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    // Injected by SubjectHomePageController
    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
    }

    @Override
    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }
    public void onRemoveAssignment(ActionEvent actionEvent) {
    }

    public void onAddAssignment(ActionEvent actionEvent) {
    }

    public void onEditAssignment(ActionEvent actionEvent) {
    }

    public void onSaveEditedAssignment(ActionEvent actionEvent) {
    }

    public void onReleaseAssignment(ActionEvent actionEvent) {
    }

    public void onAssignmentSelected(ActionEvent actionEvent) {
    }




}
