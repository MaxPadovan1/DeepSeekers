package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.SubjectDAO;
import com.example.teach.model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SignUpViewBasicController {

    @FXML private TextField idField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private VBox subjectCheckboxContainer;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private Label messageLabel;

    @FXML public void initialize() {
        try {
            // 1) Load all subjects
            List<Subject> subjects = new SubjectDAO().getAll();

            // 2) Populate students’ checkboxes
            subjectCheckboxContainer.getChildren().clear();
            for (Subject s : subjects) {
                CheckBox cb = new CheckBox(s.getName());
                cb.setUserData(s.getId());        // stash the ID for later
                subjectCheckboxContainer.getChildren().add(cb);
            }

            // 3) Populate teachers’ combo box
            subjectComboBox.getItems().setAll(subjects);
            subjectComboBox.getSelectionModel().clearSelection();
        }
        catch (SQLException ex) {
            messageLabel.setText("Error loading subjects: " + ex.getMessage());
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String id    = idField.getText().trim();
        String pw    = passwordField.getText();
        String fn    = firstNameField.getText().trim();
        String ln    = lastNameField.getText().trim();
        String email = emailField.getText().trim();

        if (id.isEmpty() || pw.isEmpty() || fn.isEmpty() || ln.isEmpty() || email.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }

        char role = Character.toUpperCase(id.charAt(0));
        List<String> subjectIds;

        if (role == 'S') {
            // collect the IDs of all selected checkboxes
            List<String> selected = subjectCheckboxContainer.getChildren().stream()
                    .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                    .map(node -> (String) ((CheckBox) node).getUserData())
                    .collect(Collectors.toList());

            if (selected.isEmpty()) {
                messageLabel.setText("Students must select at least one subject.");
                return;
            }
            if (selected.size() > 4) {
                messageLabel.setText("You can select up to 4 subjects only.");
                return;
            }
            subjectIds = selected;
        }
        else if (role == 'T') {
            Subject tchSubj = subjectComboBox.getValue();
            if (tchSubj == null) {
                messageLabel.setText("Teachers must select exactly one subject.");
                return;
            }
            subjectIds = List.of(tchSubj.getId());
        }
        else {
            messageLabel.setText("ID must start with 'S' or 'T'.");
            return;
        }

        String pwHash = User.hashPassword(pw);
        User newUser = User.signUp(id, pwHash, fn, ln, email, subjectIds);
        if (newUser == null) {
            messageLabel.setText("Sign-up failed: duplicate ID or invalid data.");
            return;
        }

        // navigate back to Login
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/LoginPage-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Unexpected error loading Login page.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/LoginPage-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
        } catch (IOException e) {
            messageLabel.setText("Could not return to login.");
        }
    }
}
