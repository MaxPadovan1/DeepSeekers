package com.example.teach.controller;
import com.example.teach.model.User;
import com.example.teach.model.Student;
import com.example.teach.model.Teacher;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class LoginPageController {
    @FXML private TextField userId;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Hyperlink fpassword;
    @FXML private Label welcomeText;
    @FXML private Label welcomeText1;


    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    @FXML
    private void handleLogin() {
        String userID   = userId.getText();
        String password = passwordField.getText();

        // call the 2-arg static method on User
        User loggedInUser = User.login(userID, password);

        if (loggedInUser == null) {
            showError("Invalid credentials");
        } else if (loggedInUser instanceof Student) {
            openStudentDashboard((Student)loggedInUser);
        } else {
            openTeacherDashboard((Teacher)loggedInUser);
        }
    }
    @FXML private void handleSignUp()
    {
        String userID = userId.getText();
        String password = passwordField.getText();
    }
    @FXML void openStudentDashboard(Student s) {
        // e.g. FXMLLoader.load("StudentDashboard.fxml"), pass `s` via a setter on the controller, etc.
    }

    @FXML private void openTeacherDashboard(Teacher t) {
        // load teacher view, etc.
    }
}