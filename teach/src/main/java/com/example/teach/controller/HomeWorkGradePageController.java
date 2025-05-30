package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
/**
 * Controller for the Homework and Grade Page. Handles both teacher and student views.
 * Teachers can review and submit grades; students can view their feedback and marks.
 */
public class HomeWorkGradePageController implements Initializable, SectionControllerBase {

    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    @FXML private Accordion accordion;
    @FXML private ListView<String> studentListView;
    @FXML private Button submitGradeButton;
    @FXML private Button editButton;

    private String selectedStudentId = null;
    private final Map<String, String> nameToIdMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Init after context set
    }

    @Override
    public void setUser(User user) {
        this.currentUser = user;
        trySetup();
    }

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        trySetup();
    }

    @Override
    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
    /**
     * Ensures required context (user and subject) is available before loading data.
     */
    private void trySetup() {
        if (currentUser != null && currentSubject != null) {
            if (currentUser instanceof Teacher) {
                loadStudentList();
                loadAssignmentSubmissions();  // 🔵 CALL IT HERE ✅
                if (studentListView != null) studentListView.setVisible(true);
                if (submitGradeButton != null) submitGradeButton.setVisible(true);
                if (editButton != null) editButton.setVisible(true);
            }

            else {
                selectedStudentId = currentUser.getId();
                loadGrades();
                if (studentListView != null) studentListView.setVisible(false);
                if (submitGradeButton != null) submitGradeButton.setVisible(false);
                if (editButton != null) editButton.setVisible(false);
            }
        }
    }
    /**
     * Loads students who have submitted assignments and displays them in a ListView.
     */
    private void loadStudentList() {
        ASubmissionDAO submissionDAO = new ASubmissionDAO();
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        StudentDAO studentDAO = new StudentDAO();

        nameToIdMap.clear();

        try {
            // Get all assignments for the subject
            List<Assignment> assignments = assignmentDAO.getBySubject(currentSubject.getId());
            Set<String> submittingStudentIds = new HashSet<>();

            // Collect student IDs from all submissions to these assignments
            for (Assignment assignment : assignments) {
                List<ASubmission> submissions = submissionDAO.getSubmissionsByAssignmentId(assignment.getId());
                for (ASubmission s : submissions) {
                    submittingStudentIds.add(s.getStudentId());
                }
            }

            // Load student names directly from Users table via StudentDAO
            List<String> names = new ArrayList<>();
            for (String studentId : submittingStudentIds) {
                Student s = studentDAO.getStudentById(studentId);
                if (s != null) {
                    String fullName = s.getFirstName() + " " + s.getLastName();
                    names.add(fullName);
                    nameToIdMap.put(fullName, s.getId());
                }
            }

            studentListView.setItems(FXCollections.observableArrayList(names));

            studentListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                selectedStudentId = nameToIdMap.get(newVal);
                loadGrades();
            });

            if (!names.isEmpty()) {
                studentListView.getSelectionModel().selectFirst();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads and displays all assignment submissions (for teachers).
     */
    private void loadAssignmentSubmissions() {
        try {
            AssignmentDAO assignmentDAO = new AssignmentDAO();
            ASubmissionDAO submissionDAO = new ASubmissionDAO();
            GradeDAO gradeDAO = new GradeDAO();

            List<Assignment> assignments = assignmentDAO.getBySubject(currentSubject.getId());
            if (assignments.isEmpty()) return;

            accordion.getPanes().clear();

            for (Assignment assignment : assignments) {
                List<ASubmission> submissions = submissionDAO.getSubmissionsByAssignmentId(assignment.getId());
                List<Grade> grades = gradeDAO.getGradesForAssignment(assignment.getId());

                Map<String, Grade> studentGrades = new HashMap<>();
                for (Grade g : grades) {
                    studentGrades.put(g.getStudentId(), g);
                }

                for (ASubmission sub : submissions) {
                    String studentId = sub.getStudentId();
                    Grade grade = studentGrades.get(studentId);

                    String feedback = (grade != null && grade.getFeedback() != null) ? grade.getFeedback() : "";
                    String mark = (grade != null && grade.getGrade() != null) ? grade.getGrade() : "";
                    String time = (sub.getTimestamp() != null) ? sub.getTimestamp() : "N/A";

                    accordion.getPanes().add(
                            createPane("Assignment: " + assignment.getTitle() + " - Student: " + studentId,
                                    feedback, mark, time, assignment.getId(), studentId)
                    );
                }
            }

            if (!accordion.getPanes().isEmpty()) {
                accordion.setExpandedPane(accordion.getPanes().get(0));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Loads grades for the current student and populates the accordion.
     */
    private void loadGrades() {
        if (currentUser == null || !(currentUser instanceof Student) || currentSubject == null) return;

        String studentId = currentUser.getId();
        accordion.getPanes().clear();

        AssignmentDAO assignmentDAO = new AssignmentDAO();
        ASubmissionDAO submissionDAO = new ASubmissionDAO();
        GradeDAO gradeDAO = new GradeDAO();

        try {
            // All assignments for the subject
            List<Assignment> assignments = assignmentDAO.getBySubject(currentSubject.getId());

            for (Assignment assignment : assignments) {
                // Get this student's submission for this assignment
                ASubmission submission = submissionDAO.getSubmissionByStudentAndAssignment(studentId, assignment.getId());
                if (submission == null) continue;  // skip if not submitted

                // Get this student's grade for this assignment
                Grade grade = gradeDAO.getGradesForAssignment(assignment.getId()).stream()
                        .filter(g -> g.getStudentId().equals(studentId))
                        .findFirst()
                        .orElse(null);

                // Fallbacks
                String feedback = (grade != null && grade.getFeedback() != null && !grade.getFeedback().isBlank())
                        ? grade.getFeedback()
                        : " No feedback yet.";

                String mark = (grade != null && grade.getGrade() != null && !grade.getGrade().isBlank())
                        ? grade.getGrade()
                        : "️ Grade pending.";

                String submittedTime = (submission.getTimestamp() != null)
                        ? submission.getTimestamp()
                        : "N/A";

                accordion.getPanes().add(
                        createPane("Assignment: " + assignment.getTitle(), feedback, mark, submittedTime, assignment.getId(), studentId)
                );
                if (!accordion.getPanes().isEmpty()) {
                    accordion.setExpandedPane(accordion.getPanes().get(0));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Builds a titled pane for a specific assignment and student, with optional editing.
     */
    private TitledPane createPane(String title, String feedback, String grade, String submittedTime, String assignmentId, String studentId) {
        TextArea feedbackArea = new TextArea(feedback);
        TextArea gradeArea = new TextArea(grade);
        TextArea submittedArea = new TextArea(submittedTime);

        boolean isTeacher = currentUser instanceof Teacher;

        // Edit permissions
        feedbackArea.setEditable(isTeacher);
        gradeArea.setEditable(isTeacher);
        submittedArea.setEditable(false);

        // Wrapping and sizing
        feedbackArea.setWrapText(true);
        gradeArea.setWrapText(true);
        submittedArea.setWrapText(true);

        feedbackArea.setPrefHeight(200);
        feedbackArea.setPrefWidth(600);

        gradeArea.setPrefHeight(40);
        submittedArea.setPrefHeight(40);
        gradeArea.setPrefWidth(250);
        submittedArea.setPrefWidth(250);

        // Submit button
        Button submitBtn = new Button("Submit Grade");
        submitBtn.setVisible(isTeacher);
        submitBtn.setManaged(isTeacher);
        submitBtn.setOnAction(e -> {
            Grade g = new Grade(
                    UUID.randomUUID().toString(),
                    assignmentId,
                    studentId,
                    gradeArea.getText(),
                    feedbackArea.getText(),
                    submittedArea.getText()
            );
            new GradeDAO().saveOrUpdateGrade(g);
            new Alert(Alert.AlertType.INFORMATION, " Grade submitted.").showAndWait();
        });

        // Layout
        VBox feedbackBox = new VBox(10, new Label("Feedback"), feedbackArea);
        feedbackBox.setPrefWidth(600);

        VBox gradeTimeBox = new VBox(10,
                new VBox(5, new Label("Grade"), gradeArea),
                new VBox(5, new Label("Submitted Time"), submittedArea)
        );
        gradeTimeBox.setPrefWidth(250);

        VBox buttonBox = new VBox(submitBtn);
        buttonBox.setPrefWidth(150);
        buttonBox.setSpacing(20);

        HBox content = new HBox(50, feedbackBox, gradeTimeBox, buttonBox);
        content.setPadding(new Insets(20));
        content.setPrefWidth(1000);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        TitledPane pane = new TitledPane(title, scrollPane);
        pane.setExpanded(true);
        pane.setUserData(Map.of(
                "assignmentId", assignmentId,
                "studentId", studentId,
                "feedbackArea", feedbackArea,
                "gradeArea", gradeArea,
                "submittedArea", submittedArea
        ));

        return pane;
    }
    /**
     * Not used—editing is handled inline per pane.
     */
    @FXML
    private void handleEdit() {
        // No longer needed since edit is managed per pane
    }
    /**
     * Submits all grades edited within the accordion panes.
     */
    @FXML
    private void handleSubmit() {
        for (TitledPane pane : accordion.getPanes()) {
            Object userData = pane.getUserData();
            if (!(userData instanceof Map)) continue;

            Map<String, Object> meta = (Map<String, Object>) userData;

            String assignmentId = (String) meta.get("assignmentId");
            String studentId = (String) meta.get("studentId");
            TextArea feedbackArea = (TextArea) meta.get("feedbackArea");
            TextArea gradeArea = (TextArea) meta.get("gradeArea");
            TextArea submittedArea = (TextArea) meta.get("submittedArea");

            Grade grade = new Grade(
                    UUID.randomUUID().toString(),
                    assignmentId,
                    studentId,
                    gradeArea.getText(),
                    feedbackArea.getText(),
                    submittedArea.getText()
            );

            boolean success = new GradeDAO().saveOrUpdateGrade(grade);
            if (!success) {
                System.err.println(" Failed to save grade for " + studentId);
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, " All grades submitted successfully.");
        alert.showAndWait();
    }


}
