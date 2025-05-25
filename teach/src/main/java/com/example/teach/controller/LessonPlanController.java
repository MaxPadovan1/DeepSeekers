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
import javafx.scene.control.*;
import javafx.util.StringConverter;
import com.example.teach.model.AIService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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

    private void prepareManualEntry() {
        clearSelectionState();
        LPTitleField.clear();
        LPDetailsText.clear();
        LPTitleField.setDisable(false);
        LPTitleField.setEditable(true);
        LPDetailsText.setDisable(false);
        LPDetailsText.setEditable(true);
        saveLPButton.setVisible(true);
        saveLPButton.setDisable(false);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearSelectionState();

        // Setup ComboBox with custom display
        LPDropdown.setItems(plans);
        LPDropdown.setCellFactory(cb -> new ListCell<LessonPlan>() {
            @Override
            protected void updateItem(LessonPlan item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });
        LPDropdown.setConverter(new StringConverter<LessonPlan>() {
            @Override
            public String toString(LessonPlan lp) {
                return lp == null ? "" : lp.getTitle();
            }
            @Override
            public LessonPlan fromString(String string) {
                return null; // not needed
            }
        });
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

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadPlans();
        clearSelectionState();
    }

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
    private void onLPSelected(ActionEvent event) {
        LessonPlan sel = LPDropdown.getValue();
        if (sel == null) return;
        LPTitleField.setText(sel.getTitle());
        LPDetailsText.setText(sel.getDetails());
        LPTitleField.setDisable(false);
        LPTitleField.setEditable(false);
        LPDetailsText.setDisable(false);
        LPDetailsText.setEditable(false);

        removeLPButton.setVisible(true);
        removeLPButton.setDisable(false);
        editLPButton.setVisible(true);
        editLPButton.setDisable(false);
    }

    @FXML
    private void onEditLP(ActionEvent event) {
        LPTitleField.setDisable(false);
        LPTitleField.setEditable(true);
        LPDetailsText.setDisable(false);
        LPDetailsText.setEditable(true);
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
                LessonPlan lp = new LessonPlan(
                        id,
                        currentSubject.getId(),
                        LPTitleField.getText(),
                        LPDetailsText.getText());
                lpDao.save(lp);
                plans.add(lp);
                LPDropdown.getSelectionModel().select(lp);
            } else {
                sel.setTitle(LPTitleField.getText());
                sel.setDetails(LPDetailsText.getText());
                LPDetailsText.setEditable(true);
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

    private void clearSelectionState() {
        addLPButton.setVisible(true);
        removeLPButton.setVisible(false);
        editLPButton.setVisible(false);
        saveLPButton.setVisible(false);
        LPTitleField.setDisable(true);
        LPDetailsText.setDisable(true);
        LPDropdown.getSelectionModel().clearSelection();
    }
    @FXML
    private void onAddLP(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Create Lesson Plan");
        alert.setHeaderText("Do you want to auto-generate the lesson plan using AI?");
        alert.setContentText("Choose 'Yes' to use AI, or 'No' to enter the plan manually.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == yesButton) {
                showWeekSelectionAndGenerateAI();
            } else if (result.get() == noButton) {
                prepareManualEntry();
            } // else Cancel — do nothing
        }
    }
    private void showWeekSelectionAndGenerateAI() {
        List<String> weeks = List.of(
                "Week 1: Understanding Computers and Programming Languages",
                "Week 2: Variables, Data Types, and Expressions",
                "Week 3: Control Flow – if, else, switch",
                "Week 4: Loops – while, for, do-while",
                "Week 5: Methods and Modular Programming",
                "Week 6: Arrays and Collections",
                "Week 7: Object-Oriented Programming Basics",
                "Week 8: Exception Handling and File I/O"
        );

        ChoiceDialog<String> dialog = new ChoiceDialog<>(weeks.get(0), weeks);
        dialog.setTitle("Select Week");
        dialog.setHeaderText("Choose the week for which to generate the lesson plan.");
        dialog.setContentText("Week:");

        Optional<String> selected = dialog.showAndWait();
        selected.ifPresent(this::generateAIForWeek);
    }
    private void generateAIForWeek(String selectedWeek) {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("syllabus/course_content.txt");
            if (resourceUrl == null) {
                LPDetailsText.setText("❌ Syllabus file not found in resources.");
                return;
            }

            Path path = Path.of(resourceUrl.toURI());
            String fullSyllabus = Files.readString(path);

            String prompt = "Generate a lesson plan for the following course week:\n\n"
                    + selectedWeek + "\n\n"
                    + "Include:\n"
                    + "- 2 to 3 learning objectives\n"
                    + "- 2 to 3 key topics\n"
                    + "- 1 to 2 suggested activities\n"
                    + "Keep it under 200 words.\n"
                    + "Here is the course context:\n\n"
                    + fullSyllabus;


            String aiOutput = AIService.getInstance().generateLessonPlan(prompt);

            LPTitleField.setText(selectedWeek);
            LPDetailsText.setText(aiOutput);

            LPTitleField.setDisable(false);
            LPTitleField.setEditable(true);
            LPDetailsText.setDisable(false);
            LPDetailsText.setEditable(true);
            saveLPButton.setVisible(true);
            saveLPButton.setDisable(false);

        } catch (Exception e) {
            LPDetailsText.setText("❌ Failed to generate AI lesson plan.");
            e.printStackTrace();
        }
    }




}
