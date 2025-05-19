package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

/**
 * Controller for the Subject Home Page within the Dashboard.
 * <p>
 * Manages the display of the default home pane and navigation
 * to various subject-specific sections (Study, Homework, Test, etc.).
 * Relies on {@link SectionControllerBase} for section controllers.
 */
public class SubjectHomePageController {

    /** The authenticated user for this session. */
    private User currentUser;
    /** The subject currently being viewed. */
    private Subject currentSubject;
    /** Reference to the DashboardController for navigation and UI updates. */
    private DashboardController dashboardController;
    /** Root layout for this subject page, with a center region for content. */
    @FXML private BorderPane subjectRoot;
    /** The default "home" content pane, shown when returning home. */
    @FXML private VBox defaultContent;

    /**
     * Injects the authenticated user into this controller.
     * Called by DashboardController immediately after loading this view.
     *
     * @param u the logged-in {@link User}
     */
    public void setUser(User u) {
        this.currentUser = u;
    }

    /**
     * Injects the current subject into this controller.
     * Called by DashboardController immediately after loading this view.
     *
     * @param s the {@link Subject} to display
     */
    public void setSubject(Subject s) {
        this.currentSubject = s;
    }

    /**
     * Injects the DashboardController for cross-controller navigation.
     * Called by DashboardController immediately after loading this view.
     *
     * @param d the parent {@link DashboardController}
     */
    public void setDashboardController(DashboardController d) {
        this.dashboardController = d;
    }

    /**
     * Restores the original home view in the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML private void onGoHome() {
        subjectRoot.setCenter(defaultContent);
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName());
    }

    /**
     * Loads the Study section into the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML private void onStudy() {
        loadSection("StudyPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Study");
    }

    /**
     * Loads the Homework section into the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML private void onHomework() {
        loadSection("HomeworkPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Homework");
    }

    /**
     * Placeholder for Test section: currently logs a click event.
     */
    @FXML private void onTest() {
        loadSection("TestPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Test");
    }

    /**
     * Loads the Assignment section into the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML
    private void onAssignment() {
        loadSection("AssignmentPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Assignment");
    }



    /**
     * Placeholder for Grade section: currently logs a click event.
     */
    @FXML private void onGrade() {
        loadSection("HomeWorkGradePage.fxml");  // Assuming this is the actual Grades view
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Grade");
    }

    /**
     * Helper to load a section FXML into the center pane, performing
     * dependency injection for its controller if it extends SectionControllerBase.
     *
     * @param fxmlName name of the FXML file in the com/example/teach package
     */
    private void loadSection(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/teach/" + fxmlName
                    )
            );
            Node section = loader.load();

            Object ctrl = loader.getController();
            if (ctrl instanceof SectionControllerBase base) {
                base.setUser(currentUser);
                base.setSubject(currentSubject);
                base.setDashboardController(dashboardController);
            }

            subjectRoot.setCenter(section);
        } catch (IOException e) {
            e.printStackTrace();
            // Consider showing an Alert in production code
        }
    }
}