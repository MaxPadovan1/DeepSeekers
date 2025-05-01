package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import com.example.teach.model.Subject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    @FXML private TextField userId;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String id = userId.getText().trim();
        String plainPassword = passwordField.getText().trim();

        if (id.isEmpty() || plainPassword.isEmpty()) {
            errorLabel.setText("Please enter both ID and password.");
            return;
        }

        String hashedPassword = User.hashPassword(plainPassword);
        User user = User.login(id, hashedPassword);

        if (user != null) {
            if (user instanceof Student student) {
                openStudentDashboard(event, student);
            } else if (user instanceof Teacher teacher) {
                openTeacherHome(event, teacher.getSubject());
            }
        } else {
            errorLabel.setText("Invalid user ID or password.");
        }
    }

    private void openStudentDashboard(ActionEvent event, Student student) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent root = loader.load();

            // ✅ Inject student object
            DashboardController controller = loader.getController();
            controller.setStudent(student);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Dashboard");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openTeacherHome(ActionEvent event, Subject subject) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/HomePage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(subject.getName() + " Home");
            stage.show();

            // Optional: Pass subject to controller
            // HomePageController controller = loader.getController();
            // controller.setSubject(subject);

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load subject home page.");
        }
    }

    @FXML
    private void handleSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Signup.fxml"));
            Stage stage = (Stage) userId.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load sign-up screen.");
        }
    }
}
