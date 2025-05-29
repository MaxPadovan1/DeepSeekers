package com.example.teach.controller;

import com.example.teach.model.Subject;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class StudentStudyPageController implements SectionControllerBase{

    @FXML private Accordion weekAccordion;

    private User currentUser;
    private Subject currentSubject;


    private void loadWeekTitledPanes(String subjectId) {
        weekAccordion.getPanes().clear();

        for (int week = 1; week <= 12; week++) {
            String folderPath = "teach/study/" + subjectId + "/week" + week + "/released";
            Path folder = Paths.get(folderPath);

            if (!Files.exists(folder)) {
                continue;
            }

            String titleText = "No title available";
            Path titlePath = folder.resolve("title.txt");
            if (Files.exists(titlePath)) {
                try {
                    titleText = Files.readString(titlePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Label titleLabel = new Label(titleText);
            Button openButton = new Button("Open Files");

            int finalWeek = week;
            openButton.setOnAction(e -> loadFilesForWeek(subjectId, finalWeek));

            VBox content = new VBox(10, titleLabel, openButton);
            content.setPadding(new Insets(10));

            TitledPane weekPane = new TitledPane("Week " + week, content);
            weekAccordion.getPanes().add(weekPane); // ✅ 添加 TitledPane
        }

        if (weekAccordion.getPanes().isEmpty()) {
            TitledPane empty = new TitledPane("No Content Available",
                    new Label("No files have been released yet."));
            empty.setExpanded(true);
            weekAccordion.getPanes().add(empty);
        }
    }

    private void loadFilesForWeek(String subjectId, int weekNumber) {
        String folderPath = "teach/study/" + subjectId + "/week" + weekNumber + "/released";
        Path folder = Paths.get(folderPath);

        if (!Files.exists(folder)) {
            showAlert("No released files found.");
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Released Files");
        dialog.setHeaderText("Week " + weekNumber + " Files:");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
            for (Path file : stream) {
                if (!Files.isDirectory(file) && !file.getFileName().toString().equals("title.txt")) {
                    String fileName = file.getFileName().toString();

                    Button fileButton = new Button(fileName);
                    fileButton.setOnAction(e -> openFile(file.toFile())); // ✅ 点击打开文件
                    vbox.getChildren().add(fileButton);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to load files.");
            return;
        }

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void openFile(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                showAlert("Desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to open file: " + file.getName());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @Override
    public void setUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void setSubject(Subject subject) {
        this.currentSubject = subject;
        loadWeekTitledPanes(String.valueOf(subject.getId()));
    }

    @Override
    public void setDashboardController(DashboardController dashboardController) {

    }
}