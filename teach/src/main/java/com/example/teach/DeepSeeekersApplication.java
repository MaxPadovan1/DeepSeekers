package com.example.teach;

import com.example.teach.model.SQLiteDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DeepSeeekersApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        new SQLiteDAO();
        FXMLLoader fxmlLoader = new FXMLLoader(DeepSeeekersApplication.class.getResource("LoginPage-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("DeepSeekers");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
