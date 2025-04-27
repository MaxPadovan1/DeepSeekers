package com.example.teach;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginPageController {
    @FXML
    private TextField userId;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Hyperlink fpassword;
    @FXML
    private Label welcomeText;
    @FXML
    private Label welcomeText1;

    @FXML
    private void handleLogin() {
        String userID = userId.getText();
        String password = passwordField.getText();
    }
    @FXML
    private void handleSignUp() {
        String userID = userId.getText();
        String password = passwordField.getText();
    }
}