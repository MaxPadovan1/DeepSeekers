package com.example.teach.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
}


