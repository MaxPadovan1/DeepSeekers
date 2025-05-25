package com.example.teach.controller;

import com.example.teach.model.Homework;
import com.example.teach.model.HomeworkDAO;
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
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
        teacherStatusLabel.setText("AI generation not connected yet.");
    }

    private void enableEditing(boolean enable) {
        homeworkTitleField.setDisable(!enable);
        homeworkDetailsText.setDisable(!enable);
        homeworkDueDatePicker.setDisable(!enable);
        saveHomeworkButton.setDisable(!enable);
    }
}
