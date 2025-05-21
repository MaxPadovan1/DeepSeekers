package com.example.teach.controller;

import com.example.teach.model.*;
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

    @FXML private TextArea feedbackArea;
    @FXML private TextArea gradeArea;
    @FXML private TextArea submittedArea;

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
        List<Student> students = new StudentDAO().getStudentsBySubject(currentSubject.getId());
        List<String> names = new ArrayList<>();
        for (Student s : students) {
            String fullName = s.getFirstName() + " " + s.getLastName();
            names.add(fullName);
            nameToIdMap.put(fullName, s.getId());
        }
        studentListView.getItems().setAll(names);

        studentListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedStudentId = nameToIdMap.get(newVal);
            loadGrades();
        });

        if (!names.isEmpty()) {
            studentListView.getSelectionModel().selectFirst();
        }
    }

    private void loadGrades() {
        List<Grade> grades = new GradeDAO().getGradesForStudent(selectedStudentId);
        accordion.getPanes().clear();

        for (Grade g : grades) {
            accordion.getPanes().add(createPane("Assignment: " + g.getAssignmentId(), g.getFeedback(), g.getGrade(), g.getSubmittedTime()));
        }
    }

    private TitledPane createPane(String title, String feedback, String grade, String submittedTime) {
        feedbackArea = new TextArea(feedback);
        gradeArea = new TextArea(grade);
        submittedArea = new TextArea(submittedTime);

        boolean isTeacher = currentUser instanceof Teacher;

        feedbackArea.setEditable(isTeacher);
        gradeArea.setEditable(isTeacher);
        submittedArea.setEditable(isTeacher);

        feedbackArea.setPrefSize(500, 150);
        gradeArea.setPrefSize(150, 60);
        submittedArea.setPrefSize(150, 60);

        VBox feedbackBox = new VBox(new Text("Feedback"), feedbackArea);
        VBox gradeBox = new VBox(new Text("Grade"), gradeArea);
        VBox submittedBox = new VBox(new Text("Submitted Time"), submittedArea);

        HBox content = new HBox(20, feedbackBox, gradeBox, submittedBox);
        AnchorPane root = new AnchorPane(content);
        AnchorPane.setTopAnchor(content, 10.0);
        AnchorPane.setLeftAnchor(content, 10.0);

        return new TitledPane(title, root);
    }

    @FXML
    private void handleEdit() {
        feedbackArea.setEditable(true);
        gradeArea.setEditable(true);
        submittedArea.setEditable(true);
    }

    @FXML
    private void handleSubmit() {
        if (selectedStudentId == null) return;

        Grade grade = new Grade(
                UUID.randomUUID().toString(),
                "A001", // Example assignment ID
                selectedStudentId,
                gradeArea.getText(),
                feedbackArea.getText(),
                submittedArea.getText()
        );
        new GradeDAO().saveOrUpdateGrade(grade);
        loadGrades();
    }
}
