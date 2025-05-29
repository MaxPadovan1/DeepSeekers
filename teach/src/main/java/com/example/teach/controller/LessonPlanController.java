package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class LessonPlanController implements SectionControllerBase, Initializable {
    private User currentUser;
    private DashboardController dashboardController;
    private Subject currentSubject;

    private final LessonPlanDAO lpDao = new LessonPlanDAO();

    @FXML private Button addLPButton;
    @FXML private Button removeLPButton;
    @FXML private Button editLPButton;
    @FXML private Button saveLPButton;
    @FXML private ComboBox<LessonPlan> LPDropdown;
    @FXML private TextField LPTitleField;
    @FXML private TextArea LPDetailsText;

    private final ObservableList<LessonPlan> plans = FXCollections.observableArrayList();

    private void prepareManualEntry() {
        clearSelectionState();
        LPTitleField.clear();
        LPDetailsText.clear();
        LPTitleField.setDisable(false);
        LPTitleField.setEditable(true);
        LPDetailsText.setDisable(false);
        LPDetailsText.setEditable(true);
        saveLPButton.setVisible(true);
        saveLPButton.setDisable(false);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearSelectionState();

        // Setup ComboBox with custom display
        LPDropdown.setItems(plans);
        LPDropdown.setCellFactory(cb -> new ListCell<LessonPlan>() {
            @Override
            protected void updateItem(LessonPlan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });
        LPDropdown.setConverter(new StringConverter<LessonPlan>() {
            @Override
            public String toString(LessonPlan lp) {
                return lp == null ? "" : lp.getTitle();
            }
            @Override
            public LessonPlan fromString(String string) {
                return null; // not needed
            }
        });
        LPDropdown.setOnAction(this::onLPSelected);
    }

    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadPlans();
        clearSelectionState();
    }

    public void setSubject(List<Subject> subjects) {
        if (subjects != null && !subjects.isEmpty()) {
            setSubject(subjects.get(0));
        }
    }

    private void loadPlans() {
        plans.clear();
        try {
            plans.addAll(lpDao.findBySubject(currentSubject.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onLPSelected(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        if (sel == null) return;
        LPTitleField.setText(sel.getTitle());
        LPDetailsText.setText(sel.getDetails());
        LPTitleField.setDisable(false);
        LPTitleField.setEditable(false);
        LPDetailsText.setDisable(false);
        LPDetailsText.setEditable(false);

        removeLPButton.setVisible(true);
        removeLPButton.setDisable(false);
        editLPButton.setVisible(true);
        editLPButton.setDisable(false);
    }

    @FXML
    private void onEditLP(ActionEvent event) {
        LPTitleField.setDisable(false);
        LPTitleField.setEditable(true);
        LPDetailsText.setDisable(false);
        LPDetailsText.setEditable(true);
        saveLPButton.setVisible(true);
        saveLPButton.setDisable(false);
        editLPButton.setVisible(false);
    }

    @FXML
    private void onSaveEditedLP(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        try {
            if (sel == null) {
                String id = UUID.randomUUID().toString();
                LessonPlan lp = new LessonPlan(
                        id,
                        currentSubject.getId(),
                        LPTitleField.getText(),
                        LPDetailsText.getText());
                lpDao.save(lp);
                plans.add(lp);
                LPDropdown.getSelectionModel().select(lp);
            } else {
                sel.setTitle(LPTitleField.getText());
                sel.setDetails(LPDetailsText.getText());
                LPDetailsText.setEditable(true);
                lpDao.update(sel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearSelectionState();
    }

    @FXML
    private void onRemoveLP(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        if (sel != null) {
            try {
                lpDao.delete(sel.getId());
                plans.remove(sel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        clearSelectionState();
    }

    private void clearSelectionState() {
        addLPButton.setVisible(true);
        removeLPButton.setVisible(false);
        editLPButton.setVisible(false);
        saveLPButton.setVisible(false);
        LPTitleField.setDisable(true);
        LPDetailsText.setDisable(true);
        LPDropdown.getSelectionModel().clearSelection();
    }
    @FXML
    private void onAddLP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Create Lesson Plan");
        alert.setHeaderText("Do you want to auto-generate the lesson plan using AI?");
        alert.setContentText("Choose 'Yes' to use AI, or 'No' to enter the plan manually.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == yesButton) {
                showWeekSelectionAndGenerateAI();
            } else if (result.get() == noButton) {
                prepareManualEntry();
            } // else Cancel — do nothing
        }
    }
    private void showWeekSelectionAndGenerateAI() {
        try {
            String subjectId = currentSubject.getId();
            List<StudyFile> releasedFiles = SQLiteDAO.getReleasedFilesForWeek(subjectId, "Week 1"); // You can iterate all weeks if needed

            if (releasedFiles.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Files Found");
                alert.setHeaderText("No released short stories found.");
                alert.setContentText("Please upload and release a short story from the Study page.");
                alert.showAndWait();
                return;
            }

            // Convert files to choice labels like "Week 1: short_story.txt"
            List<String> choices = releasedFiles.stream()
                    .map(file -> "Week 1: " + file.getFileName())
                    .toList();

            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
            dialog.setTitle("Select Short Story");
            dialog.setHeaderText("Choose a short story to preview and generate a lesson plan.");
            dialog.setContentText("Story:");

            Optional<String> selected = dialog.showAndWait();

            selected.ifPresent(selectionLabel -> {
                // Extract file name back from the label
                String fileName = selectionLabel.substring(selectionLabel.indexOf(":") + 2);
                StudyFile selectedFile = releasedFiles.stream()
                        .filter(f -> f.getFileName().equals(fileName))
                        .findFirst()
                        .orElse(null);

                if (selectedFile != null) {
                    // Show preview dialog
                    previewAndConfirmLessonGeneration(selectedFile);
                }
            });

        } catch (Exception e) {
            LPDetailsText.setText("❌ Failed to load study files.");
            e.printStackTrace();
        }
    }
    private void previewAndConfirmLessonGeneration(StudyFile storyFile) {
        try {
            String storyText = new String(storyFile.getData(), StandardCharsets.UTF_8);

            Alert previewDialog = new Alert(Alert.AlertType.CONFIRMATION);
            previewDialog.setTitle("Preview Short Story");
            previewDialog.setHeaderText("Would you like to generate a lesson plan based on this story?");

            TextArea previewText = new TextArea(storyText);
            previewText.setWrapText(true);
            previewText.setEditable(false);
            previewText.setPrefWidth(600);
            previewText.setPrefHeight(400);
            previewDialog.getDialogPane().setContent(previewText);

            ButtonType generateButton = new ButtonType("Generate Lesson Plan");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            previewDialog.getButtonTypes().setAll(generateButton, cancelButton);

            Optional<ButtonType> result = previewDialog.showAndWait();
            if (result.isPresent() && result.get() == generateButton) {
                generateAIFromStudyFile(storyFile);
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "❌ Error previewing story.").showAndWait();
            e.printStackTrace();
        }
    }


    private void generateAIFromStudyFile(StudyFile story) {
        try {
            String storyText = new String(story.getData(), StandardCharsets.UTF_8);

            String prompt = "Using this short story:\n\n" + storyText + "\n\n" +
                    "Generate a lesson plan including:\n" +
                    "- 2 to 3 learning objectives\n" +
                    "- 2 discussion questions\n" +
                    "- 1 suggested classroom activity\n" +
                    "Keep output under 200 words.";

            String result = AIService.getInstance().generateLessonPlan(prompt);

            LPTitleField.setText(story.getTitle());
            LPDetailsText.setText(result);
            LPTitleField.setDisable(false);
            LPTitleField.setEditable(true);
            LPDetailsText.setDisable(false);
            LPDetailsText.setEditable(true);
            saveLPButton.setVisible(true);
            saveLPButton.setDisable(false);

        } catch (Exception e) {
            LPDetailsText.setText("❌ Lesson plan generation failed.");
            e.printStackTrace();
        }
    }






}
