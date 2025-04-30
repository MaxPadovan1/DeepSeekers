package com.example.teach.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import  javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;


public class LoginPageController {
    @FXML private TextField userId;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Hyperlink fPassword;
    @FXML private Label welcomeText;
    @FXML private Label welcomeText1;

    @FXML private void handleLogin(ActionEvent event) {
        String userID = userId.getText();
        String password = passwordField.getText();

        boolean loginSuccessful=true;

        if(loginSuccessful)
        {
            openDashboard(event);
        }
        else
        {
            errorLabel.setText("Invalid userID or password!");
        }
    }

    private void openDashboard(ActionEvent event)
    {
        try{
            FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent dashboardRoot= fxmlLoader.load();

            Stage currentstage=(Stage) ((Node) event.getSource()).getScene().getWindow();
            //get the new scene
            Scene dashboardScene=new Scene(dashboardRoot);
            currentstage.setTitle("Dashboard");
            currentstage.setScene(dashboardScene);
            currentstage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @FXML private void handleSignUp() {
        String userID = userId.getText();
        String password = passwordField.getText();
    }
}