package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Subject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML private VBox sub1box;
    @FXML private VBox sub2box;
    @FXML private VBox sub3box;
    @FXML private VBox sub4box;

    @FXML private VBox drawer;
    @FXML private Label lessonPlanLabel;

    private final VBox[] subjectBoxes = new VBox[4];
    private List<Subject> subjects;

    @FXML
    public void initialize() {
        subjectBoxes[0] = sub1box;
        subjectBoxes[1] = sub2box;
        subjectBoxes[2] = sub3box;
        subjectBoxes[3] = sub4box;
    }

    public void setStudent(Student student) {
        this.subjects = student.getSubjects();

        for (int i = 0; i < subjectBoxes.length; i++) {
            VBox box = subjectBoxes[i];

            if (i < subjects.size()) {
                Subject s = subjects.get(i);
                Label title = new Label(s.getName());
                title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                box.getChildren().clear();
                box.getChildren().add(title);
                box.setVisible(true);
                box.setManaged(true);
            } else {
                box.setVisible(false);
                box.setManaged(false);
            }
        }
    }

    @FXML
    private void toggleDrawer(ActionEvent event) {
        boolean showing = drawer.isVisible();
        drawer.setVisible(!showing);
        drawer.setManaged(!showing);
    }

    @FXML
    private void handleMouseEnter(MouseEvent event) {
        lessonPlanLabel.setStyle("-fx-text-fill: #00aced; -fx-font-size: 18px; -fx-padding: 20px;");
    }

    @FXML
    private void handleMouseExit(MouseEvent event) {
        lessonPlanLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 20px;");
    }

    @FXML private void handleSubject1Click(MouseEvent event) { openSubjectIfPresent(0, event); }
    @FXML private void handleSubject2Click(MouseEvent event) { openSubjectIfPresent(1, event); }
    @FXML private void handleSubject3Click(MouseEvent event) { openSubjectIfPresent(2, event); }
    @FXML private void handleSubject4Click(MouseEvent event) { openSubjectIfPresent(3, event); }

    private void openSubjectIfPresent(int index, MouseEvent event) {
        if (subjects != null && subjects.size() > index) {
            openSubjectPage(subjects.get(index), event);
        }
    }

    private void openSubjectPage(Subject subject, MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/HomePage-view.fxml"));
            Parent root = loader.load();

            // Optionally pass subject to controller
            // HomePageController controller = loader.getController();
            // controller.setSubject(subject);

            Stage stage = (Stage) ((VBox) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(subject.getName() + " - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/LoginPage-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/Dashboard-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
