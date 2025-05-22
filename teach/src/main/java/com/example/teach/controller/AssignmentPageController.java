package com.example.teach.controller;

import com.example.teach.model.*;
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




public class AssignmentPageController implements SectionControllerBase {

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
    @FXML private Button saveAssignmentButton;
    @FXML private Button aiHelpButton;

    private boolean editingAssignment = false;


    private User user;
    private Subject subject;
    private File selectedFile;
    private boolean addingNewAssignment = false;


    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final ASubmissionDAO ASubmissionDAO = new ASubmissionDAO();// You will need to implement this

    @Override
    public void setUser(User user) {
        this.user = user;
        updateUIForUserRole();
    }

    @Override
    public void setSubject(Subject subject) {
        this.subject = subject;
        updateUIForUserRole();
    }
    @Override
    public void setDashboardController(DashboardController controller) {
        // Optional, in case you want to update labels or switch pages
    }


    @FXML
    public void initialize() {

    }

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
        if (user instanceof Student) {
            assignmentTitleField.setEditable(false);
            dueDatePicker.setDisable(true);
            assignmentDetailsText.setEditable(false);
        }

        loadAssignments();
    }


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

    @FXML
    private void onAddAssignment() {
        if (!(user instanceof Teacher)) return;

        assignmentTitleField.clear();
        assignmentDetailsText.clear();
        dueDatePicker.setValue(null);
        assignmentDropdown.getSelectionModel().clearSelection();

        assignmentTitleField.setEditable(true);
        assignmentDetailsText.setEditable(true);
        dueDatePicker.setDisable(false);

        addingNewAssignment = true;
        editingAssignment = false;
        saveAssignmentButton.setVisible(true);
        submissionStatusLabel.setText("🆕 Enter assignment details, then press Save.");
    }


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

    @FXML
    private void onReleaseAssignment() {
        Assignment selected = assignmentDropdown.getValue();
        if (selected != null) {
            try {
                assignmentDAO.releaseAssignment(selected.getId());
                loadAssignments();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onAssignmentSelected() {
        Assignment selected = assignmentDropdown.getValue();
        if (selected == null) return;

        assignmentTitleField.setText(selected.getTitle());
        assignmentDetailsText.setText(selected.getDescription());
        dueDatePicker.setValue(LocalDate.parse(selected.getDueDate()));

        if (user instanceof Teacher) {
            loadSubmissions(selected);

            // Lock fields by default unless editing
            if (!editingAssignment) {
                assignmentTitleField.setEditable(false);
                assignmentDetailsText.setEditable(false);
                dueDatePicker.setDisable(true);
                saveAssignmentButton.setVisible(false);
            }
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
        if (!editingAssignment && !addingNewAssignment) {
            assignmentTitleField.setEditable(false);
            assignmentDetailsText.setEditable(false);
            dueDatePicker.setDisable(true);
            saveAssignmentButton.setVisible(false);
        }

    }


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
    @FXML
    private void onEditAssignment() {
        Assignment selected = assignmentDropdown.getValue();
        if (selected == null || !(user instanceof Teacher)) return;

        if (selected.isReleased()) {
            submissionStatusLabel.setText("❌ Cannot edit a released assignment.");
            return;
        }

        editingAssignment = true;

        // Enable editing fields
        assignmentTitleField.setEditable(true);
        assignmentDetailsText.setEditable(true);
        dueDatePicker.setDisable(false);
        saveAssignmentButton.setVisible(true);

        submissionStatusLabel.setText("📝 Now editing. Press Save to confirm.");
    }
    @FXML
    private void onSaveEditedAssignment() {
        if (!(user instanceof Teacher)) return;

        String title = assignmentTitleField.getText();
        String description = assignmentDetailsText.getText();
        String dueDate = dueDatePicker.getValue() != null ? dueDatePicker.getValue().toString() : null;

        if (title.isBlank() || description.isBlank() || dueDate == null) {
            submissionStatusLabel.setText("❗ All fields are required.");
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
                submissionStatusLabel.setText("✅ Assignment added.");
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
                submissionStatusLabel.setText("✅ Assignment updated.");
            }

            // Reset UI
            addingNewAssignment = false;
            editingAssignment = false;
            saveAssignmentButton.setVisible(false);
            assignmentTitleField.setEditable(false);
            assignmentDetailsText.setEditable(false);
            dueDatePicker.setDisable(true);

            loadAssignments();

        } catch (SQLException e) {
            e.printStackTrace();
            submissionStatusLabel.setText("❌ Failed to save.");
        }
    }
    @FXML
    private void onAIHelp() {
        System.out.println("[DEBUG] AI button clicked");
        Assignment selected = assignmentDropdown.getValue();
        if (selected == null) {
            submissionStatusLabel.setText("❗ Please select an assignment first.");
            return;
        }

        String prompt = "Summarize the following assignment or suggest improvements:\n\n"
                + "Title: " + assignmentTitleField.getText() + "\n"
                + "Description: " + assignmentDetailsText.getText();

        aiHelpButton.setDisable(true);
        submissionStatusLabel.setText("⏳ AI is generating suggestions...");

        new Thread(() -> {
            String response = AIService.getInstance().getResponse(prompt);

            // DEBUG: print response in console
            System.out.println("[DEBUG] AI Response:\n" + response);

            javafx.application.Platform.runLater(() -> {
                if (assignmentDetailsText.isEditable()) {
                    // Append suggestion instead of overwriting
                    assignmentDetailsText.appendText("\n\n💡 AI Suggestion:\n" + response);
                } else {
                    assignmentDetailsText.setText(assignmentDetailsText.getText() + "\n\n💡 AI Suggestion:\n" + response);
                }

                submissionStatusLabel.setText("✅ AI suggestions inserted.");
                aiHelpButton.setDisable(false);
            });
        }).start();
    }


}

