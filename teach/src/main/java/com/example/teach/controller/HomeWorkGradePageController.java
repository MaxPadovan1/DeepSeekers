package com.example.teach.controller;

import com.example.teach.model.Subject;
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
import java.util.ResourceBundle;

public class HomeWorkGradePageController implements Initializable, SectionControllerBase {

    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    @FXML
    private Accordion accordion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // If you're planning to load content dynamically later, you can call a method here.
        accordion.getPanes().addAll(
                createPane("Assignment 1", "Feedback for Assignment 1", "9/10", "2025-05-10 14:00"),
                createPane("Test 2", "Feedback for Test 2", "18/20", "2025-05-12 10:30"),
                createPane("Assignment 2", "Feedback for Assignment 2", "7/10", "2025-05-14 11:00"),
                createPane("Test 3", "Feedback for Test 3", "20/20", "2025-05-15 09:45")
        );

    }

    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }
    private TitledPane createPane(String title, String feedback, String grade, String submittedTime) {
        Text feedbackLabel = new Text("Feedback");
        feedbackLabel.setStyle("-fx-font-size: 24px;");

        TextArea feedbackArea = new TextArea(feedback);
        feedbackArea.setPrefHeight(200);
        feedbackArea.setPrefWidth(800);

        TextArea gradeArea = new TextArea(grade);
        gradeArea.setPrefHeight(100);
        gradeArea.setPrefWidth(240);

        Text gradeLabel = new Text("Grade");
        gradeLabel.setStyle("-fx-font-size: 22px;");

        VBox gradeBox = new VBox(gradeLabel, gradeArea);
        gradeBox.setStyle("-fx-background-color: #A9A9A9;");
        gradeBox.setPrefWidth(280);

        TextArea submittedArea = new TextArea(submittedTime);
        submittedArea.setPrefHeight(100);
        submittedArea.setPrefWidth(240);

        Text submittedLabel = new Text("Submitted");
        submittedLabel.setStyle("-fx-font-size: 22px;");

        VBox submittedBox = new VBox(submittedLabel, submittedArea);
        submittedBox.setStyle("-fx-background-color: #A9A9A9;");
        submittedBox.setPrefWidth(280);

        VBox rightColumn = new VBox(gradeBox, submittedBox);
        rightColumn.setStyle("-fx-background-color: #A9A9A9;");
        rightColumn.setPrefWidth(280);

        HBox mainBox = new HBox(30, feedbackLabel, feedbackArea, rightColumn);
        mainBox.setStyle("-fx-background-color: #A9A9A9;");
        mainBox.setPrefWidth(1280);

        AnchorPane content = new AnchorPane(mainBox);
        AnchorPane.setTopAnchor(mainBox, 0.0);
        AnchorPane.setLeftAnchor(mainBox, 0.0);

        TitledPane titledPane = new TitledPane(title, content);
        titledPane.setPrefWidth(1280);
        return titledPane;
    }

    private void openGradesSection() {
        // Automatically open the "Grade" pane (assume it's the last one)
        if (!accordion.getPanes().isEmpty()) {
            accordion.setExpandedPane(accordion.getPanes().get(accordion.getPanes().size() - 1));
        }
    }

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
    }

    @Override
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
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

    // Optional: Add method to populate accordion dynamically if needed
}

