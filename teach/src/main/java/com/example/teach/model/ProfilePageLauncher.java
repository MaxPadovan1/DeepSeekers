package com.example.teach;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfilePageLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ProfilePage.fxml"));
        primaryStage.setTitle("Student Profile Test");
        primaryStage.setScene(new Scene(root, 1280, 720)); // Adjust size as you want
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
