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

    @FXML private Button    addLPButton;
    @FXML private Button    removeLPButton;
    @FXML private Button    editLPButton;
    @FXML private Button    saveLPButton;
    @FXML private ComboBox<LessonPlan> LPDropdown;
    @FXML private TextField LPTitleField;
    @FXML private TextArea  LPDetailsText;

    private final ObservableList<LessonPlan> plans = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearSelectionState();
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
     * Interface method: load plans for a single subject.
     */
    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadPlans();
        clearSelectionState();
    }

    /**
     * Overload used by Dashboard (when passing list of subjects).
     */
    public void setSubject(List<Subject> subjects) {
        if (subjects != null && !subjects.isEmpty()) {
            setSubject(subjects.get(0));
        }
    }

    private void loadPlans() {
        plans.clear();
        try {
            plans.addAll(lpDao.findBySubject(currentSubject.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddLP(ActionEvent event) {
        clearSelectionState();
        LPTitleField.clear();
        LPDetailsText.clear();
        LPTitleField.setDisable(false);
        LPDetailsText.setDisable(false);
        saveLPButton.setVisible(true);
        saveLPButton.setDisable(false);
    }

    @FXML
    private void onLPSelected(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        if (sel == null) return;
        LPTitleField.setText(sel.getTitle());
        LPDetailsText.setText(sel.getDetails());
        LPTitleField.setDisable(true);
        LPDetailsText.setDisable(true);
        saveLPButton.setVisible(true);
        saveLPButton.setDisable(true);
        removeLPButton.setVisible(true);
        removeLPButton.setDisable(false);
        editLPButton.setVisible(true);
        editLPButton.setDisable(false);
    }

    @FXML
    private void onEditLP(ActionEvent event) {
        LPTitleField.setDisable(false);
        LPDetailsText.setDisable(false);
        saveLPButton.setVisible(true);
        saveLPButton.setDisable(false);
        editLPButton.setVisible(false);
    }

    @FXML
    private void onSaveEditedLP(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        try {
            if (sel == null) {
                String id = UUID.randomUUID().toString();
                LessonPlan lp = new LessonPlan(id,
                        currentSubject.getId(),
                        LPTitleField.getText(),
                        LPDetailsText.getText());
                lpDao.save(lp);
                plans.add(lp);
                LPDropdown.getSelectionModel().select(lp);
            } else {
                sel.setTitle(LPTitleField.getText());
                sel.setDetails(LPDetailsText.getText());
                lpDao.update(sel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearSelectionState();
    }

    @FXML
    private void onRemoveLP(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        if (sel != null) {
            try {
                lpDao.delete(sel.getId());
                plans.remove(sel);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        clearSelectionState();
    }

    /**
     * Resets UI: only Add is visible, fields disabled.
     */
    private void clearSelectionState() {
        addLPButton.setVisible(true);
        removeLPButton.setVisible(false);
        editLPButton.setVisible(false);
        saveLPButton.setVisible(false);
        LPTitleField.setDisable(true);
        LPDetailsText.setDisable(true);
        LPDropdown.getSelectionModel().clearSelection();
    }
}
