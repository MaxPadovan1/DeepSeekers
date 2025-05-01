package com.example.teach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LessonPlanController {
    @FXML
    private VBox drawer;
    @FXML
    private Label dash;

    @FXML
    private void toggleDrawer() {
        boolean isVisible = drawer.isVisible();
        drawer.setVisible(!isVisible);
        drawer.setManaged(!isVisible);
    }

    @FXML
    private Button studyButton, homeworkButton, assignmentButton, testButton, gradeButton;

    @FXML
    private TextArea week1TextArea, week2TextArea, week3TextArea, week4TextArea, week5TextArea;

    @FXML
    private void handleButtonClick(ActionEvent event) {
        if (event.getSource() == studyButton) {
            showAlert("Study Button Clicked", "You clicked on Study!");
        } else if (event.getSource() == homeworkButton) {
            showAlert("HomeWork Button Clicked", "You clicked on HomeWork!");
        } else if (event.getSource() == assignmentButton) {
            showAlert("Assignment Button Clicked", "You clicked on Assignment!");
        } else if (event.getSource() == testButton) {
            showAlert("Test Button Clicked", "You clicked on Test!");
        } else if (event.getSource() == gradeButton) {
            showAlert("Grade Button Clicked", "You clicked on Grade!");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleMouseEnter() {
        dash.setStyle("-fx-font-size: 23px; -fx-text-fill: yellow; -fx-cursor: hand;");
    }

    @FXML
    private void handleMouseExit() {
        dash.setStyle("-fx-font-size: 23px; -fx-text-fill: white; -fx-cursor: hand;");
    }
    @FXML
    private void handleAddLessonPlan() {
        System.out.println("Add Lesson Plan button clicked!");
        // Later you can pop up a dialog or open a new view here
    }
    @FXML
    public void initialize() {
        dash.setOnMouseClicked(event -> openDashboard());
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent dashBoardRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) dash.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(dashBoardRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleDashboardClick(ActionEvent event) {
        // This method will navigate back to the Dashboard screen.
        // You can use the scene or stage to switch views.
        try {
            // Load the Dashboard FXML file (replace with your actual FXML path)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent dashboardRoot = loader.load();

            // Get the current stage (the window in which your application is running)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene to the dashboard scene
            stage.setScene(new Scene(dashboardRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }





}

