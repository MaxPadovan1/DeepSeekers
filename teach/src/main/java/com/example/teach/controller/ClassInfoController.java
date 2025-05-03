package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import com.example.teach.model.SubjectDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.List;

/**
 * Controller for the Class Info view.
 * <p>
 * Displays detailed blocks for each subject, including teacher assignment
 * and enrolled students, arranged in a grid. Also handles navigation
 * back to the dashboard via injected DashboardController.
 */
public class ClassInfoController {

    /** The authenticated user (unused but available for extensions). */
    private User currentUser;
    /** Reference to the DashboardController for navigation commands. */
    private DashboardController dashboardController;
    /** Grid container for subject info blocks. */
    @FXML private GridPane studentListContainer;

    /**
     * Injects the current User into this controller.
     * <p>
     * Called by {@code DashboardController.loadCenter(...)} before display.
     *
     * @param u the authenticated {@link User}
     */
    public void setUser(User u) {
        this.currentUser = u;
    }

    /**
     * Injects the DashboardController to enable drawer toggling and navigation.
     * <p>
     * Called by {@code DashboardController.loadCenter(...)} before display.
     *
     * @param dash the {@link DashboardController} instance
     */
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /**
     * Populates the grid with class info blocks for each subject.
     * <p>
     * Must be invoked after {@link #setUser} and {@link #setDashboardController}.
     *
     * @param subs list of {@link Subject} instances to display
     */
    public void setSubjects(List<Subject> subs) {
        studentListContainer.getChildren().clear();
        for (int i = 0; i < subs.size(); i++) {
            addClassBlock(subs.get(i), i);
        }
    }

    /**
     * Creates and adds a class info block into the grid.
     * <p>
     * Each block shows the subject name, assigned teacher (if any),
     * and a list of enrolled students. Blocks are styled and placed
     * according to their index in a 4-column layout.
     *
     * @param subj  the {@link Subject} to display
     * @param index zero-based index used to calculate grid row and column
     */
    private void addClassBlock(Subject subj, int index) {
        VBox classBox = new VBox(10);
        classBox.setStyle("-fx-padding:15; -fx-background-radius:10; -fx-background-color:#bdc3c7;");

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
            new Alert(Alert.AlertType.ERROR, "Could not load teacher for " + subj.getName() + ":\n" + ex.getMessage()).showAndWait();
        }

        // Student list
        try {
            VBox studentBlock = new VBox(5);
            studentBlock.setStyle("-fx-padding:10; -fx-background-radius:5; -fx-background-color:white;");
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
            new Alert(Alert.AlertType.ERROR, "Could not load students for " + subj.getName() + ":\n" + ex.getMessage()).showAndWait();
        }

        // Calculate grid position (4 columns)
        int col = index % 4;
        int row = index / 4;
        studentListContainer.add(classBox, col, row);
    }
}