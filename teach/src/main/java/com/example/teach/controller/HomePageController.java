package com.example.teach.controller;

import com.example.teach.model.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class HomePageController {

    @FXML
    private VBox drawer;

    @FXML
    private Label subjectLabel;

    private Student student;  // ✅ Store student locally

    // This is called from Dashboard to pass the student
    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSubjectName(String subjectName) {
        subjectLabel.setText(subjectName);
    }

    @FXML
    private void toggleDrawer() {
        boolean isVisible = drawer.isVisible();
        drawer.setVisible(!isVisible);
        drawer.setManaged(!isVisible);
    }

    @FXML
    private void goBackToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent root = loader.load();

            // ✅ Pass the student back to dashboard
            DashboardController controller = loader.getController();
            controller.setStudent(student);

            Stage stage = (Stage) subjectLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToStudyPage(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/view/StudyPage.fxml"));
            Parent studyRoot = loader.load();

            // Optional: if StudyPageController needs the student, pass it here
            // StudyPageController controller = loader.getController();
            // controller.setStudent(student);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(studyRoot));
            stage.setTitle("Study Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/LoginPage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setStudent(student); // ✅ Pass back the student

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProfile(MouseEvent event) {
        switchToProfileScene(((Node) event.getSource()).getScene().getWindow());
    }

    @FXML
    private void goToProfile(ActionEvent event) {
        switchToProfileScene(((MenuItem) event.getSource()).getParentPopup().getOwnerWindow());
    }

    private void switchToProfileScene(Window window) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/ProfilePage.fxml"));
            Parent root = loader.load();

            // Optionally pass student to profile controller
            // ProfilePageController controller = loader.getController();
            // controller.setStudent(student);

            Stage stage = (Stage) window;
            stage.setScene(new Scene(root));
            stage.setTitle("Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
