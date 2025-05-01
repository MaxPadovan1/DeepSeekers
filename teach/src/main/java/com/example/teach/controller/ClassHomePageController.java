package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ClassHomePageController
{
    private User currentUser;
    private Subject currentSubject;

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        // TODO: update your UI labels/fields here
    }

    @FXML private void toggleDrawer(ActionEvent actionEvent) {
    }

    @FXML private void handleLogout(ActionEvent actionEvent) {
    }

    @FXML private void goToDashboard(MouseEvent mouseEvent) {
    }

    @FXML private void goToStudyPage(MouseEvent mouseEvent) {
    }
}
