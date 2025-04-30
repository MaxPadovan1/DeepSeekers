package com.example.teach.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class DashboardController {
    @FXML
    private VBox drawer;

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

}
