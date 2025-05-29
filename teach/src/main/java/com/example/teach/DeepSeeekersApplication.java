package com.example.teach;

import com.example.teach.model.SQLiteDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Main entry point for the DeepSeekers JavaFX application.
 * <p>
 * This class initializes the database schema (via {@link SQLiteDAO})
 * and then loads the login screen from FXML.
 */
public class DeepSeeekersApplication extends Application {

    /**
     * Called when the JavaFX application is launched.
     * <p>
     * This method ensures the database schema exists, constructs any
     * necessary DAOs, and displays the login UI.
     *
     * @param stage the primary window provided by the JavaFX runtime
     * @throws IOException if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        new SQLiteDAO();
        FXMLLoader loader = new FXMLLoader(
                DeepSeeekersApplication.class.getResource("LoginPage-view.fxml")
        );
        Scene scene = new Scene(loader.load(), 1280, 720);
        stage.setTitle("DeepSeekers");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Standard Java launcher.
     * <p>
     * Delegates to {@link #start(Stage)} internally.
     *
     * @param args command-line arguments (ignored)
     */
    public static void main(String[] args) {
        launch();
    }
}
