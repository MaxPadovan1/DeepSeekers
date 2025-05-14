package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the "Homework" section of a subject.
 * <p>
 * Implements {@link SectionControllerBase} to allow dependency injection of
 * the current user, subject, and dashboard controller. Manages an Accordion
 * UI component to display homework-related content.
 */
public class HomeworkPageController implements SectionControllerBase, Initializable {

    /** The authenticated user viewing this section. */
    private User currentUser;
    /** The subject context for this Homework page. */
    private Subject currentSubject;
    /** Parent DashboardController for navigation and UI control. */
    private DashboardController dashboardController;
    /** Accordion UI container for organizing homework panes. */
    //@FXML private Accordion accordion;

    /**
     * Injects the authenticated user into this controller.
     *
     * @param user the logged-in {@link User}
     */

    @FXML private Accordion weekAccordion;
    @FXML private Button addWeekButton;
    @FXML private Button removeWeekButton;





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addWeekPane(); // Preload 1 week
        updateRemoveButtonState();
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
     * Injects the authenticated User.
     *
     * @param user the current User (student or teacher)
     */
    @Override public void setUser(User user) {
        this.currentUser = user;

        if (!(user instanceof com.example.teach.model.Teacher)) {
            addWeekButton.setVisible(false);
            removeWeekButton.setVisible(false);

        }
    }

    /**
     * Injects the current subject into this controller.
     * Updates each titled pane's text to include the subject context.
     *
     * @param subject the {@link Subject} being viewed
     */
    @Override public void setSubject(Subject subject) {
        this.currentSubject = subject;
        refreshAccordion();
        // Prefix each pane title with the subject name
        //accordion.getPanes().forEach(pane ->
        // pane.setText(currentSubject.getName() + " / " + pane.getText())
        // );
    }
    private int weekCount = 0;

    private void updateRemoveButtonState() {
        removeWeekButton.setDisable(weekCount == 0);
    }


    @FXML
    private void onAddWeek() {
        addWeekPane();
        updateRemoveButtonState();
    }

    @FXML
    private void onRemoveWeek() {
        if (!weekAccordion.getPanes().isEmpty()) {
            weekAccordion.getPanes().remove(weekAccordion.getPanes().size() - 1);
            weekCount--;
            updateRemoveButtonState();
        }
    }
    private void addWeekPane() {
        weekCount++;
        TitledPane tp = new TitledPane();
        tp.setText("Week " + weekCount);

        TextArea textArea = new TextArea();
        textArea.setPromptText("Homework details...");
        textArea.setWrapText(true);
        textArea.setPrefRowCount(4);

        VBox content = new VBox(10, textArea);
        content.setStyle("-fx-padding: 10;");

        AnchorPane wrapper = new AnchorPane(content);
        AnchorPane.setTopAnchor(content, 10.0);
        AnchorPane.setLeftAnchor(content, 10.0);
        AnchorPane.setRightAnchor(content, 10.0);

        tp.setContent(wrapper);
        weekAccordion.getPanes().add(tp);
    }

    private void refreshAccordion() {
        weekAccordion.getPanes().clear();
        for (int i = 1; i <= weekCount; i++) {
            TextArea ta = new TextArea();
            ta.setPromptText("Homework details...");
            ta.setWrapText(true);
            ta.setPrefRowCount(4);

            VBox content = new VBox(10, ta);
            content.setStyle("-fx-padding: 10;");
            TitledPane tp = new TitledPane("Week " + i, content);
            weekAccordion.getPanes().add(tp);
        }
    }


    /**
     * Handler for the "Home" button in the Homework section.
     * Navigates back to the dashboard or subject home view.
     */
    @FXML private void onGoHome() {
        dashboardController.goToDashboard(null);
    }
}
