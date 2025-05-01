package com.example.teach.controller;

import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;

public class DashboardController {

    private User currentUser;
    @FXML private MenuButton userMenu;
    @FXML private VBox drawer;

    /** Called immediately _after_ FXMLLoader.load() */
    public void setUser(User user)
    {
        this.currentUser = user;
        userMenu.setText(user.getFirstName() + " " + user.getLastName());
    }

    /**
     * Toggle the visibility (and managed state) of the side drawer.
     */
    @FXML private void toggleDrawer() {
        boolean open = drawer.isVisible();
        drawer.setVisible(!open);
        drawer.setManaged(!open);
    }

    /**
     * Placeholder for the back button action.
     */
    @FXML private void goBack() {
        System.out.println("Back button clicked!");
    }

    /**
     * Handler for notification button clicks.
     */
    @FXML private void showNotifications() {
        System.out.println("Notification button clicked!");
    }
}