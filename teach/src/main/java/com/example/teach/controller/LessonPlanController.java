package com.example.teach.controller;

import com.example.teach.model.LessonPlanDAO;
import com.example.teach.model.LessonPlan;
import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class LessonPlanController implements SectionControllerBase, Initializable {
    private User currentUser;
    private DashboardController dashboardController;
    private Subject currentSubject;

    private final LessonPlanDAO lpDao = new LessonPlanDAO();

    @FXML private Button addLPButton;
    @FXML private Button removeLPButton;
    @FXML private Button editLPButton;
    @FXML private Button saveLPButton;
    @FXML private ComboBox<LessonPlan> LPDropdown;
    @FXML private TextField LPTitleField;
    @FXML private TextArea LPDetailsText;

    private final ObservableList<LessonPlan> plans = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // disable controls by default
        removeLPButton.setDisable(true);
        editLPButton.setDisable(true);
        saveLPButton.setDisable(true);
        LPTitleField.setDisable(true);
        LPDetailsText.setDisable(true);

        // set up ComboBox
        LPDropdown.setItems(plans);
        LPDropdown.setOnAction(this::onLPSelected);
    }

    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /**
     * Called by DashboardController, passing a list of subjects.
     * We take the first subject (teacher has one) and load its plans.
     */
    public void setSubject(List<Subject> subjects) {
        if (subjects != null && !subjects.isEmpty()) {
            setSubject(subjects.get(0));
        } else {
            plans.clear();
            clearFields();
        }
    }

    /**
     * Internal: load plans for a single subject.
     */
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        try {
            List<LessonPlan> list = lpDao.findBySubject(subject.getId());
            plans.setAll(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearFields();
        LPDropdown.getSelectionModel().clearSelection();
    }

    @FXML
    private void onAddLP(ActionEvent event) {
        LPDropdown.getSelectionModel().clearSelection();
        LPTitleField.clear();
        LPDetailsText.clear();

        LPTitleField.setDisable(false);
        LPDetailsText.setDisable(false);
        saveLPButton.setDisable(false);
        editLPButton.setDisable(true);
        removeLPButton.setDisable(true);
    }

    @FXML
    private void onLPSelected(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        if (sel == null) return;

        LPTitleField.setText(sel.getTitle());
        LPDetailsText.setText(sel.getDetails());

        LPTitleField.setDisable(true);
        LPDetailsText.setDisable(true);
        editLPButton.setDisable(false);
        removeLPButton.setDisable(false);
        saveLPButton.setDisable(true);
    }

    @FXML
    private void onEditLP(ActionEvent event) {
        LPTitleField.setDisable(false);
        LPDetailsText.setDisable(false);
        saveLPButton.setDisable(false);
        editLPButton.setDisable(true);
    }

    @FXML
    private void onSaveEditedLP(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        try {
            if (sel == null) {
                // creating new lesson plan
                String id = UUID.randomUUID().toString();
                LessonPlan lp = new LessonPlan(
                        id,
                        currentSubject.getId(),
                        LPTitleField.getText(),
                        LPDetailsText.getText()
                );
                lpDao.save(lp);
                plans.add(lp);
                LPDropdown.getSelectionModel().select(lp);
            } else {
                // updating existing
                sel.setTitle(LPTitleField.getText());
                sel.setDetails(LPDetailsText.getText());
                lpDao.update(sel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // reset UI
            LPTitleField.setDisable(true);
            LPDetailsText.setDisable(true);
            saveLPButton.setDisable(true);
            editLPButton.setDisable(false);
            removeLPButton.setDisable(false);
        }
    }

    @FXML
    private void onRemoveLP(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        if (sel == null) return;

        try {
            lpDao.delete(sel.getId());
            plans.remove(sel);
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Clear and disable fields and buttons.
     */
    private void clearFields() {
        LPTitleField.clear();
        LPDetailsText.clear();
        LPTitleField.setDisable(true);
        LPDetailsText.setDisable(true);
        editLPButton.setDisable(true);
        removeLPButton.setDisable(true);
        saveLPButton.setDisable(true);
    }
}
