package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
/**
 * Controller for managing the Assignment page.
 * <p>
 * Handles assignment creation, editing, releasing, submission,
 * and AI-based generation of assignment descriptions. Supports both
 * teacher and student roles, updating the UI accordingly.
 */
public class AssignmentPageController implements SectionControllerBase {

    @FXML private Button addAssignmentButton;
    @FXML private Button removeAssignmentButton;
    @FXML private Button releaseAssignmentButton;
    @FXML private Button editAssignmentButton;
    @FXML private Button saveAssignmentButton;
    @FXML private VBox teacherSection;
    @FXML private HBox studentSection;
    @FXML private ComboBox<Assignment> assignmentDropdown;
    @FXML private TextArea assignmentDetailsText;
    @FXML private TableView<ASubmission> submissionTable;
    @FXML private TableColumn<ASubmission, String> studentNameColumn;
    @FXML private TableColumn<ASubmission, String> submissionTimeColumn;
    @FXML private TableColumn<ASubmission, String> fileColumn;
    @FXML private Label submissionStatusLabel;
    @FXML private TextField assignmentTitleField;
    @FXML private DatePicker dueDatePicker;
    @FXML private TableColumn<ASubmission, Void> viewColumn;
    @FXML private Label teacherStatusLabel;
    @FXML private Button generateWithAIButton;



    private boolean editingAssignment = false;


    private User user;
    private Subject subject;
    private File selectedFile;
    private boolean addingNewAssignment = false;


    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final ASubmissionDAO ASubmissionDAO = new ASubmissionDAO();// You will need to implement this

    /**
     * Sets the currently logged-in user and updates the UI.
     */
    @Override
    public void setUser(User user) {
        this.user = user;
        updateUIForUserRole();
    }

    /**
     * Sets the current subject context and updates the UI.
     */
    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
        updateUIForUserRole();
    }

    /**
     * Sets the dashboard controller reference (optional).
     */
    @Override
    public void setDashboardController(DashboardController controller) {
        // Optional, in case you want to update labels or switch pages
    }

    /**
     * Shows a button (makes it visible and enabled).
     */
    private void show(Button btn) {
        btn.setVisible(true);
        btn.setManaged(true);
        btn.setDisable(false);
    }

    /**
     * Hides a button (makes it invisible and disables it).
     */
    private void hide(Button btn) {
        btn.setVisible(false);
        btn.setManaged(false);
        btn.setDisable(true); // optional: just in case
    }

    /**
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Updates the UI based on the user's role (Teacher or Student).
     */
    private void updateUIForUserRole() {
        if (user == null || subject == null) return;

        System.out.println("[AssignmentPageController] User is " + user.getClass().getSimpleName());

        boolean isTeacher = user instanceof Teacher;

        if (teacherSection != null) {
            teacherSection.setVisible(isTeacher);
            teacherSection.setManaged(isTeacher);
        }

        if (studentSection != null) {
            studentSection.setVisible(!isTeacher);
            studentSection.setManaged(!isTeacher);
        }

        if (submissionTable != null) {
            submissionTable.setVisible(isTeacher);
            submissionTable.setManaged(isTeacher);
        }

        // Make fields read-only for students
        if (assignmentTitleField != null) {
            assignmentTitleField.setEditable(isTeacher);
        }
        if (dueDatePicker != null) {
            dueDatePicker.setDisable(!isTeacher);
        }
        if (assignmentDetailsText != null) {
            assignmentDetailsText.setEditable(isTeacher);
        }
        if (generateWithAIButton != null) {
            generateWithAIButton.setVisible(isTeacher);
            generateWithAIButton.setManaged(isTeacher);
            generateWithAIButton.setDisable(!isTeacher);
        }

        if (user instanceof Student) {
            assignmentTitleField.setEditable(false);
            assignmentTitleField.setDisable(false);

            dueDatePicker.setEditable(false);       // for dropdown interaction
            dueDatePicker.setDisable(false);

            assignmentDetailsText.setEditable(false);
            assignmentDetailsText.setDisable(false);
        }
        hide(editAssignmentButton);
        hide(removeAssignmentButton);
        hide(releaseAssignmentButton);
        hide(saveAssignmentButton);

        loadAssignments();
    }


    /**
     * Loads assignments based on user role.
     */
    private void loadAssignments() {
        try {
            List<Assignment> assignments = (user instanceof Teacher)
                    ? assignmentDAO.getBySubject(subject.getId())
                    : assignmentDAO.getReleasedAssignments(subject.getId());
            assignmentDropdown.setItems(FXCollections.observableArrayList(assignments));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepares form for adding a new assignment.
     */
    @FXML
    private void onAddAssignment() {
        if (!(user instanceof Teacher)) return;
        assignmentDropdown.getSelectionModel().clearSelection();
        assignmentTitleField.clear();
        assignmentDetailsText.clear();
        applyPlaceholderStyle(assignmentTitleField, "Enter Assignment Title");
        applyPlaceholderStyle(assignmentDetailsText, "Enter Assignment Details");
        assignmentTitleField.setDisable(false);
        assignmentDetailsText.setDisable(false);
        hide(editAssignmentButton);
        hide(releaseAssignmentButton);
        hide(removeAssignmentButton);

        dueDatePicker.setValue(null);

        assignmentTitleField.setEditable(true);
        assignmentDetailsText.setEditable(true);
        dueDatePicker.setDisable(false);
        ChangeListener<Object> formListener = (obs, oldVal, newVal) -> {
            saveAssignmentButton.setDisable(!isAssignmentFormValid());
        };

        assignmentTitleField.textProperty().addListener(formListener);
        assignmentDetailsText.textProperty().addListener(formListener);
        dueDatePicker.valueProperty().addListener(formListener);

        addingNewAssignment = true;
        editingAssignment = false;
        show(saveAssignmentButton);
        teacherStatusLabel.setText("Enter assignment details, then press Save.");
    }

    /**
     * Removes the selected assignment.
     */
    @FXML
    private void onRemoveAssignment() {
        Assignment selected = assignmentDropdown.getValue();
        if (selected == null) return;

        try {
            assignmentDAO.removeAssignment(selected.getId());
            loadAssignments();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Releases or unreleases the selected assignment.
     */
    @FXML
    private void onToggleRelease() {
        Assignment selected = assignmentDropdown.getValue();
        if (selected == null || !(user instanceof Teacher)) return;

        try {
            if (selected.isReleased()) {
                assignmentDAO.unreleaseAssignment(selected.getId());
                teacherStatusLabel.setText("Assignment is now *unreleased*.");
            } else {
                assignmentDAO.releaseAssignment(selected.getId());
                teacherStatusLabel.setText("Assignment is now *released*.");
            }

            // Clear fields
            assignmentTitleField.clear();
            assignmentDetailsText.clear();
            dueDatePicker.setValue(null);
            assignmentTitleField.setEditable(false);
            assignmentTitleField.setDisable(true);

            assignmentDetailsText.setEditable(false);
            assignmentDetailsText.setDisable(true);

            dueDatePicker.setDisable(true);

            hide(saveAssignmentButton);
            hide(editAssignmentButton);
            hide(removeAssignmentButton);
            hide(releaseAssignmentButton);

            teacherStatusLabel.setText("");

            assignmentDropdown.getSelectionModel().clearSelection();

        } catch (SQLException e) {
            e.printStackTrace();
            teacherStatusLabel.setText("Failed to update release status.");
        }
    }

    /**
     * Applies placeholder style to text input fields.
     */
    public static void applyPlaceholderStyle(TextInputControl field, String placeholderText) {
        field.setText(placeholderText);
        field.setStyle("-fx-text-fill: grey; -fx-font-style: italic;");

        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                if (field.getText().equals(placeholderText)) {
                    field.clear();
                    field.setStyle("-fx-text-fill: black; -fx-font-style: normal;");
                }
            } else {
                if (field.getText().isEmpty()) {
                    field.setText(placeholderText);
                    field.setStyle("-fx-text-fill: grey; -fx-font-style: italic;");
                }
            }
        });
    }

    /**
     * Called when an assignment is selected from the dropdown.
     * Populates form and loads submissions.
     */
    @FXML
    private void onAssignmentSelected() {
        Assignment selected = assignmentDropdown.getValue();
        teacherStatusLabel.setText("");
        if (selected == null) return;
        assignmentTitleField.setText(selected.getTitle());
        assignmentDetailsText.setText(selected.getDescription());
        dueDatePicker.setValue(LocalDate.parse(selected.getDueDate()));

        if (user instanceof Teacher) {
            loadSubmissions(selected);

            // Lock fields by default unless editing
            if (!addingNewAssignment && !editingAssignment) {
                assignmentTitleField.setEditable(false);
                assignmentDetailsText.setEditable(false);
                dueDatePicker.setDisable(true);
                hide(saveAssignmentButton);
            }

            // Show Edit and Delete always
            show(editAssignmentButton);
            show(removeAssignmentButton);

            releaseAssignmentButton.setText(selected.isReleased() ? "Unrelease" : "Release");
            show(releaseAssignmentButton);
        }

        if (user instanceof Student student) {
            try {
                ASubmission existing = ASubmissionDAO.getSubmissionByStudentAndAssignment(
                        student.getId(), selected.getId()
                );
                if (existing != null) {
                    submissionStatusLabel.setText("Previously submitted: " + Path.of(existing.getFilePath()).getFileName());
                } else {
                    submissionStatusLabel.setText("No submission yet.");
                }
            } catch (SQLException e) {
                submissionStatusLabel.setText("Failed to check submission.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads all submissions for a given assignment.
     */
    private void loadSubmissions(Assignment assignment) {
        try {
            List<ASubmission> submissions = ASubmissionDAO.getSubmissionsByAssignmentId(assignment.getId());
            ObservableList<ASubmission> observable = FXCollections.observableArrayList(submissions);

            submissionTable.setItems(observable);

            studentNameColumn.setCellValueFactory(data -> data.getValue().studentNameProperty());
            submissionTimeColumn.setCellValueFactory(data -> data.getValue().timestampProperty());
            fileColumn.setCellValueFactory(data -> data.getValue().filePathProperty());
            viewColumn.setCellFactory(col -> new TableCell<>() {
                private final Button viewButton = new Button("Open");

                {
                    viewButton.setOnAction(event -> {
                        ASubmission s = getTableView().getItems().get(getIndex());
                        try {
                            File file = new File(s.getFilePath());
                            if (file.exists()) {
                                java.awt.Desktop.getDesktop().open(file);
                            } else {
                                System.out.println("File not found: " + s.getFilePath());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(viewButton);
                    }
                }
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the assignment form fields.
     */
    private boolean isAssignmentFormValid() {
        String title = assignmentTitleField.getText();
        String details = assignmentDetailsText.getText();
        LocalDate dueDate = dueDatePicker.getValue();

        return title != null && !title.trim().isEmpty() && !title.equals("Enter Assignment Title")
                && details != null && !details.trim().isEmpty() && !details.equals("Enter Assignment Details")
                && dueDate != null;
    }

    /**
     * Opens a file chooser to upload a .txt file.
     */
    @FXML
    private void onUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a text file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            submissionStatusLabel.setText("Selected: " + selectedFile.getName());
        }
    }

    /**
     * Submits the selected file as an assignment submission.
     */
    @FXML
    private void onSubmit() {
        if (selectedFile == null || !(user instanceof Student)) return;

        Assignment assignment = assignmentDropdown.getValue();
        if (assignment == null) return;

        Student student = (Student) user;

        // Format timestamp
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        // Generate unique filename
        String fileName = student.getId() + "_" + timestamp + ".txt";

        // Destination path
        String dest = "teach/submissions/" + subject.getId() + "/" + assignment.getId() + "/" + fileName;

        try {
            Path destPath = Paths.get(dest);
            Files.createDirectories(destPath.getParent());
            Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            // Save submission to DB
            ASubmissionDAO.submitAssignment(new ASubmission(
                    UUID.randomUUID().toString(),           // unique submission ID
                    assignment.getId(),
                    student.getId(),
                    dest,
                    java.time.LocalDateTime.now().toString()
            ));

            submissionStatusLabel.setText("Submitted: " + fileName);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            submissionStatusLabel.setText("Submission failed.");
        }
    }

    /**
     * Enables editing of the selected assignment.
     */
    @FXML
    private void onEditAssignment() {
        Assignment selected = assignmentDropdown.getValue();
        if (selected == null || !(user instanceof Teacher)) return;
        if (selected.isReleased()) {
            teacherStatusLabel.setText("Cannot edit a released assignment.");
            return;
        }
        editingAssignment = true;
        assignmentTitleField.setText(selected.getTitle());
        assignmentDetailsText.setText(selected.getDescription());
        dueDatePicker.setValue(LocalDate.parse(selected.getDueDate()));
        assignmentTitleField.setDisable(false);
        assignmentTitleField.setEditable(true);
        assignmentDetailsText.setDisable(false);
        assignmentDetailsText.setEditable(true);
        dueDatePicker.setDisable(false);
        show(saveAssignmentButton);
        teacherStatusLabel.setText("Now editing. Press Save to confirm.");
    }

    /**
     * Saves the assignment after editing or adding.
     */
    @FXML
    private void onSaveEditedAssignment() {
        if (!(user instanceof Teacher)) return;

        String title = assignmentTitleField.getText();
        String description = assignmentDetailsText.getText();
        String dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().toString() : null;

        if (title.isBlank() || description.isBlank() || dueDate == null) {
            teacherStatusLabel.setText("All fields are required.");
            return;
        }

        try {
            if (addingNewAssignment) {
                Assignment newAssignment = new Assignment(
                        UUID.randomUUID().toString(),
                        subject.getId(),
                        title,
                        description,
                        dueDate,
                        false
                );
                assignmentDAO.add(newAssignment);
                teacherStatusLabel.setText("Assignment added.");
            } else if (editingAssignment) {
                Assignment selected = assignmentDropdown.getValue();
                if (selected == null) return;

                Assignment updated = new Assignment(
                        selected.getId(),
                        selected.getSubjectId(),
                        title,
                        description,
                        dueDate,
                        selected.isReleased()
                );
                assignmentDAO.updateAssignment(updated);
                teacherStatusLabel.setText("Assignment updated.");
            }
            // Reset UI
            addingNewAssignment = false;
            editingAssignment = false;
            hide(saveAssignmentButton);
            assignmentTitleField.setEditable(false);
            assignmentTitleField.setDisable(true);

            assignmentDetailsText.setEditable(false);
            assignmentDetailsText.setDisable(true);

            dueDatePicker.setDisable(true);
            hide(editAssignmentButton);
            hide(removeAssignmentButton);
            hide(releaseAssignmentButton);
            assignmentTitleField.clear();
            assignmentDetailsText.clear();
            dueDatePicker.setValue(null);
            loadAssignments();

        } catch (SQLException e) {
            e.printStackTrace();
            teacherStatusLabel.setText("Failed to save.");
        }
    }

    /**
     * Uses AI to generate a description based on current title and description.
     */
    @FXML
    private void onGenerateWithAI() {
        if (!(user instanceof Teacher)) return;

        String title = assignmentTitleField.getText();
        String desc = assignmentDetailsText.getText();
        if (title == null || title.isBlank()) {
            teacherStatusLabel.setText("Enter a title before generating.");
            return;
        }
        if (desc == null || desc.isBlank()){
            teacherStatusLabel.setText("Enter a description before generating.");
            return;
        }

        teacherStatusLabel.setText("Generating with AI...");

        new Thread(() -> {
            AIService aiService = AIService.getInstance();
            String prompt = "Generate a well-structured assignment brief titled '" + title + "', based on this description: '" + desc + "'. Limit it to 200 words. Use only plain text. Do not include bullet points, asterisks, markdown, or any formatting symbols. Write it as if for a teacher giving instructions to students.";

            String aiResponse = aiService.getResponse(prompt);

            javafx.application.Platform.runLater(() -> {
                teacherStatusLabel.setText("AI suggestion ready. Review and edit.");

                // Create a custom dialog
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("AI Assignment Description");
                dialog.setHeaderText("Review and edit the AI-suggested assignment description below:");

                // Editable TextArea
                TextArea textArea = new TextArea(aiResponse);
                textArea.setWrapText(true);
                textArea.setPrefHeight(300);
                dialog.getDialogPane().setContent(textArea);

                // Dialog buttons
                ButtonType applyButton = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialog.getDialogPane().getButtonTypes().addAll(applyButton, cancelButton);

                // Return edited text on Apply
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == applyButton) {
                        return textArea.getText();
                    }
                    return null;
                });

                dialog.showAndWait().ifPresent(editedText -> {
                    assignmentDetailsText.setText(editedText);
                    teacherStatusLabel.setText("AI description applied. You can further edit before saving.");
                });
            });
        }).start();
    }
}