package com.example.teach.controller;

import com.example.teach.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the LoginPage view.
 * <p>
 * Handles user authentication and navigation to dashboard or sign-up screens.
 */
public class LoginPageController {

    @FXML private TextField userId;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Hyperlink fPassword;

    /**
     * Invoked when the Sign In button is clicked.
     * <p>
     * Validates input fields, attempts login, displays errors on failure,
     * and opens the dashboard on successful authentication.
     *
     * @param event the ActionEvent triggered by clicking the Sign In button
     */
    @FXML private void handleLogin(ActionEvent event) {
        String id  = userId.getText().trim();
        String pw  = passwordField.getText();

        // 1) Basic non-empty check
        if (id.isEmpty() || pw.isEmpty()) {
            errorLabel.setText("Both User ID and Password are required.");
            return;
        }

        // 2) Hash & attempt login
        String hash = User.hashPassword(pw);
        User loggedIn = User.login(id, hash);
        if (loggedIn == null) {
            // 3a) Failure
            errorLabel.setText("Invalid username or password.");
        } else {
            openDashboard(event, loggedIn);
        }
    }

    /**
     * Loads the dashboard view, injects the authenticated User into its controller,
     * and swaps the current scene to the dashboard.
     *
     * @param event     the ActionEvent that triggered navigation
     * @param loggedIn  the authenticated User object
     */
    private void openDashboard(ActionEvent event, User loggedIn) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/Dashboard-view.fxml")
            );
            Parent dashboardRoot = loader.load();

            // Obtain controller and pass the authenticated user
            DashboardController dashCtrl = loader.getController();
            dashCtrl.setUser(loggedIn);

            // Switch scenes on the same stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardRoot, 1280, 720);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not load Dashboard. Please try again.");
        }
    }

    /**
     * Invoked when the Sign Up button or hyperlink is clicked.
     * <p>
     * Loads the sign-up view and replaces the current scene.
     *
     * @param event the ActionEvent triggered by clicking Sign Up
     */
    @FXML private void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/SignUp-viewBasic.fxml")
            );
            Parent signUpRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(signUpRoot, 1280, 720);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Unable to load Sign Up page. Please try again later.");
        }
    }
    /**
     * Invoked when the "Forgot Password?" hyperlink is clicked.
     * <p>
     * Loads the password reset view and transitions the user to that page.
     *
     * @param event the ActionEvent triggered by clicking the Forgot Password link
     */
    @FXML private void handleForgotPassword(ActionEvent event) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/example/teach/ResetPage.fxml")
                );
                Parent resetRoot = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(resetRoot, 1280, 720);
                stage.setScene(scene);
                stage.setTitle("Reset Password");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Unable to load Reset Password page. Please try again later.");
            }
    }
}