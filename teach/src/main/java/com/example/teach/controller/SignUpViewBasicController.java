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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Controller for the basic Sign Up view.
 * <p>
 * Manages role selection (student vs. teacher), loading subjects,
 * generating unique user IDs, validating input, creating new users,
 * and navigation back to the login page.
 */
public class SignUpViewBasicController {

    /** DAO for accessing subjects in the database. */
    private final SubjectDAO subjectDAO = new SubjectDAO();

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

    /**
     * Initializes the Sign Up view.
     * <p>
     * Loads subjects from the database into the checkbox container and combo box,
     * sets up role toggle behavior (show/hide relevant panes and generate IDs),
     * and defaults the selection to student.
     */
    @FXML public void initialize() {
        try {
            List<Subject> subjects = subjectDAO.getAll();
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

        if (roleGroup.getSelectedToggle() == null) {
            studentRadio.setSelected(true);
        }
    }

    /**
     * Generates a unique 6-digit user ID with the given prefix.
     * <p>
     * Attempts up to 1000 random IDs before falling back to sequential generation.
     *
     * @param prefix the letter prefix for the ID (e.g., "S" or "T")
     * @return a unique ID string in the format PREFIX######
     */
    private String generateNextId(String prefix) {
        Random rand = new Random();
        UserDAO dao = new UserDAO();
        for (int attempt = 0; attempt < 1000; attempt++) {
            int num = rand.nextInt(1_000_000);
            String candidate = String.format("%s%06d", prefix, num);
            if (!dao.exists(candidate)) {
                return candidate;
            }
        }
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
     * Handles the Sign-Up button click.
     * <p>
     * Validates user input (fields, email format, subject selection),
     * collects chosen subjects, outputs debug dump, creates the new user
     * via {@link User#signUp}, and navigates back to login.
     *
     * @param event the ActionEvent triggered by clicking the Sign-Up button
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
            try {
                if (subjectDAO.findTeacherBySubject(sel.getId()) != null) {
                    messageLabel.setText("That class already has a teacher.");
                    return;
                }
            } catch (SQLException ex) {
                messageLabel.setText("Error checking existing teacher:\n" + ex.getMessage());
                return;
            }
            subjectIds = List.of(sel.getId());
        }

        // **DEBUG SIGNUP DUMP**
//        System.out.println("=== DEBUG SIGNUP DUMP ===");
//        System.out.println("ID:       " + id);
//        System.out.println("Name:     " + fn + " " + ln);
//        System.out.println("Email:    " + email);
//        System.out.println("Password: " + pw);  // show raw password
        String pwHash = User.hashPassword(pw);
//        System.out.println("Hash:     " + pwHash);  // show password hash
//        System.out.println("Role:     " + (studentRadio.isSelected() ? "Student" : "Teacher"));
//        System.out.println("Subjects: " + subjectIds);
//        System.out.println("=========================");

        // Create user
        User newUser = User.signUp(id, pwHash, fn, ln, email, subjectIds);
        if (newUser != null) {
//            System.out.println("✅ Signed up: " + newUser);
        } else {
//            System.out.println("❌ Sign-up failed for id=" + id);
        }

        // Navigate back to Login
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/LoginPage-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Unexpected error loading Login page.");
        }
    }

    /**
     * Handles the Cancel button click by returning to the login page.
     *
     * @param event the ActionEvent triggered by clicking the Cancel button
     */
    @FXML private void handleCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/LoginPage-view.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
        } catch (IOException e) {
            messageLabel.setText("Could not return to login.");
        }
    }
}