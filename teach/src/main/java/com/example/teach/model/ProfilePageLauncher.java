package com.example.teach.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfilePageLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // ✅ Correct relative path to the FXML in 'resources/com/example/teach/'
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/SignUp.fxml"));
        Parent root = loader.load();

        stage.setTitle("Sign Up Page");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
