package com.example.teach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class LessonPlanController {
    @FXML
    private VBox drawer;

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

        System.out.println("Mouse entered!");
    }
    @FXML
    private void handleMouseExit() {

        System.out.println("Mouse exited!");
    }
    @FXML
    private void handleAddLessonPlan() {
        System.out.println("Add Lesson Plan button clicked!");
        // Later you can pop up a dialog or open a new view here
    }

}

