package com.example.teach.controller;

import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LessonPlanController implements SectionControllerBase, Initializable {

    private User currentUser;
    private DashboardController dashboardController;

    @FXML private Accordion weekAccordion;
    @FXML private Button addWeekButton;
    @FXML private Button removeWeekButton;
    @FXML private ToggleButton toggleViewButton;

    private int weekCount = 0;

    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    @Override
    public void setSubject(com.example.teach.model.Subject subject) {
        // Optional future implementation
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addWeekPane();
        updateRemoveButtonState();
    }

    @FXML
    private void onAddWeek() {
        addWeekPane();
        updateRemoveButtonState();
    }

    @FXML
    private void onRemoveWeek() {
        if (!weekAccordion.getPanes().isEmpty()) {
            weekAccordion.getPanes().remove(weekAccordion.getPanes().size() - 1);
            weekCount--;
        }
        updateRemoveButtonState();
    }

    private void updateRemoveButtonState() {
        removeWeekButton.setDisable(weekCount == 0);
    }

    private void addWeekPane() {
        weekCount++;
        TitledPane weekPane = new TitledPane();
        weekPane.setText("Week " + weekCount);

        Accordion dayAccordion = new Accordion();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        for (String day : days) {
            VBox dayContent = new VBox(10);
            dayContent.setStyle("-fx-background-color: #A9A9A9;");

            TextArea overview = new TextArea();
            overview.setPromptText("Overview");
            overview.setPrefHeight(120);
            overview.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-font-weight: bold;");

            TextArea details = new TextArea();
            details.setPromptText("Details");
            details.setPrefHeight(120);
            details.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-font-weight: bold;");

            Button editBtn = new Button("Edit");
            editBtn.setStyle("-fx-font-size: 8px; -fx-font-weight: bold;");
            editBtn.setOnAction(e -> {
                boolean nowEditable = !overview.isEditable(); // Toggle state
                overview.setEditable(nowEditable);
                details.setEditable(nowEditable);
                editBtn.setText(nowEditable ? "Done" : "Edit");
            });

            dayContent.getChildren().addAll(editBtn, overview, details);

            TitledPane dayPane = new TitledPane(day, dayContent);
            dayPane.setStyle("-fx-background-color: #BDC3C7;");
            dayAccordion.getPanes().add(dayPane);
        }

        weekPane.setContent(dayAccordion);
        weekPane.setStyle("-fx-background-color: #BDC3C7; -fx-padding: 20px;");
        weekAccordion.getPanes().add(weekPane);
    }
    @FXML
    private void goBack() {
        if (dashboardController != null) {
            dashboardController.goToDashboard(null);
        }
    }
}
