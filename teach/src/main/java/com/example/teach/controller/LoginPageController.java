package com.example.teach.controller;
import com.example.teach.DeepSeeekersApplication;
import com.example.teach.TempBackendTesting.MockDB;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.sqlite.core.DB;

import java.util.Optional;
import static com.example.teach.model.User.login;


public class LoginPageController {
    @FXML private TextField userId;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Hyperlink fpassword;
    @FXML private Label welcomeText;
    @FXML private Label welcomeText1;


    @FXML private void handleLogin()
    {
        // 1) Grab inputs
        String userID = userId.getText();
        String password = passwordField.getText();

        // 2) Attempt authentication
        Optional<User> optUser = login(userID, password, DeepSeeekersApplication.DB);

        // 3) Check result
        if (optUser.isPresent()) {
            User loggedIn = optUser.get();
            System.out.printf("User %s %s logged in%n", loggedIn.getFirstName(), loggedIn.getLastName());
            //jesus christ it finally works

            //TODO: continue to dashboard
        }
        else {
            // Invalid credentials—show a friendly message
            System.out.println("Invalid username or password.");
            //you fucked up
        }
    }
    @FXML private void handleSignUp()
    {
        String userID = userId.getText();
        String password = passwordField.getText();
    }
}