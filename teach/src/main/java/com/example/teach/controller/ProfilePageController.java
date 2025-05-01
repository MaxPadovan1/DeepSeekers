package com.example.teach.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class ProfilePageController {

    @FXML
    private ComboBox<String> studentDropdown;

    @FXML
    private Button viewPerformanceButton;

    @FXML
    private Button viewAttendanceButton;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label studentIDLabel;

    @FXML
    public void initialize() {
        studentDropdown.getItems().addAll("Michaela Brad", "Joe David", "Smith Joey");
        studentDropdown.setValue("Michaela Brad");

        studentDropdown.setOnAction(e -> {
            String selected = studentDropdown.getValue();
            studentNameLabel.setText("Name: " + selected);
            studentIDLabel.setText("ID: " + selected.hashCode());
        });

        viewPerformanceButton.setOnAction(e ->
                System.out.println("Viewing performance for: " + studentDropdown.getValue())
        );

        viewAttendanceButton.setOnAction(e ->
                System.out.println("Viewing attendance for: " + studentDropdown.getValue())
        );
    }
}