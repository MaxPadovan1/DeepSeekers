package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Controller for the class‐home panel loaded into the Dashboard's center.
 */
public class ClassHomePageController {
    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    /** Called by DashboardController after loading FXML. */
    public void setUser(User user) {
        this.currentUser = user;
    }

    /** Called by DashboardController after loading FXML. */
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        // TODO: update any UI labels/fields here based on currentSubject
    }

    /**
     * So we can call back into the dashboard to swap center
     * (and hide the drawer) without re‐loading the entire scene.
     */
    public void setDashboardController(DashboardController parent) {
        this.dashboardController = parent;
    }

    /**
     * Fired when you click the “Dashboard” label in the side‐drawer.
     * Restores the original dashboard grid.
     */
    @FXML private void goToDashboard(MouseEvent event) {
        dashboardController.goToDashboard(null);
    }

    /** Stub for your toggle‐drawer if you included it here too. */
    @FXML private void toggleDrawer(ActionEvent actionEvent) {
        dashboardController.toggleDrawer();
    }

    /** Stub for logout in the class‐home panel. You can forward or handle directly. */
    @FXML private void handleLogout(ActionEvent actionEvent) {
        // e.g. dashboardController.logout(...)
    }

    /**
     * Clicking the Study icon/text should reload *this* view
     * in the center—i.e. reset Class Home to its default.
     */
    @FXML private void goToStudyPage(MouseEvent ev) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/HomePage-view.fxml")
            );
            Parent view = loader.load();

            // re‐inject the same user, subject, and parent dashboard controller
            ClassHomePageController ctrl = loader.getController();
            ctrl.setUser(currentUser);
            ctrl.setSubject(currentSubject);
            ctrl.setDashboardController(dashboardController);

            // now swap the center back to this fresh copy of class‐home
            dashboardController.getRootBorderPane().setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void goToAssignmentPage(MouseEvent mouseEvent) {
    }

    @FXML private void goToGradePage(MouseEvent mouseEvent) {
    }

    @FXML private void goToHomeworkPage(MouseEvent mouseEvent) {
    }

    @FXML private void goToTestPage(MouseEvent mouseEvent) {
    }
}
