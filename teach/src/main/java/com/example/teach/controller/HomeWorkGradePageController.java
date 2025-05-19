package com.example.teach.controller;

import com.example.teach.model.Grade;
import com.example.teach.model.GradeDAO;
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomeWorkGradePageController implements Initializable, SectionControllerBase {

    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    @FXML
    private Accordion accordion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Don't load anything here — wait until setUser & setSubject are called
    }

    @Override
    public void setUser(User user) {
        this.currentUser = user;
        loadGrades();
    }

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadGrades();
    }

    @Override
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    private void loadGrades() {
        if (currentUser == null || currentSubject == null) return;
        System.out.println("CurrentUser: " + currentUser.getId());
        System.out.println("CurrentSubject: " + currentSubject.getId());

        GradeDAO gradeDAO = new GradeDAO();
        List<Grade> grades;

        if (currentUser instanceof Teacher) {
            grades = gradeDAO.getGradesForAssignment(currentSubject.getId());
        } else {
            grades = gradeDAO.getGradesForStudent(currentUser.getId());
        }

        accordion.getPanes().clear();

        for (Grade g : grades) {
            accordion.getPanes().add(
                    createPane("Assignment ID: " + g.getAssignmentId(), g.getFeedback(), g.getGrade(), g.getSubmittedTime())
            );
        }
    }

    private TitledPane createPane(String title, String feedback, String grade, String submittedTime) {
        boolean isTeacher = currentUser instanceof Teacher;

        // FEEDBACK
        Text feedbackLabel = new Text("Feedback");
        feedbackLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextArea feedbackArea = new TextArea(feedback);
        feedbackArea.setPrefHeight(200);
        feedbackArea.setPrefWidth(800);
        feedbackArea.setEditable(isTeacher);

        // GRADE
        Text gradeLabel = new Text("Grade");
        gradeLabel.setStyle("-fx-font-size: 22px;");
        TextArea gradeArea = new TextArea(grade);
        gradeArea.setPrefHeight(100);
        gradeArea.setPrefWidth(240);
        gradeArea.setEditable(isTeacher);

        VBox gradeBox = new VBox(gradeLabel, gradeArea);
        gradeBox.setPrefWidth(280);

        // SUBMITTED
        Text submittedLabel = new Text("Submitted");
        submittedLabel.setStyle("-fx-font-size: 22px;");
        TextArea submittedArea = new TextArea(submittedTime);
        submittedArea.setPrefHeight(100);
        submittedArea.setPrefWidth(240);
        submittedArea.setEditable(isTeacher);

        VBox submittedBox = new VBox(submittedLabel, submittedArea);
        submittedBox.setPrefWidth(280);

        // RIGHT COLUMN (Grade + Submitted)
        VBox rightColumn = new VBox(gradeBox, submittedBox);
        rightColumn.setPrefWidth(280);

        // MAIN ROW
        HBox mainBox = new HBox(30, feedbackLabel, feedbackArea, rightColumn);
        mainBox.setPrefWidth(1280);
        mainBox.setStyle("-fx-background-color: #ECECEC; -fx-padding: 20px;");

        AnchorPane content = new AnchorPane(mainBox);
        AnchorPane.setTopAnchor(mainBox, 0.0);
        AnchorPane.setLeftAnchor(mainBox, 0.0);

        TitledPane titledPane = new TitledPane(title, content);
        titledPane.setPrefWidth(1280);
        return titledPane;
    }


    @FXML
    private void goBack() {
        if (dashboardController != null && currentSubject != null) {
            dashboardController.navigateTo(
                    "/com/example/teach/SubjectHomePage-view.fxml",
                    "Dashboard / " + currentSubject.getName(),
                    ctrl -> {
                        if (ctrl instanceof SubjectHomePageController shc) {
                            shc.setDashboardController(dashboardController);
                            shc.setUser(currentUser);
                            shc.setSubject(currentSubject);
                        }
                    }
            );
        }
    }
}
