package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    @FXML private Accordion weekAccordion;
    @FXML private Button addWeekButton;
    @FXML private Button removeWeekButton;

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
        addWeekPane();
        updateRemoveButtonState(); // check if we should disable the remove button
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
    private int weekCount = 0;

    private void updateRemoveButtonState() {
        removeWeekButton.setDisable(weekCount == 0);
    }


    @FXML
    private void onAddWeek() {
        addWeekPane();
        updateRemoveButtonState();
    }

    private void addWeekPane() {
        weekCount++;
        TitledPane weekPane = new TitledPane();
        weekPane.setText("Week " + weekCount);

        VBox content = new VBox(10);
        content.getChildren().addAll(
                createRow("Lecture notes:", "https://example.com/lecture-notes"),
                createRow("Pre-lecture notes:", "https://example.com/pre-lecture"),
                createRow("Flashcard:", "https://example.com/flashcard"),
                createRow("Practice quiz link:", "https://example.com/quiz")
        );

        HBox buttons = new HBox(10);
        Button uploadBtn = new Button("Upload");
        Button editBtn = new Button("Edit");
        uploadBtn.setPrefSize(100, 30);
        editBtn.setPrefSize(100, 30);
        buttons.getChildren().addAll(uploadBtn, editBtn);
        buttons.setStyle("-fx-alignment: center-right;");

        AnchorPane anchorPane = new AnchorPane(content, buttons);
        AnchorPane.setTopAnchor(content, 10.0);
        AnchorPane.setLeftAnchor(content, 10.0);
        AnchorPane.setRightAnchor(content, 10.0);
        AnchorPane.setBottomAnchor(buttons, 10.0);
        AnchorPane.setRightAnchor(buttons, 10.0);

        weekPane.setContent(anchorPane);
        weekAccordion.getPanes().add(weekPane);
    }


    private HBox createRow(String labelText, String linkUrl) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size:18px;");
        Hyperlink link = new Hyperlink(linkUrl);
        HBox hbox = new HBox(10, label, link);
        hbox.setStyle("-fx-alignment: center-left;");
        return hbox;
    }
    @FXML
    private void onRemoveWeek() {
        if (!weekAccordion.getPanes().isEmpty()) {
            weekAccordion.getPanes().remove(weekAccordion.getPanes().size() - 1);
            weekCount--;
            updateRemoveButtonState();
            System.out.println("Removed Week " + (weekCount + 1));
        } else {
            System.out.println("No weeks to remove.");
        }
    }


    // TODO: Add @FXML methods for handling Study page actions (e.g., resource loading, quizzes)
}