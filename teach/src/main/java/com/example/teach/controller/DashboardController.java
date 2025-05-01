package com.example.teach.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;


public class DashboardController {
    @FXML
    private VBox drawer;
    @FXML
    private VBox sub2box;  // This corresponds to the VBox with fx:id="sub2box"
    @FXML
    private VBox sub3box;  // This corresponds to the VBox with fx:id="sub3box"
    @FXML
    private VBox sub4box;  // This corresponds to the VBox with fx:id="sub4box"
    @FXML
    private VBox sub1box;  // This corresponds to the VBox with fx:id="sub1box"
    @FXML
    private void openSubjectHomePage(MouseEvent event) {
        // Open home page, no subject name passed
    }
    @FXML
    private void toggleDrawer() {
        boolean isVisible = drawer.isVisible();
        drawer.setVisible(!isVisible);
        drawer.setManaged(!isVisible);
    }
    @FXML
    private void goBack() {
        System.out.println("Back button clicked!");
    }

    @FXML
    private void showNotifications() {
        System.out.println("Notification button clicked!");
    }

    @FXML
    private Label lessonPlanLabel;

    @FXML
    public void initialize() {
        lessonPlanLabel.setOnMouseClicked(event -> openLessonPlan());
        sub1box.setOnMouseClicked(this::openSubjectHomePage);
        sub2box.setOnMouseClicked(this::openSubjectHomePage);
        sub3box.setOnMouseClicked(this::openSubjectHomePage);
        sub4box.setOnMouseClicked(this::openSubjectHomePage);
    }

    private void openLessonPlan() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/LessonPlan-view.fxml"));
            Parent lessonPlanRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) lessonPlanLabel.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(lessonPlanRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleMouseEnter() {
        lessonPlanLabel.setStyle("-fx-font-size: 23px; -fx-text-fill: yellow; -fx-cursor: hand;");
    }

    @FXML
    private void handleMouseExit() {
        lessonPlanLabel.setStyle("-fx-font-size: 23px; -fx-text-fill: white; -fx-cursor: hand;");
    }
    @FXML
    private void openSubjectHomePage(String subjectName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/HomePage-view.fxml"));
            Parent HomePageroot = loader.load();

            // Get the controller and set the subject name
            HomePageController controller = loader.getController();
            controller.setSubjectName(subjectName);

            // Get the current stage
            Stage stage1 = (Stage) sub1box.getScene().getWindow();
            Stage stage2 = (Stage) sub2box.getScene().getWindow();
            Stage stage3= (Stage) sub3box.getScene().getWindow();
            Stage stage4= (Stage) sub4box.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(HomePageroot);
            stage1.setScene(scene);
            stage1.show();
            stage2.setScene(scene);
            stage2.show();
            stage3.setScene(scene);
            stage3.show();
            stage4.setScene(scene);
            stage4.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSubject1Click() {
        openSubjectHomePage("Subject 1");
    }

    @FXML
    private void handleSubject2Click() {
        openSubjectHomePage("Subject 2");
    }

    @FXML
    private void handleSubject3Click() {
        openSubjectHomePage("Subject 3");
    }

    @FXML
    private void handleSubject4Click() {
        openSubjectHomePage("Subject 4");
    }
}



