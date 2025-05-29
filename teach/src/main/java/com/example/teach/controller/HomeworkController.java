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
/**
 * Controller for managing Homework creation, editing, deletion, and AI-assisted generation.
 * Supports teacher-only features and integrates with the AIService for content generation.
 */
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
    /**
     * Sets the current user and adjusts UI based on user role.
     * @param user the logged-in user
     */
    @Override
    public void setUser(User user) {
        this.currentUser = user;

        boolean isTeacher = user instanceof Teacher;

        addHomeworkButton.setVisible(isTeacher);
        addHomeworkButton.setManaged(isTeacher);

        editHomeworkButton.setVisible(isTeacher);
        editHomeworkButton.setManaged(isTeacher);

        saveHomeworkButton.setVisible(isTeacher);
        saveHomeworkButton.setManaged(isTeacher);

        deleteHomeworkButton.setVisible(isTeacher);
        deleteHomeworkButton.setManaged(isTeacher);

        releaseHomeworkButton.setVisible(isTeacher);
        releaseHomeworkButton.setManaged(isTeacher);

        enhanceWithAIButton.setVisible(isTeacher);
        enhanceWithAIButton.setManaged(isTeacher);

        if (!isTeacher) {
            teacherStatusLabel.setText("🔒 View-only mode. Homework is visible but cannot be edited.");
        }
    }

    /**
     * Sets the current subject context and loads existing homework.
     * @param subject the selected subject
     */
    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadHomeworks();
    }

    @Override
    public void setDashboardController(DashboardController controller) {}
    /**
     * Loads homework from the database for the current subject.
     */
    private void loadHomeworks() {
        try {
            List<Homework> list;
            if (currentUser instanceof Teacher) {
                list = homeworkDAO.getBySubject(currentSubject.getId());
            } else {
                list = homeworkDAO.getReleasedBySubject(currentSubject.getId());
            }
            homeworkDropdown.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler for adding new homework, optionally using AI assistance.
     */
    @FXML
    private void onAddHomework() {
        if (!(currentUser instanceof Teacher)) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Homework");
        alert.setHeaderText("Do you want to auto-generate this homework using course content?");
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
    /**
     * Enables editing of the selected homework item.
     */
    @FXML
    private void onEditHomework() {
        if (editingHomework == null) return;
        enableEditing(true);
        addingNew = false;
        teacherStatusLabel.setText("Editing homework. Make changes and save.");
    }
    /**
     * Saves newly added or edited homework into the database.
     */
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
            Homework hw;

            if (addingNew) {
                hw = new Homework(
                        currentSubject.getId(),
                        "Week 1", // You can make this dynamic later
                        title,
                        description,
                        dueDate.toString(),
                        null, // releaseDate
                        null, // openDate
                        UUID.randomUUID().toString()
                );
                homeworkDAO.add(hw);
            } else {
                hw = editingHomework;
                hw.setTitle(title);
                hw.setDescription(description);
                hw.setDueDate(dueDate.toString());
                homeworkDAO.update(hw);
            }

            teacherStatusLabel.setText("Homework saved. Click 'Release' to publish it to students.");
            loadHomeworks(); // Refresh the dropdown
            homeworkDropdown.setValue(hw); // Select the newly added or edited homework
            enableEditing(false);
        } catch (SQLException e) {
            e.printStackTrace();
            teacherStatusLabel.setText("Error saving or releasing homework.");
        }
    }

    /**
     * Deletes the selected homework.
     */
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
    /**
     * Placeholder method for release functionality.
     */
    @FXML
    private void onToggleRelease() {
        if (!(currentUser instanceof Teacher)) return;

        Homework selected = homeworkDropdown.getValue();
        if (selected == null) {
            teacherStatusLabel.setText("Please select homework to release/unrelease.");
            return;
        }

        boolean isReleased = selected.getReleaseDate() != null;

        try {
            if (isReleased) {
                homeworkDAO.unreleaseHomework(selected.getId());
                teacherStatusLabel.setText(" Homework has been unpublished.");
            } else {
                homeworkDAO.releaseHomework(selected.getId());
                teacherStatusLabel.setText("Homework has been released to students.");
            }

            // Reload list to reflect release state and re-select
            loadHomeworks();
            homeworkDropdown.setValue(
                    homeworkDAO.getBySubject(currentSubject.getId()).stream()
                            .filter(hw -> hw.getId().equals(selected.getId()))
                            .findFirst().orElse(null)
            );

        } catch (SQLException e) {
            e.printStackTrace();
            teacherStatusLabel.setText("Error toggling release status.");
        }
    }

    /**
     * Populates the UI with selected homework details.
     */
    @FXML
    private void onHomeworkSelected() {
        Homework selected = homeworkDropdown.getValue();
        if (selected == null) return;

        editingHomework = selected;
        homeworkTitleField.setText(selected.getTitle());
        homeworkDetailsText.setText(selected.getDescription());
        homeworkDueDatePicker.setValue(LocalDate.parse(selected.getDueDate()));

        boolean isReleased = selected.getReleaseDate() != null;
        releaseHomeworkButton.setText(isReleased ? "Unrelease" : "Release");

        editHomeworkButton.setDisable(false);
        deleteHomeworkButton.setDisable(false);
        releaseHomeworkButton.setDisable(false);
        saveHomeworkButton.setDisable(true);
        enableEditing(false);
    }

    /**
     * Opens a dialog to choose course content and generates homework using AI.
     */
    @FXML
    private void onGenerateWithAI() {
        if (!(currentUser instanceof Teacher)) return;

        String subjectId = currentSubject.getId();

        List<StudyFile> released = SQLiteDAO.getReleasedFilesForWeek(subjectId, "Week 1"); // can make dynamic later
        if (released.isEmpty()) {
            teacherStatusLabel.setText("No released stories found in Study page.");
            return;
        }

        List<String> options = released.stream()
                .map(f -> "Week 1: " + f.getFileName())
                .toList();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle("Select Course Content:");
        dialog.setHeaderText("Choose course content to base the homework on:");
        dialog.setContentText("Course Content:");

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
    /**
     * Shows preview of selected course content before generating homework.
     * @param storyFile the selected StudyFile
     */
    private void previewAndGenerateHomeworkFromStory(StudyFile storyFile) {
        try {
            String storyText = new String(storyFile.getData(), StandardCharsets.UTF_8);

            Alert preview = new Alert(Alert.AlertType.CONFIRMATION);
            preview.setTitle("Preview Course Content");
            preview.setHeaderText("Use this course content to generate homework questions?");
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
            teacherStatusLabel.setText("Failed to preview content.");
        }
    }
    /**
     * Sends the course content to AI service and updates the homework form with the generated content.
     * @param title title of the content
     * @param storyText the full text of the content
     */
    private void runAIHomeworkGeneration(String title, String storyText) {
        teacherStatusLabel.setText("AI is generating homework...");

        new Thread(() -> {
            AIService ai = AIService.getInstance();
            String prompt = "Using the following course content:\n\n" + storyText + "\n\n"
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

    /**
     * Enables or disables editing of form fields.
     * @param enable true to enable, false to disable
     */

    private void enableEditing(boolean enable) {
        // For both teachers and students: allow scroll & text visibility
        homeworkTitleField.setEditable(enable);
        homeworkTitleField.setDisable(false);

        homeworkDetailsText.setEditable(enable);
        homeworkDetailsText.setDisable(false); // allow scroll

        homeworkDueDatePicker.setDisable(!enable);
        saveHomeworkButton.setDisable(!enable);
    }


    /**
     * Enhances manually entered homework using the AIService.
     */
    @FXML
    private void onEnhanceWithAI() {
        String original = homeworkDetailsText.getText();

        if (original.isBlank()) {
            teacherStatusLabel.setText("Please enter homework questions first.");
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
                    teacherStatusLabel.setText("Homework enhanced. Review before saving.");
                });
            });
        }).start();
    }

}
