package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    @FXML private Button enhanceWithAIButton;
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
        if (enhanceWithAIButton != null) {
            boolean isTeacher = user instanceof Teacher;
            enhanceWithAIButton.setVisible(isTeacher);
            enhanceWithAIButton.setManaged(isTeacher);
            enhanceWithAIButton.setDisable(!isTeacher);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Homework");
        alert.setHeaderText("Do you want to auto-generate this homework using a short story?");
        alert.setContentText("Choose 'Yes' to generate with AI, or 'No' to enter manually.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isEmpty() || result.get() == cancelButton) return;

        homeworkDropdown.getSelectionModel().clearSelection();
        homeworkTitleField.clear();
        homeworkDetailsText.clear();
        homeworkDueDatePicker.setValue(null);
        editingHomework = null;
        addingNew = true;
        enableEditing(true);

        if (result.get() == yesButton) {
            // Trigger the same logic as Generate with AI
            onGenerateWithAI();
        } else {
            teacherStatusLabel.setText("Enter homework details manually.");
        }
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

        String subjectId = currentSubject.getId();

        List<StudyFile> released = SQLiteDAO.getReleasedFilesForWeek(subjectId, "Week 1"); // can make dynamic later
        if (released.isEmpty()) {
            teacherStatusLabel.setText("❌ No released stories found in Study page.");
            return;
        }

        List<String> options = released.stream()
                .map(f -> "Week 1: " + f.getFileName())
                .toList();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle("Select Short Story");
        dialog.setHeaderText("Choose a short story to base the homework on:");
        dialog.setContentText("Story:");

        Optional<String> selected = dialog.showAndWait();
        if (selected.isEmpty()) return;

        String fileName = selected.get().substring(selected.get().indexOf(":") + 2);
        StudyFile selectedFile = released.stream()
                .filter(f -> f.getFileName().equals(fileName))
                .findFirst()
                .orElse(null);

        if (selectedFile != null) {
            previewAndGenerateHomeworkFromStory(selectedFile);
        }
    }
    private void previewAndGenerateHomeworkFromStory(StudyFile storyFile) {
        try {
            String storyText = new String(storyFile.getData(), StandardCharsets.UTF_8);

            Alert preview = new Alert(Alert.AlertType.CONFIRMATION);
            preview.setTitle("Preview Short Story");
            preview.setHeaderText("Use this short story to generate homework questions?");
            TextArea content = new TextArea(storyText);
            content.setWrapText(true);
            content.setEditable(false);
            content.setPrefSize(600, 400);
            preview.getDialogPane().setContent(content);

            ButtonType generateBtn = new ButtonType("Generate");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            preview.getButtonTypes().setAll(generateBtn, cancelBtn);

            Optional<ButtonType> result = preview.showAndWait();
            if (result.isPresent() && result.get() == generateBtn) {
                runAIHomeworkGeneration(storyFile.getTitle(), storyText);
            }

        } catch (Exception e) {
            e.printStackTrace();
            teacherStatusLabel.setText("❌ Failed to preview story.");
        }
    }
    private void runAIHomeworkGeneration(String title, String storyText) {
        teacherStatusLabel.setText("AI is generating homework...");

        new Thread(() -> {
            AIService ai = AIService.getInstance();
            String prompt = "Using the following short story:\n\n" + storyText + "\n\n"
                    + "Generate 3 to 5 homework questions that assess reading comprehension and critical thinking. "
                    + "Number the questions clearly. Keep it suitable for high school students.";

            String response = ai.getResponse(prompt);

            javafx.application.Platform.runLater(() -> {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("AI-Generated Homework");
                dialog.setHeaderText("Review and edit the AI-generated homework:");

                TextArea textArea = new TextArea(response);
                textArea.setWrapText(true);
                textArea.setPrefHeight(250);
                dialog.getDialogPane().setContent(textArea);

                ButtonType applyBtn = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(applyBtn, cancelBtn);

                dialog.setResultConverter(button -> button == applyBtn ? textArea.getText() : null);

                dialog.showAndWait().ifPresent(edited -> {
                    homeworkDetailsText.setText(edited);
                    homeworkTitleField.setText(title + " – Homework");
                    teacherStatusLabel.setText("AI homework applied. Review before saving.");
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
    @FXML
    private void onEnhanceWithAI() {
        String original = homeworkDetailsText.getText();

        if (original.isBlank()) {
            teacherStatusLabel.setText("❌ Please enter homework questions first.");
            return;
        }

        teacherStatusLabel.setText("Sending your homework to AI for enhancement...");

        new Thread(() -> {
            AIService ai = AIService.getInstance();
            String prompt = "Improve the following homework questions. Keep the intent but enhance clarity, structure, and depth for high school students:\n\n"
                    + original + "\n\n"
                    + "Return only the enhanced version without commentary.";

            String improved = ai.getResponse(prompt);

            javafx.application.Platform.runLater(() -> {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Enhanced Homework");
                dialog.setHeaderText("Review the AI-enhanced questions below:");

                TextArea textArea = new TextArea(improved);
                textArea.setWrapText(true);
                textArea.setPrefHeight(250);
                dialog.getDialogPane().setContent(textArea);

                ButtonType apply = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(apply, cancel);

                dialog.setResultConverter(button -> button == apply ? textArea.getText() : null);

                dialog.showAndWait().ifPresent(edited -> {
                    homeworkDetailsText.setText(edited);
                    teacherStatusLabel.setText("✅ Homework enhanced. Review before saving.");
                });
            });
        }).start();
    }

}
