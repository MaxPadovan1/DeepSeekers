package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.SubjectDAO;
import com.example.teach.model.User;
import com.example.teach.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SignUpViewBasicController {

    @FXML private RadioButton studentRadio;
    @FXML private RadioButton teacherRadio;
    @FXML private ToggleGroup roleGroup;
    @FXML private TextField idField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private VBox subjectCheckboxContainer;
    @FXML private ComboBox<Subject> subjectComboBox;
    @FXML private VBox studentPane;
    @FXML private VBox teacherPane;
    @FXML private Label messageLabel;

    @FXML public void initialize() {
        try {
            // Load subjects and populate UI
            List<Subject> subjects = new SubjectDAO().getAll();
            subjectCheckboxContainer.getChildren().clear();
            for (Subject s : subjects) {
                CheckBox cb = new CheckBox(s.getName());
                cb.setUserData(s.getId());
                subjectCheckboxContainer.getChildren().add(cb);
            }
            subjectComboBox.getItems().setAll(subjects);
        } catch (SQLException ex) {
            messageLabel.setText("Error loading subjects: " + ex.getMessage());
        }

        // Toggle between student/teacher
        roleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == studentRadio) {
                studentPane.setVisible(true);
                teacherPane.setVisible(false);
                idField.setText(generateNextId("S"));
            } else if (newToggle == teacherRadio) {
                studentPane.setVisible(false);
                teacherPane.setVisible(true);
                idField.setText(generateNextId("T"));
            }
            messageLabel.setText("");
        });

        // Default to student
        if (roleGroup.getSelectedToggle() == null) {
            studentRadio.setSelected(true);
        }
    }

    /**
     * Generates a random 6-digit ID with the given prefix, ensuring no collision.
     * Tries up to 1000 random attempts before falling back to sequential.
     */
    private String generateNextId(String prefix) {
        Random rand = new Random();
        UserDAO dao = new UserDAO();
        for (int attempt = 0; attempt < 1000; attempt++) {
            int num = rand.nextInt(1_000_000); // 0 to 999999
            String candidate = String.format("%s%06d", prefix, num);
            if (!dao.exists(candidate)) {
                return candidate;
            }
        }
        // Fallback sequential generation
        try {
            String max = dao.findMaxIdWithPrefix(prefix);
            int next = (max == null) ? 1 : Integer.parseInt(max.substring(1)) + 1;
            return String.format("%s%06d", prefix, next);
        } catch (Exception ex) {
            ex.printStackTrace();
            return prefix + "000001";
        }
    }


    /**
     * Processes the Sign-Up button click: validates inputs, creates the user,
     * and navigates back to the Login page.
     */
    @FXML private void handleSignUp(ActionEvent event) {
        String id    = idField.getText();
        String pw    = passwordField.getText();
        String fn    = firstNameField.getText().trim();
        String ln    = lastNameField.getText().trim();
        String email = emailField.getText().trim();

        // Validate core fields
        if (fn.isEmpty() || ln.isEmpty() || pw.isEmpty() || email.isEmpty()) {
            messageLabel.setText("All fields are required.");
            return;
        }

        // Validate email format
        // Regex: "\S+@\S+\.\S+"
        // In English: one or more non-whitespace characters, followed by '@',
        // then one or more non-whitespace characters, followed by '.',
        // then one or more non-whitespace characters.
        if (!email.matches("\\S+@\\S+\\.\\S+")) {
            messageLabel.setText("Invalid email format.");
            return;
        }

        // Collect subjects based on role
        List<String> subjectIds;
        if (studentRadio.isSelected()) {
            subjectIds = subjectCheckboxContainer.getChildren().stream()
                    .filter(n -> n instanceof CheckBox && ((CheckBox)n).isSelected())
                    .map(n -> (String)((CheckBox)n).getUserData())
                    .collect(Collectors.toList());
            if (subjectIds.isEmpty()) {
                messageLabel.setText("Students must select at least one subject.");
                return;
            }
            if (subjectIds.size() != 4) {
                messageLabel.setText("You must select 4 up to subjects");
                return;
            }
        } else {
            Subject sel = subjectComboBox.getValue();
            if (sel == null) {
                messageLabel.setText("Teachers must select exactly one subject.");
                return;
            }
            subjectIds = List.of(sel.getId());
        }

        // **DEBUG DUMP**
        System.out.println("=== DEBUG SIGNUP DUMP ===");
        System.out.println("ID:       " + id);
        System.out.println("Name:     " + fn + " " + ln);
        System.out.println("Email:    " + email);
        System.out.println("Password: " + pw);  // show raw password
        String pwHash = User.hashPassword(pw);
        System.out.println("Hash:     " + pwHash);  // show password hash
        System.out.println("Role:     " + (studentRadio.isSelected() ? "Student" : "Teacher"));
        System.out.println("Subjects: " + subjectIds);
        System.out.println("=========================");

        // Create user
        //String pwHash = User.hashPassword(pw);
        User newUser = User.signUp(id, pwHash, fn, ln, email, subjectIds);
        if (newUser != null) {
            System.out.println("✅ Signed up: " + newUser);
        } else {
            System.out.println("❌ Sign-up failed for id=" + id);
        }

        // Navigate back to Log in
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/LoginPage-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Unexpected error loading Login page.");
        }
    }

    /**
     * Handles the Cancel button by returning to the Login page.
     */
    @FXML private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/LoginPage-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Login");
        } catch (IOException e) {
            messageLabel.setText("Could not return to login.");
        }
    }
}