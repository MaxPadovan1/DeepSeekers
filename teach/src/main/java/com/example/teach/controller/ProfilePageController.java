package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Profile page fragment within the Dashboard.
 * <p>
 * Displays and manages editing of personal and class-related details
 * for the currently authenticated {@link User} (Student or Teacher).
 */
public class ProfilePageController {

    /** The logged-in user whose profile is being displayed. */
    private User currentUser;
    /** Reference to the DashboardController for navigation and UI control. */
    private DashboardController dashboardController;

    // Profile header
   // @FXML private Label profileLabel;
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


    /**
     * Initializes the profile view with the given user.
     * Populates header and form fields, and configures visibility based on role.
     *
     * @param user the authenticated User (Student or Teacher)
     */
    public void setUser(User user) {
        this.currentUser = user;

        // Header
        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        idLabel.setText("ID: " + user.getId());

        // Personal
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        ageField.clear();
        dobField.clear();
        addressField.clear();
        phoneField.clear();
        emergencyContactField.clear();

        // Class details for teachers or placeholder for students
        teacherFirstNameField.clear();
        teacherLastNameField.clear();
        divisionField.clear();
        teacherEmailField.clear();

        if (user instanceof Student) {
            viewPerformanceButton.setVisible(false);
            //profileLabel.setText("Student Profile");
            // student-specific data loading (e.g. assigned teacher)
        } else if (user instanceof Teacher t) {
            viewPerformanceButton.setVisible(true);
            teacherFirstNameField.setText(t.getFirstName());
            teacherLastNameField.setText(t.getLastName());
            teacherEmailField.setText(t.getEmail());
            //profileLabel.setText("Teacher Profile");
            // divisionField.setText(t.getDivision());
        }

        // disable editing by default
        onEdit(null);
    }

    /**
     * Injects the DashboardController so this controller can invoke navigation.
     *
     * @param dashCtrl the DashboardController instance
     */
    public void setDashboardController(DashboardController dashCtrl) {
        this.dashboardController = dashCtrl;
    }



    /**
     * Saves any changes (stub) and toggles edit mode off.
     *
     * @param ev the ActionEvent from the Save button
     */
    @FXML private void onSave(ActionEvent ev) {
        // TODO: persist changes via appropriate DAO
        onEdit(ev);
    }

    /**
     * Toggles the editability of all form fields.
     *
     * @param ev the ActionEvent from the Edit or Save button (may be null)
     */
    @FXML private void onEdit(ActionEvent ev) {
        boolean editable = firstNameField.isDisable();
        TextField[] fields = {
                firstNameField, lastNameField, emailField,
                ageField, dobField, addressField, phoneField, emergencyContactField,
                teacherFirstNameField, teacherLastNameField, divisionField, teacherEmailField
        };
        for (TextField tf : fields) {
            tf.setDisable(!editable);
        }
    }

    /**
     * Handles the View Attendance button click. (Not yet implemented)
     *
     * @param ev the ActionEvent from the View Attendance button
     */
    @FXML private void onViewAttendance(ActionEvent ev) {
        // TODO: load attendance view
    }

    /**
     * Handles the View Performance button click. (Not yet implemented)
     *
     * @param ev the ActionEvent from the View Performance button
     */
    @FXML private void onViewPerformance(ActionEvent ev) {
        // TODO: load performance view
    }
    /**
     * Navigates back to the main dashboard page.
     * Called from a "Back" button.
     */
    @FXML
    private void goBack() {
        if (dashboardController != null) {
            dashboardController.goToDashboard(null); // or navigate to SubjectHomePage if appropriate
        }
    }

}