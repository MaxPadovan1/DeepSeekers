package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

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

    private void trySetup() {
        if (currentUser != null && currentSubject != null) {
            if (currentUser instanceof Teacher) {
                loadStudentList();
                if (studentListView != null) studentListView.setVisible(true);
                if (submitGradeButton != null) submitGradeButton.setVisible(true);
                if (editButton != null) editButton.setVisible(true);
            } else {
                selectedStudentId = currentUser.getId();
                loadGrades();
                if (studentListView != null) studentListView.setVisible(false);
                if (submitGradeButton != null) submitGradeButton.setVisible(false);
                if (editButton != null) editButton.setVisible(false);
            }
        }
    }

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




    private void loadAssignmentSubmissions() {
        try {
            List<Assignment> assignments = new AssignmentDAO().getBySubject(currentSubject.getId());
            if (assignments.isEmpty()) return;

            Assignment selectedAssignment = assignments.get(0); // You can make this dynamic with dropdown
            List<ASubmission> submissions = new ASubmissionDAO().getSubmissionsByAssignmentId(selectedAssignment.getId());
            List<Grade> grades = new GradeDAO().getGradesForAssignment(selectedAssignment.getId());

            Map<String, Grade> studentGrades = new HashMap<>();
            for (Grade g : grades) {
                studentGrades.put(g.getStudentId(), g);
            }

            accordion.getPanes().clear();
            for (ASubmission sub : submissions) {
                Grade grade = studentGrades.get(sub.getStudentId());

                String feedback = grade != null ? grade.getFeedback() : "";
                String mark = grade != null ? grade.getGrade() : "";
                String time = sub.getTimestamp();

                accordion.getPanes().add(
                        createPane("Student: " + sub.getStudentId(), feedback, mark, time, selectedAssignment.getId(), sub.getStudentId())
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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




    private TitledPane createPane(String title, String feedback, String grade, String submittedTime, String assignmentId, String studentId) {
        TextArea feedbackArea = new TextArea(feedback);
        TextArea gradeArea = new TextArea(grade);
        TextArea submittedArea = new TextArea(submittedTime);

        boolean isTeacher = currentUser instanceof Teacher;

        feedbackArea.setEditable(isTeacher);
        gradeArea.setEditable(isTeacher);
        submittedArea.setEditable(false);
        feedbackArea.setPrefRowCount(3);
        gradeArea.setPrefRowCount(1);
        submittedArea.setPrefRowCount(1);

        feedbackArea.setWrapText(true);
        gradeArea.setWrapText(true);
        submittedArea.setWrapText(true);

        feedbackArea.setMaxHeight(100);
        gradeArea.setMaxHeight(60);
        submittedArea.setMaxHeight(60);// submission time is always read-only

        Button submitBtn = new Button("Submit Grade");
        submitBtn.setVisible(isTeacher);
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "✅ Grade submitted.");
            alert.showAndWait();
        });

        VBox feedbackBox = new VBox(new Text("Feedback"), feedbackArea);
        VBox gradeBox = new VBox(new Text("Grade"), gradeArea);
        VBox submittedBox = new VBox(new Text("Submitted Time"), submittedArea);
        VBox buttonsBox = new VBox(submitBtn);

        HBox content = new HBox(20, feedbackBox, gradeBox, submittedBox, buttonsBox);
        AnchorPane root = new AnchorPane(content);
        AnchorPane.setTopAnchor(content, 10.0);
        AnchorPane.setLeftAnchor(content, 10.0);

        TitledPane pane = new TitledPane(title, root);
        pane.setUserData(Map.of(
                "assignmentId", assignmentId,
                "studentId", studentId,
                "feedbackArea", feedbackArea,
                "gradeArea", gradeArea,
                "submittedArea", submittedArea
        ));

        return pane;
    }



    @FXML
    private void handleEdit() {
        // No longer needed since edit is managed per pane
    }
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
