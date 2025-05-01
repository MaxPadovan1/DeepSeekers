package com.example.teach.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class DashboardController {

    public Button notificationButton;
    public Button hamburgerButton;
    @FXML
    private VBox drawer;

    /**
     * Toggle the visibility (and managed state) of the side drawer.
     */
    @FXML
    private void toggleDrawer() {
        boolean open = drawer.isVisible();
        drawer.setVisible(!open);
        drawer.setManaged(!open);
    }

    /**
     * Placeholder for the back button action.
     */
    @FXML
    private void goBack() {
        System.out.println("Back button clicked!");
    }

    /**
     * Handler for notification button clicks.
     */
    @FXML
    private void showNotifications() {
        System.out.println("Notification button clicked!");
    }
}