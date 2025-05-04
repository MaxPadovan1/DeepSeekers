package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the "Assignment" section of a subject.
 * <p>
 * Implements {@link SectionControllerBase} for dependency injection of
 * the current user, subject, and dashboard controller. Manages UI
 * components related to assignments, including selection, details,
 * submission, and notes.
 */
public class AssignmentPageController implements SectionControllerBase {

    /** The authenticated user viewing this section. */
    private User currentUser;
    /** The subject context for this Assignment page. */
    private Subject currentSubject;
    /** Reference to the DashboardController for navigation and UI updates. */
    private DashboardController dashboardController;

    // UI components
    @FXML private ComboBox<?> assignmentDropdown;
    @FXML private TextArea assignmentDetailsText;
    @FXML private TextArea instructionsText;
    @FXML private Button uploadButton;
    @FXML private Button submitButton;
    @FXML private Hyperlink notesLink;
    @FXML private Label submissionStatusLabel;

    /**
     * Injects the authenticated User into this controller.
     *
     * @param u the current {@link User}
     */
    @Override public void setUser(User u) {
        this.currentUser = u;
    }

    /**
     * Injects the current Subject into this controller.
     *
     * @param s the current {@link Subject}
     */
    @Override public void setSubject(Subject s) {
        this.currentSubject = s;
        // TODO: use SubjectDAO to load assignment data if needed
    }

    /**
     * Injects the DashboardController for navigation actions.
     *
     * @param dash the parent {@link DashboardController}
     */
    @Override public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /**
     * Handler for when an assignment is selected from the dropdown.
     * Currently a no-op stub for FXML reference.
     */
    @FXML private void onAssignmentSelected() {
        // TODO: load assignment details into assignmentDetailsText and instructionsText
    }

    /**
     * Handler for the Upload button click.
     * Currently a no-op stub for FXML reference.
     */
    @FXML private void onUpload() {
        // TODO: implement file upload dialog and set submissionStatusLabel
    }

    /**
     * Handler for the Submit button click.
     * Currently a no-op stub for FXML reference.
     */
    @FXML private void onSubmit() {
        // TODO: implement assignment submission logic and update submissionStatusLabel
    }

    /**
     * Handler for the Notes hyperlink click.
     * Currently a no-op stub for FXML reference.
     */
    @FXML private void onNotesLink() {
        // TODO: open student notes URL or dialog
    }
}