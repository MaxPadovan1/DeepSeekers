package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/** Minimal stub so your FXML loads without errors. */
public class AssignmentPageController implements SectionControllerBase {
    private User currentUser;
    private Subject currentSubject;
    private DashboardController dashboardController;

    // these must match your fx:id’s in FXML (even if you don’t use them yet)
    @FXML private ComboBox<?>    assignmentDropdown;
    @FXML private TextArea       assignmentDetailsText;
    @FXML private TextArea       instructionsText;
    @FXML private Button         uploadButton;
    @FXML private Button         submitButton;
    @FXML private Hyperlink      notesLink;
    @FXML private Label          submissionStatusLabel;

    @Override
    public void setUser(User u) {
        this.currentUser = u;
    }

    @Override
    public void setSubject(Subject s) {
        this.currentSubject = s;
        // no DAO calls here yet
    }

    @Override
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    // stub handlers so FXML onAction / onMouseClicked references compile:
    @FXML private void onAssignmentSelected() { /* no-op */ }
    @FXML private void onUpload()            { /* no-op */ }
    @FXML private void onSubmit()            { /* no-op */ }
    @FXML private void onNotesLink()         { /* no-op */ }
}
