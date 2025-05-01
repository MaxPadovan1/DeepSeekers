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

public class LoginPageController {
    @FXML private TextField userId;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Hyperlink fPassword;

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
            // 3b) Success → debug print + open dashboard
            System.out.println("🔑 Logged in: " + loggedIn);
            openDashboard(event, loggedIn);
        }
    }

    /**
     * Loads the dashboard, injects the logged-in User into its controller,
     * and then shows it in the same window.
     */
    private void openDashboard(ActionEvent event, User loggedIn) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/Dashboard-view.fxml")
            );
            Parent dashboardRoot = loader.load();

            // 1) grab the DashboardController instance
            DashboardController dashCtrl = loader.getController();
            // 2) inject our User
            dashCtrl.setUser(loggedIn);

            // 3) swap scenes
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardRoot, 1280, 720);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Could not load Dashboard. Please try again.");
        }
    }

    @FXML private void handleSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/SignUp-viewBasic.fxml")
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
}