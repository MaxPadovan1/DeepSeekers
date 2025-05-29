package com.example.teach.controller;

import com.example.teach.model.SQLiteDAO;
import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class TeacherStudyPageController implements SectionControllerBase{

    @FXML private ComboBox<String> weekDropdown;
    @FXML private TextField titleInput;
    @FXML private Label statusLabel;
    @FXML private ListView<String> fileListView;
    @FXML private Button uploadButton;
    @FXML private Button releaseButton;
    @FXML private Button backToTeacherViewButton;
    private User currentUser;
    private Subject currentSubject;


    @FXML
    public void initialize() {
        weekDropdown.getItems().addAll("Week 1", "Week 2", "Week 3", "Week 4");
        weekDropdown.getSelectionModel().selectFirst();


        fileListView.setCellFactory(list -> new ListCell<>() {
            private final HBox content = new HBox(10);
            private final Label fileLabel = new Label();
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(e -> {
                    String fileName = getItem();
                    if (fileName != null) {
                        deleteFile(fileName);
                    }
                });
                content.getChildren().addAll(fileLabel, deleteButton);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    fileLabel.setText(item);
                    setGraphic(content);
                }
            }
        });
    }



    private void deleteFile(String fileName) {
        String subjectId = currentSubject.getId();
        String selectedWeekName = weekDropdown.getValue();
        if (selectedWeekName == null) {
            showAlert("Please select a week.");
            return;
        }

        int weekNumber = extractWeekNumber(selectedWeekName);
        Path filePath = Paths.get("teach/study/" + subjectId + "/week" + weekNumber + "/" + fileName);

        try {
            Files.deleteIfExists(filePath);
            statusLabel.setText("Deleted: " + fileName);
            updateFileList(weekNumber);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to delete file: " + fileName);
        }
    }


    @FXML
    private void handleUpload() {
        String subjectId = currentSubject.getId();
        String selectedWeekName = weekDropdown.getValue();
        if (selectedWeekName == null) {
            statusLabel.setText("Please select a week.");
            return;
        }

        int weekNumber = extractWeekNumber(selectedWeekName);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Course Content.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                String title = selectedFile.getName().replaceAll("\\.(txt|pdf)", "");

                // Store in DB
                SQLiteDAO.saveStudyFile(
                        subjectId,
                        "Week " + weekNumber,
                        selectedFile.getName(),
                        title,
                        fileContent,
                        true // released immediately
                );

                // (Optional) Save on disk for legacy view
                String folderPath = "teach/study/" + subjectId + "/week" + weekNumber;
                Files.createDirectories(Paths.get(folderPath));
                Path targetPath = Paths.get(folderPath, selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                statusLabel.setText("Uploaded and released: " + selectedFile.getName());
                updateFileList(weekNumber);

            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setText("Upload failed.");
            }
        } else {
            statusLabel.setText("No file selected.");
        }
    }


    @FXML
    private void handleRelease() {
        String selectedWeek = weekDropdown.getValue();
        String title = titleInput.getText().trim();

        if (selectedWeek == null || title.isEmpty()) {
            showAlert("Please select a week and enter a title.");
            return;
        }

        int weekNumber = extractWeekNumber(selectedWeek);
        String subjectId = currentSubject.getId();
        String sourceFolder = "teach/study/" + subjectId + "/week" + weekNumber;
        String releaseFolder = sourceFolder + "/released";

        try {
            Files.createDirectories(Paths.get(releaseFolder));

            boolean hasFiles = false;
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(sourceFolder))) {
                for (Path file : stream) {
                    if (!Files.isDirectory(file)) {
                        Path target = Paths.get(releaseFolder, file.getFileName().toString());
                        Files.copy(file, target, StandardCopyOption.REPLACE_EXISTING);
                        hasFiles = true;
                    }
                }
            }

            if (!hasFiles) {
                showAlert("No files to release.");
                return;
            }

            Path titlePath = Paths.get(releaseFolder, "title.txt");
            Files.writeString(titlePath, title);

            showAlert("Released successfully to Week " + weekNumber);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Release failed.");
        }
    }

    private void updateFileList(int weekNumber) {
        String subjectId = currentSubject.getId();
        fileListView.getItems().clear();
        String folderPath = "teach/study/" + subjectId + "/week" + weekNumber;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath))) {
            for (Path path : stream) {
                fileListView.getItems().add(path.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int extractWeekNumber(String weekText) {
        return Integer.parseInt(weekText.replaceAll("[^0-9]", ""));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
    }

    @Override
    public void setDashboardController(DashboardController dashboardController) {

    }
}
