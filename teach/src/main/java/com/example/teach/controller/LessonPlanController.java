package com.example.teach.controller;

import com.example.teach.model.LessonPlan;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class LessonPlanController implements SectionControllerBase, Initializable
{
    private User currentUser;
    private DashboardController dashboardController;

    @FXML private Button addLPButton;
    @FXML private Button removeLPButton;
    @FXML private Button editLPButton;
    @FXML private Button saveLPButton;
    @FXML private ComboBox<LessonPlan> LPDropdown;
    @FXML private TextField LPTitleField;
    @FXML private TextArea LPDetailsText;

    @Override public void initialize(URL url, ResourceBundle resourceBundle)
    {
        removeLPButton.setDisable(true);
        editLPButton.setDisable(true);
        saveLPButton.setDisable(true);
        LPTitleField .setDisable(true);
        LPDetailsText.setDisable(true);
    }

    @Override public void setUser(User user)
    {
        this.currentUser = user;
    }

    @Override public void setDashboardController(DashboardController dash)
    {
        this.dashboardController = dash;
    }

    @Override public void setSubject(com.example.teach.model.Subject subject)
    {
        // Optional future implementation
    }
}