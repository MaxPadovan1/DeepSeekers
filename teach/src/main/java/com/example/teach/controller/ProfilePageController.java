package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class ProfilePageController
{
    private User currentUser;
    private DashboardController dashboardController;

    // inject the root BorderPane so we can swap scenes or delegate
    @FXML private BorderPane profilePage;

    // Header bar
    @FXML private Button hamburgerButton;
    @FXML private MenuButton userMenu;

    // Profile header
    @FXML private ImageView profileImage;
    @FXML private Label nameLabel;
    @FXML private Label idLabel;
    @FXML private Button viewPerformanceButton;

    // Personal details
    @FXML private TextField genderField;
    @FXML private TextField ageField;
    @FXML private TextField dobField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emergencyContactField;

    // Class details
    @FXML private TextField assignedClassField;
    @FXML private TextField divisionField;
    @FXML private TextField classTeacherField;
    @FXML private TextField emailField;
    @FXML private Button viewAttendanceButton;

    // Action buttons
    @FXML private Button saveButton;
    @FXML private Button editButton;

    /** Called by DashboardController when loading this page. */
    public void setUser(User user)
    {
        this.currentUser = user;

        // Top‐right menu shows the name
        userMenu.setText(user.getFirstName() + " " + user.getLastName());

        // Header labels
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        idLabel.setText("ID: " + user.getId());

        // If you have an avatar URL, uncomment this:
        // profileImage.setImage(new Image(user.getAvatarUrl(), true));

        // Guaranteed fields
        emailField.setText(user.getEmail());

        // 4) clear / disable all unsupported fields:
        genderField.clear();           genderField.setDisable(true);
        ageField.clear();              ageField.setDisable(true);
        dobField.clear();              dobField.setDisable(true);
        addressField.clear();          addressField.setDisable(true);
        phoneField.clear();            phoneField.setDisable(true);
        emergencyContactField.clear(); emergencyContactField.setDisable(true);
        assignedClassField.clear();    assignedClassField.setDisable(true);
        divisionField.clear();         divisionField.setDisable(true);
        classTeacherField.clear();     classTeacherField.setDisable(true);

        // 5) if Student has extra info, you can map it here:
        if (user instanceof Student s) {
            // for example, if you later add getGender() etc to Student:
            // genderField.setText(s.getGender());   genderField.setDisable(false);
            // ...
        }
        // If it's a Teacher:
        else if (user instanceof Teacher t) {
            // same eventual mapping for teacher
            viewPerformanceButton.setVisible(true);
        }
    }

    /** DashboardController injects itself here so we can reuse its drawer/toggle logic. */
    public void setDashboardController(DashboardController dashCtrl) {
        this.dashboardController = dashCtrl;
    }

    /** Hamburger menu click — show/hide your drawer (or delegate). */
    @FXML private void toggleDrawer(MouseEvent ev) {
        if (dashboardController != null) {
            dashboardController.toggleDrawer();
        }
    }

    /** Save edits to profile (still in this pane). */
    @FXML private void onSave(ActionEvent ev) {
        // e.g. UserDAO.update(currentUser);
        onEdit(ev); // flip back to non-editable
    }

    /** Toggle editability of all text fields in the form. */
    @FXML private void onEdit(ActionEvent ev) {
        boolean editable = !genderField.isEditable();
        genderField.setEditable(editable);
        ageField.setEditable(editable);
        dobField.setEditable(editable);
        addressField.setEditable(editable);
        phoneField.setEditable(editable);
        emergencyContactField.setEditable(editable);
        emailField.setEditable(editable);
        assignedClassField.setEditable(editable);
        divisionField.setEditable(editable);
        classTeacherField.setEditable(editable);
    }
}