package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.Arrays;

public class SignUpController {

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleBox;
    @FXML private TextField subjectsField;
    @FXML private Label statusLabel;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("Student", "Teacher");
    }

    @FXML
    private void handleSignUp() {
        String id = idField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String role = roleBox.getValue();
        String subjectInput = subjectsField.getText();

        if (id.isEmpty() || password.isEmpty() || firstName.isEmpty() ||
                lastName.isEmpty() || email.isEmpty() || role == null) {
            statusLabel.setText("All fields must be filled!");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        User user;

        if ("Student".equals(role)) {
            Student student = new Student(id, password, firstName, lastName, email);
            if (!subjectInput.isBlank()) {
                student.setSubjects(Arrays.asList(subjectInput.split("\\s*,\\s*")));
            }
            if (student.getSubjects() != null && student.getSubjects().size() > 4) {
                statusLabel.setText("Students cannot select more than 4 subjects.");
                statusLabel.setTextFill(Color.RED);
                return;
            }
            user = student;
        } else {
            user = new Teacher(id, password, firstName, lastName, email);
        }

        boolean success = dbHandler.signUp(user);
        if (success) {
            statusLabel.setText("Sign up successful!");
            statusLabel.setTextFill(Color.GREEN);
            clearForm();
        } else {
            statusLabel.setText("Sign up failed. User may already exist.");
            statusLabel.setTextFill(Color.RED);
        }
    }

    private void clearForm() {
        idField.clear();
        passwordField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        roleBox.setValue(null);
        subjectsField.clear();
    }
}
