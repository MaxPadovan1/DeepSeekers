package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProfilePageController {
    private User currentUser;
    private DashboardController dashboardController;

    // Profile header
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

    /** Called by DashboardController when loading this page fragment. */
    public void setUser(User user) {
        this.currentUser = user;

        // Header labels
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        idLabel.setText("ID: " + user.getId());

        // Always populate email
        emailField.setText(user.getEmail());

        // Clear & disable all optional fields
        for (TextField tf : new TextField[]{
                genderField, ageField, dobField, addressField,
                phoneField, emergencyContactField,
                assignedClassField, divisionField, classTeacherField
        }) {
            tf.clear();
            tf.setDisable(true);
        }

        // Show/hide student vs teacher bits
        if (user instanceof Student s) {
            // e.g. if Student has gender/age/dob:
            // genderField.setText(s.getGender());   genderField.setDisable(false);
            // ageField.setText(String.valueOf(s.getAge())); ageField.setDisable(false);
            // dobField.setText(s.getDob().toString());        dobField.setDisable(false);
        }
        else if (user instanceof Teacher t) {
            // teacher-specific button
            viewPerformanceButton.setVisible(true);
        }
    }

    /** DashboardController injects itself so we can toggle the drawer if needed. */
    public void setDashboardController(DashboardController dashCtrl) {
        this.dashboardController = dashCtrl;
    }

    /** Save changes (stub) and lock edits. */
    @FXML private void onSave(ActionEvent ev) {
        // TODO: persist currentUser changes via DAO...
        onEdit(ev);
    }

    /** Toggle editability of all fields. */
    @FXML private void onEdit(ActionEvent ev) {
        boolean editable = !genderField.isEditable();
        for (TextField tf : new TextField[]{
                genderField, ageField, dobField, addressField,
                phoneField, emergencyContactField,
                assignedClassField, divisionField, classTeacherField, emailField
        }) {
            tf.setDisable(!editable);
        }
    }

    @FXML private void onViewAttendance(ActionEvent ev) {
        // TODO: hook up attendance view
    }

    @FXML private void onViewPerformance(ActionEvent ev) {
        // TODO: hook up performance view
    }
}
