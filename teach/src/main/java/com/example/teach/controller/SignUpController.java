package com.example.teach.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class SignUpController {

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleBox;
    @FXML private TextField subjectsField;
    @FXML private Label statusLabel;

    private Connection conn;

    @FXML
    public void initialize() {
        roleBox.setItems(FXCollections.observableArrayList("Student", "Teacher"));
        try {
            // Update this with your correct database path
            conn = DriverManager.getConnection("jdbc:sqlite:teach.db");
        } catch (SQLException e) {
            statusLabel.setText("DB connection failed.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String id = idField.getText().trim();
        String password = passwordField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleBox.getValue();
        String subjectsRaw = subjectsField.getText().trim();

        if (id.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || role == null) {
            statusLabel.setText("Fill all required fields.");
            return;
        }

        try {
            // Check if user already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT id FROM Users WHERE id = ?");
            checkStmt.setString(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                statusLabel.setText("User ID already exists.");
                return;
            }

            // Insert into Users table
            PreparedStatement insertUser = conn.prepareStatement(
                    "INSERT INTO Users(id, passwordHash, firstName, lastName, role, email) VALUES (?, ?, ?, ?, ?, ?)"
            );
            insertUser.setString(1, id);
            insertUser.setString(2, password); // TODO: hash this password in real app
            insertUser.setString(3, firstName);
            insertUser.setString(4, lastName);
            insertUser.setString(5, role.startsWith("S") ? "S" : "T");
            insertUser.setString(6, email);
            insertUser.executeUpdate();

            // If Student, insert into Subjects table
            if (role.equals("Student") && !subjectsRaw.isBlank()) {
                List<String> subjects = Arrays.asList(subjectsRaw.split("\\s*,\\s*"));
                if (subjects.size() > 4) {
                    statusLabel.setText("Max 4 subjects allowed.");
                    return;
                }

                PreparedStatement insertSubject = conn.prepareStatement(
                        "INSERT INTO Subjects(userId, subjectName) VALUES (?, ?)"
                );
                for (String subject : subjects) {
                    insertSubject.setString(1, id);
                    insertSubject.setString(2, subject);
                    insertSubject.addBatch();
                }
                insertSubject.executeBatch();
            }

            statusLabel.setText("Sign-up successful!");
            statusLabel.setStyle("-fx-text-fill: green;");

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Sign-up failed due to DB error.");
        }
    }
}
