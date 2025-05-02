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
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField ageField;
    @FXML private TextField dobField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emergencyContactField;

    // Class details
    @FXML private TextField teacherFirstNameField;
    @FXML private TextField teacherLastNameField;
    @FXML private TextField divisionField;
    @FXML private TextField teacherEmailField;
    @FXML private Button viewAttendanceButton;

    // Action buttons
    @FXML private Button saveButton;
    @FXML private Button editButton;

    /** Called by DashboardController when loading this page fragment. */
    public void setUser(User user) {
        this.currentUser = user;

        // Header
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        idLabel.setText("ID: " + user.getId());

        // Personal
        firstNameField.setText(user.getFirstName());
        lastNameField .setText(user.getLastName());
        emailField    .setText(user.getEmail());
        // clear the rest for now
        ageField.clear(); dobField.clear();
        addressField.clear(); phoneField.clear(); emergencyContactField.clear();

        // Class details: only teachers know their division, etc.
        teacherFirstNameField.clear();
        teacherLastNameField .clear();
        divisionField        .clear();
        teacherEmailField    .clear();

        // Show/hide bits
        if (user instanceof Student s) {
            // no performance button for students
            viewPerformanceButton.setVisible(false);
            // class details for student come from their assigned teacher/division...
            // fill teacherFirstNameField/teacherLastNameField/divisionField/teacherEmailField
            // once you wire in your SubjectDAO.lookup for the student’s class.
        }
        else if (user instanceof Teacher t) {
            viewPerformanceButton.setVisible(true);
            // teacher sees their own class details:
            teacherFirstNameField.setText(t.getFirstName());
            teacherLastNameField .setText(t.getLastName());
            teacherEmailField    .setText(t.getEmail());
            // you might have t.getDivision() on your model:
            // divisionField.setText(t.getDivision());
        }

        // disable all editing initially
        onEdit(null);
    }

    /** Inject DashboardController so we can toggle the drawer. */
    public void setDashboardController(DashboardController dashCtrl) {
        this.dashboardController = dashCtrl;
    }

    /** Save (stub) then disable editing again. */
    @FXML private void onSave(ActionEvent ev) {
        // TODO: persist via DAO...
        onEdit(ev);
    }

    /** Toggle editability of all fields. */
    @FXML private void onEdit(ActionEvent ev) {
        boolean makeEditable = !firstNameField.isDisable();
        for (TextField tf : new TextField[]{
                firstNameField, lastNameField, emailField,
                ageField, dobField, addressField, phoneField, emergencyContactField,
                teacherFirstNameField, teacherLastNameField, divisionField, teacherEmailField
        }) {
            tf.setDisable(makeEditable);
        }
    }

    @FXML private void onViewAttendance(ActionEvent ev) {
        // TODO: attendance view
    }

    @FXML private void onViewPerformance(ActionEvent ev) {
        // TODO: performance view
    }
}