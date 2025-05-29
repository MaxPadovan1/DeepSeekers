package com.example.teach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Controller for the Reset Password page.
 * <p>
 * Handles submission of the reset request and navigates back to the login screen.
 */
public class ResetController {
    /**
     * Invoked when the user clicks the "Submit" button on the reset page.
     * <p>
     * Loads the login screen and switches the current scene back to the login page.
     *
     * @param event the ActionEvent triggered by clicking the Submit button
     */
    @FXML
    private void handleResetSubmit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/LoginPage-view.fxml")
            );
            Parent loginRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginRoot, 1280, 720);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

