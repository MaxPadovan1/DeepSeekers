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

import static com.example.teach.model.User.Login;


public class LoginPageController {
    @FXML private TextField userId;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Hyperlink fpassword;
    @FXML private Label welcomeText;
    @FXML private Label welcomeText1;


    @FXML private void handleLogin()
    {
        String userID = userId.getText();
        String password = passwordField.getText();

        User loggedInUser = Login(userID, password, DeepSeeekersApplication.DB);
    }
    @FXML private void handleSignUp()
    {
        String userID = userId.getText();
        String password = passwordField.getText();
    }
}