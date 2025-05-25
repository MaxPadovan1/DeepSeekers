package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class HomeworkController implements SectionControllerBase {

    @FXML private VBox teacherSection;
    @FXML private ComboBox<Homework> homeworkDropdown;
    @FXML private TextField homeworkTitleField;
    @FXML private TextArea homeworkDetailsText;
    @FXML private DatePicker homeworkDueDatePicker;
    @FXML private Button addHomeworkButton;
    @FXML private Button editHomeworkButton;
    @FXML private Button saveHomeworkButton;
    @FXML private Button deleteHomeworkButton;
    @FXML private Button releaseHomeworkButton;
    @FXML private Button generateWithAIButton;
    @FXML private Label teacherStatusLabel;

    private HomeworkDAO homeworkDAO = new HomeworkDAO();
    private Subject currentSubject;
    private User currentUser;
    private Homework editingHomework;
    private boolean addingNew = false;

    @Override
    public void setUser(User user) {
        this.currentUser = user;

        // Only show the AI button for teachers
        if (generateWithAIButton != null) {
            boolean isTeacher = user instanceof Teacher;
            generateWithAIButton.setVisible(isTeacher);
            generateWithAIButton.setManaged(isTeacher);
            generateWithAIButton.setDisable(!isTeacher);
        }
    }


    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadHomeworks();
    }

    @Override
    public void setDashboardController(DashboardController controller) {}

    private void loadHomeworks() {
        try {
            List<Homework> list = homeworkDAO.getBySubject(currentSubject.getId());
            homeworkDropdown.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddHomework() {
        homeworkDropdown.getSelectionModel().clearSelection();
        homeworkTitleField.clear();
        homeworkDetailsText.clear();
        homeworkDueDatePicker.setValue(null);
        enableEditing(true);
        addingNew = true;
        editingHomework = null;
        teacherStatusLabel.setText("Enter details and save.");
    }

    @FXML
    private void onEditHomework() {
        if (editingHomework == null) return;
        enableEditing(true);
        addingNew = false;
        teacherStatusLabel.setText("Editing homework. Make changes and save.");
    }

    @FXML
    private void onSaveHomework() {
        String title = homeworkTitleField.getText();
        String description = homeworkDetailsText.getText();
        LocalDate dueDate = homeworkDueDatePicker.getValue();

        if (title.isBlank() || description.isBlank() || dueDate == null) {
            teacherStatusLabel.setText("Please complete all fields.");
            return;
        }

        try {
            if (addingNew) {
                Homework hw = new Homework(
                        currentSubject.getId(),
                        "Week 1", // Placeholder week
                        title,
                        description,
                        dueDate.toString(),
                        null,
                        null,
                        UUID.randomUUID().toString()
                );
                homeworkDAO.add(hw);
                teacherStatusLabel.setText("Homework added.");
            }
            loadHomeworks();
            enableEditing(false);
        } catch (SQLException e) {
            e.printStackTrace();
            teacherStatusLabel.setText("Error saving homework.");
        }
    }

    @FXML
    private void onDeleteHomework() {
        Homework selected = homeworkDropdown.getValue();
        if (selected == null) return;
        try {
            homeworkDAO.delete(selected.getId());
            loadHomeworks();
            teacherStatusLabel.setText("Homework deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
            teacherStatusLabel.setText("Failed to delete homework.");
        }
    }

    @FXML
    private void onToggleRelease() {
        teacherStatusLabel.setText("Release toggle not implemented.");
    }

    @FXML
    private void onHomeworkSelected() {
        Homework selected = homeworkDropdown.getValue();
        if (selected == null) return;
        editingHomework = selected;
        homeworkTitleField.setText(selected.getTitle());
        homeworkDetailsText.setText(selected.getDescription());
        homeworkDueDatePicker.setValue(LocalDate.parse(selected.getDueDate()));
        editHomeworkButton.setDisable(false);
        deleteHomeworkButton.setDisable(false);
        releaseHomeworkButton.setDisable(false);
        saveHomeworkButton.setDisable(true);
        enableEditing(false);
    }

    @FXML
    private void onGenerateWithAI() {
        if (!(currentUser instanceof Teacher)) return;

        String title = homeworkTitleField.getText();
        if (title == null || title.isBlank()) {
            teacherStatusLabel.setText("Enter a title before generating.");
            return;
        }

        teacherStatusLabel.setText("Generating with AI...");

        new Thread(() -> {
            AIService aiService = AIService.getInstance();
            String prompt = "Generate a concise and clear homework description for the following title: \""
                    + title + "\". Limit to 150 words.";

            String aiResponse = aiService.getResponse(prompt);

            javafx.application.Platform.runLater(() -> {
                teacherStatusLabel.setText("AI suggestion ready. Review and edit.");

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("AI Homework Description");
                dialog.setHeaderText("Review and edit the AI-generated homework description:");

                TextArea textArea = new TextArea(aiResponse);
                textArea.setWrapText(true);
                textArea.setPrefHeight(250);
                dialog.getDialogPane().setContent(textArea);

                ButtonType applyBtn = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(applyBtn, cancelBtn);

                dialog.setResultConverter(button -> button == applyBtn ? textArea.getText() : null);

                dialog.showAndWait().ifPresent(edited -> {
                    homeworkDetailsText.setText(edited);
                    teacherStatusLabel.setText("AI description applied. You can edit before saving.");
                });
            });
        }).start();
    }


    private void enableEditing(boolean enable) {
        homeworkTitleField.setDisable(!enable);
        homeworkDetailsText.setDisable(!enable);
        homeworkDueDatePicker.setDisable(!enable);
        saveHomeworkButton.setDisable(!enable);
    }
}
