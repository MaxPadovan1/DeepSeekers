package com.example.teach.controller;

import com.example.teach.controller.SectionControllerBase;
import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SubjectHomePageController implements Initializable {
    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    @FXML private BorderPane subjectRoot;
    @FXML private VBox defaultContent;  // our stashed “home” pane

    @Override public void initialize(URL location, ResourceBundle resources) {
        // Nothing here yet — defaultContent is injected automatically
    }

    /** Called by DashboardController right after loading this view. */
    public void setUser(User u)        { currentUser = u; }
    public void setSubject(Subject s)  { currentSubject = s; }
    public void setDashboardController(DashboardController d) {
        dashboardController = d;
    }

    /** Restore the original “home” view. */
    @FXML private void onGoHome() {
        subjectRoot.setCenter(defaultContent);
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName());
    }

    /** Each of these will load a new FXML into the center. */
    @FXML private void onStudy() {
        loadSection("StudyPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Study");
    }
    @FXML private void onHomework() {
        loadSection("Homework-view.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Homework");
    }
    @FXML private void onTest() {
        loadSection("Test-view.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Test");
    }
    @FXML private void onAssignment() {
        loadSection("Assignment-view.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Assignment");
    }
    @FXML private void onGrade() { loadSection("Grade-view.fxml");
        //TODO: add grade
    }

    private void loadSection(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/" + fxmlName));
            Node section = loader.load();

            // If you have controllers for these sections and need to inject user/subject:
            Object ctrl = loader.getController();
            if (ctrl instanceof SectionControllerBase base) {
                base.setUser(currentUser);
                base.setSubject(currentSubject);
                base.setDashboardController(dashboardController);
            }

            subjectRoot.setCenter(section);
        } catch (IOException e) {
            e.printStackTrace();
            // you might show an Alert here
        }
    }
}
