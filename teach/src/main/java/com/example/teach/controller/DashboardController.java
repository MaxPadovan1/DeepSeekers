package com.example.teach.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;


public class DashboardController {
    @FXML
    private VBox drawer;

    @FXML
    private void toggleDrawer() {
        boolean isVisible = drawer.isVisible();
        drawer.setVisible(!isVisible);
        drawer.setManaged(!isVisible);
    }
    @FXML
    private void goBack() {
        System.out.println("Back button clicked!");
    }

    @FXML
    private void showNotifications() {
        System.out.println("Notification button clicked!");
    }

    @FXML
    private Label lessonPlanLabel;

    @FXML
    public void initialize() {
        lessonPlanLabel.setOnMouseClicked(event -> openLessonPlan());
    }

    private void openLessonPlan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/LessonPlan-view.fxml"));
            Parent lessonPlanRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) lessonPlanLabel.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(lessonPlanRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleMouseEnter() {
        lessonPlanLabel.setStyle("-fx-font-size: 23px; -fx-text-fill: yellow; -fx-cursor: hand;");
    }

    @FXML
    private void handleMouseExit() {
        lessonPlanLabel.setStyle("-fx-font-size: 23px; -fx-text-fill: white; -fx-cursor: hand;");
    }

}


