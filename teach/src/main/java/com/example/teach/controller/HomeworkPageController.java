package com.example.teach.controller;

import com.example.teach.model.Homework;
import com.example.teach.model.Subject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class HomeworkPageController {

    @FXML private VBox homeworkList;
    @FXML private TextField titleField;
    @FXML private TextField weekField;
    @FXML private DatePicker dueDatePicker;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker releaseDatePicker;
    @FXML private DatePicker openDatePicker;
    @FXML private Label feedbackLabel;

    private final HomeworkController homeworkController = new HomeworkController();
    private Subject currentSubject;

    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadHomeworks();
    }

    private void loadHomeworks() {
        homeworkList.getChildren().clear();
        try {
            List<Homework> homeworks = homeworkController.getHomeworksForSubject(currentSubject.getId());
            for (Homework hw : homeworks) {
                Label label = new Label("📘 Week " + hw.getWeek() + ": " + hw.getTitle());
                homeworkList.getChildren().add(label);
            }
        } catch (SQLException e) {
            feedbackLabel.setText("⚠ Failed to load homeworks.");
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddHomework() {
        try {
            Homework hw = new Homework(
                    currentSubject.getId(),
                    weekField.getText(),
                    titleField.getText(),
                    descriptionField.getText(),
                    dueDatePicker.getValue().toString(),
                    releaseDatePicker.getValue().toString(),
                    openDatePicker.getValue().toString(),
                    "HW" + System.currentTimeMillis() // temp ID
            );
            homeworkController.addHomework(hw);
            feedbackLabel.setText("✅ Homework added.");
            loadHomeworks();
        } catch (Exception e) {
            feedbackLabel.setText("❌ Error adding homework.");
            e.printStackTrace();
        }
    }
}