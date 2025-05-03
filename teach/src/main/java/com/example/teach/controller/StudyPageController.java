package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the "Study" section of a subject.
 * <p>
 * Implements {@link SectionControllerBase} to allow {@link DashboardController}
 * and {@link SubjectHomePageController} to inject the current user and subject.
 * Also implements {@link Initializable} for any further initialization.
 */
public class StudyPageController implements SectionControllerBase, Initializable {

    /** Reference to the parent DashboardController for navigation or UI updates. */
    private DashboardController dashboardController;
    /** The authenticated user viewing this section. */
    private User currentUser;
    /** The subject being studied in this section. */
    private Subject currentSubject;

    /**
     * Called by the JavaFX framework after root element injection.
     * <p>
     * Perform any additional initialization for the Study page here.
     *
     * @param location  the location used to resolve relative paths for the root object, or null if unknown
     * @param resources the resources used to localize the root object, or null if not used
     */
    @Override public void initialize(URL location, ResourceBundle resources) {
        // TODO: add initialization logic for the Study page
    }

    /**
     * Injects the DashboardController instance.
     *
     * @param dash the DashboardController managing the overall UI
     */
    @Override public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /**
     * Injects the authenticated User.
     *
     * @param u the current User (student or teacher)
     */
    @Override public void setUser(User u) {
        this.currentUser = u;
    }

    /**
     * Injects the current Subject being viewed.
     *
     * @param s the Subject for this Study section
     */
    @Override
    public void setSubject(Subject s) {
        this.currentSubject = s;
    }

    // TODO: Add @FXML methods for handling Study page actions (e.g., resource loading, quizzes)
}