package com.example.teach.controller;

import com.example.teach.model.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Controller for the Sign-Up page.
 * <p>
 * Handles user registration logic, including input validation, subject selection,
 * and database communication.
 */
public class SignUpController {

    @FXML private TextField idField;
    @FXML private PasswordField passwordField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleBox;
    @FXML private ListView<String> subjectsList;
    @FXML private Label statusLabel;


    private Connection conn;

    private final Set<String> selectedSubjectIds = new HashSet<>();  // ✅ Add this at class level
    /**
     * Initializes the Sign-Up screen.
     * <p>
     * Populates the role dropdown, sets up subject list with checkboxes,
     * and connects to the database to fetch available subjects.
     */
    @FXML
    public void initialize() {
        roleBox.setItems(FXCollections.observableArrayList("Student", "Teacher"));
        subjectsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:teach.db");

            ObservableList<String> subjectIds = FXCollections.observableArrayList();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM Subjects");
            while (rs.next()) {
                subjectIds.add(rs.getString("id"));
            }

            subjectsList.setItems(subjectIds);

            // ✅ Manually track check state
            subjectsList.setCellFactory(CheckBoxListCell.forListView(item -> {
                SimpleBooleanProperty observable = new SimpleBooleanProperty(false);
                observable.addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        selectedSubjectIds.add(item.toUpperCase());
                    } else {
                        selectedSubjectIds.remove(item.toUpperCase());
                    }
                });
                return observable;
            }));

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading subjects.");
        }
    }

    /**
     * Handles the "Sign Up" button click.
     * <p>
     * Validates user input, processes selected subjects,
     * hashes the password, and attempts to register the user.
     *
     * @param event the ActionEvent triggered by the Sign-Up button
     */
    @FXML
    private void handleSignUp(ActionEvent event) {
        String id = idField.getText().trim();
        String plainPassword = passwordField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String role = roleBox.getValue();

        if (id.isEmpty() || plainPassword.isEmpty() || firstName.isEmpty() ||
                lastName.isEmpty() || email.isEmpty() || role == null) {
            statusLabel.setText("Please fill all fields.");
            return;
        }
        if (role.equals("Student") && !id.toUpperCase().startsWith("S")) {
            statusLabel.setText("Student ID must start with 'S'");
            return;
        }
        if (role.equals("Teacher") && !id.toUpperCase().startsWith("T")) {
            statusLabel.setText("Teacher ID must start with 'T'");
            return;
        }

        List<String> selectedSubjects = selectedSubjectIds.stream().toList();

        if (role.equals("Student") && selectedSubjects.size() > 4) {
            statusLabel.setText("Students can choose up to 4 subjects.");
            return;
        }

        if (role.equals("Teacher") && selectedSubjects.size() != 1) {
            statusLabel.setText("Teachers must choose exactly 1 subject.");
            return;
        }

        String hashedPassword = User.hashPassword(plainPassword);

        User result = User.signUp(id, hashedPassword, firstName, lastName, email, selectedSubjects);

        if (result != null) {
            statusLabel.setText("Sign-up successful!");
            statusLabel.setStyle("-fx-text-fill: green;");
            goToLogin(null);
        } else {
            statusLabel.setText("Sign-up failed. Duplicate ID or invalid subjects.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    /**
     * Navigates back to the login page.
     * <p>
     * Invoked either on successful sign-up or from a "Back to Login" button.
     *
     * @param actionEvent the ActionEvent (can be null)
     */
    @FXML
    public void goToLogin(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/LoginPage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) idField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to load login screen.");
        }
    }
}

