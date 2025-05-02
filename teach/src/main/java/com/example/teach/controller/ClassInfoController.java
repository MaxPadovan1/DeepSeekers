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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

public class ClassInfoController {
    private User currentUser;
    private DashboardController dashboardController;

    // Your fx:id hooks into the student list container, etc.
    @FXML private Label teacherNameLabel;
    @FXML private Label teacherEmailLabel;
    @FXML private Label teacherPhoneLabel;

    @FXML private ScrollPane studentList;
    @FXML private VBox     studentListContainer;  // fx:id on the inner VBox

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
        for (Subject subj : subs) {
            addClassBlock(subj);
        }
    }

    private void addClassBlock(Subject subj) {
        VBox classBox = new VBox(10);
        classBox.setStyle("-fx-padding:15; -fx-background-radius:10; -fx-background-color:#bdc3c7;");

        // 1) Teacher info
        try {
            Teacher t = new SubjectDAO().findTeacherBySubject(subj.getId());
            Label title = new Label("Subject: " + subj.getName());
            title.setStyle("-fx-font-size:18px; -fx-font-weight:bold;");
            classBox.getChildren().add(title);

            if (t != null) {
                classBox.getChildren().add(new Label("Teacher: " + t.getFirstName() + " " + t.getLastName()));
                classBox.getChildren().add(new Label("Email: "   + t.getEmail()));
            } else {
                classBox.getChildren().add(new Label("Teacher: (none)"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load teacher for " + subj.getName() + ":\n" + ex.getMessage())
                    .showAndWait();
        }

        // 2) Student list
        try {
            List<Student> students = new SubjectDAO().findStudentsBySubject(subj.getId());
            VBox studentBlock = new VBox(5);
            studentBlock.setStyle("-fx-padding:10; -fx-background-radius:5; -fx-background-color:white;");
            studentBlock.getChildren().add(new Label("Students:"));
            for (Student s : students) {
                HBox row = new HBox(20,
                        new Label(s.getFirstName() + " " + s.getLastName()),
                        new Label(s.getEmail()));
                studentBlock.getChildren().add(row);
            }
            classBox.getChildren().add(studentBlock);
        } catch (SQLException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load students for " + subj.getName() + ":\n" + ex.getMessage())
                    .showAndWait();
        }

        studentListContainer.getChildren().add(classBox);
    }


    /// delegate back to the Dashboard for its drawer and reset behavior:
    @FXML private void toggleDrawer() {
        dashboardController.toggleDrawer();
    }

    @FXML private void goToDashboard() {
        dashboardController.goToDashboard(null);
    }
}
