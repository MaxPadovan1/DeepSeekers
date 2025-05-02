package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import com.example.teach.model.SubjectDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class ClassInfoController {
    private User currentUser;
    private DashboardController dashboardController;
    @FXML private GridPane studentListContainer;

    /** Called by DashboardController.loadCenter(...) */
    public void setUser(User u) {
        this.currentUser = u;
    }

    /** Called by DashboardController.loadCenter(...) */
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /**
     * Must be called by DashboardController after loadCenter(),
     * so we know which class/subject to show.
     * Also passed from DashboardController
     */
    public void setSubjects(List<Subject> subs) {
        studentListContainer.getChildren().clear();
        for (int i = 0; i < subs.size(); i++) {
            addClassBlock(subs.get(i), i);
        }
    }

    private void addClassBlock(Subject subj, int index) {
        VBox classBox = new VBox(10);
        classBox.setStyle(
                "-fx-padding:15; -fx-background-radius:10; -fx-background-color:#bdc3c7;"
        );

        // Subject title + teacher info
        try {
            Label title = new Label("Subject: " + subj.getName());
            title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
            classBox.getChildren().add(title);

            Teacher t = new SubjectDAO().findTeacherBySubject(subj.getId());
            if (t != null) {
                classBox.getChildren().add(new Label("Teacher: " + t.getFirstName() + " " + t.getLastName()));
                classBox.getChildren().add(new Label("Email:   " + t.getEmail()));
            } else {
                classBox.getChildren().add(new Label("Teacher: (none)"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load teacher for " + subj.getName() + ":\n" + ex.getMessage())
                    .showAndWait();
        }

        // Student list
        try {
            VBox studentBlock = new VBox(5);
            studentBlock.setStyle(
                    "-fx-padding:10; -fx-background-radius:5; -fx-background-color:white;"
            );
            studentBlock.getChildren().add(new Label("Students:"));
            List<Student> students = new SubjectDAO().findStudentsBySubject(subj.getId());
            for (Student s : students) {
                HBox row = new HBox(20,
                        new Label(s.getFirstName() + " " + s.getLastName()),
                        new Label(s.getEmail())
                );
                studentBlock.getChildren().add(row);
            }
            classBox.getChildren().add(studentBlock);
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load students for " + subj.getName() + ":\n" + ex.getMessage())
                    .showAndWait();
        }

        // calculate column (0–3) and row
        int col = index % 4;
        int row = index / 4;
        studentListContainer.add(classBox, col, row);
    }


    /// delegate back to the Dashboard for its drawer and reset behavior:
    @FXML private void toggleDrawer() {
        dashboardController.toggleDrawer();
    }

    @FXML private void goToDashboard() {
        dashboardController.goToDashboard(null);
    }
}
