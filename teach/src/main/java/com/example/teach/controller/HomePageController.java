package com.example.teach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class HomePageController {
    @FXML
    private VBox drawer;
    @FXML
    private Label subjectLabel;

    // This method will be called when a subject is selected
    public void setSubjectName(String subjectName) {
        subjectLabel.setText(subjectName);  // Set the subject name dynamically
    }

    @FXML
    private void toggleDrawer() {
        boolean isVisible = drawer.isVisible();
        drawer.setVisible(!isVisible);
        drawer.setManaged(!isVisible);
    }
    // Navigation method to return to the dashboard
    @FXML
    private void goBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) subjectLabel.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToStudyPage(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/view/StudyPage.fxml"));
            Parent studyRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(studyRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/LoginPage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToProfile(MouseEvent event) {
        switchToProfileScene(((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    private void goToProfile(ActionEvent event) {
        switchToProfileScene(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
    }

    private void switchToProfileScene(Window window) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/ProfilePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) window;
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


