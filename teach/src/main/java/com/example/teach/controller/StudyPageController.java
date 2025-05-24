package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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
    @FXML private ComboBox<Week> weekDropdown;
    @FXML private ListView<String> fileListView;
    @FXML
    private Label statusLabel;

    private File selectedFile;
    @FXML
    private ComboBox<Assignment> assignmentDropdown;

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
        File baseDir = new File("teach/study/" + currentSubject.getId());
        if (baseDir.exists()) {
            File[] weekDirs = baseDir.listFiles(File::isDirectory);
            if (weekDirs != null) {
                List<Week> weeks = new ArrayList<>();
                for (File dir : weekDirs) {
                    String name = dir.getName(); // e.g. "week1"
                    if (name.startsWith("week")) {
                        try {
                            int weekNumber = Integer.parseInt(name.substring(4));
                            weeks.add(new Week(weekNumber));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                // 按照周次排序
                weeks.sort(Comparator.comparingInt(Week::getNumber));
                weekDropdown.getItems().setAll(weeks);
            }
        }

    }


    @FXML
    private void onUpload() {
        if (weekDropdown.getValue() == null) {
            statusLabel.setText("Please select a week first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Teaching File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null) {
            statusLabel.setText("No file selected.");
            return;
        }

        Week week = weekDropdown.getValue();
        String destPath = "teach/study/" + currentSubject.getId() + "/week" + week.getNumber() + "/" + selectedFile.getName();

        try {
            Path target = Paths.get(destPath);
            Files.createDirectories(target.getParent());
            Files.copy(selectedFile.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            statusLabel.setText("Uploaded: " + selectedFile.getName());
            updateFileList(week); // 上传完刷新该周文件列表
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Upload failed.");
        }
    }

    @FXML
    private void onSubmit() {
        if (!(currentUser instanceof Student)) {
            statusLabel.setText("Only students can submit files.");
            return;
        }

        if (selectedFile == null) {
            statusLabel.setText("Please upload a file first.");
            return;
        }

        Week selectedWeek = weekDropdown.getValue();
        if (selectedWeek == null) {
            statusLabel.setText("Please select a week.");
            return;
        }

        Assignment assignment = assignmentDropdown.getValue();
        if (assignment == null) {
            statusLabel.setText("Please select an assignment.");
            return;
        }

        Student student = (Student) currentUser;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = student.getId() + "_" + timestamp + ".txt";
        String folderPath = "teach/study/" + currentSubject.getId() + "/week" + selectedWeek.getNumber();

        try {
            Path targetDir = Paths.get(folderPath);
            Files.createDirectories(targetDir);
            Path targetPath = targetDir.resolve(fileName);
            Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            statusLabel.setText("Uploaded: " + fileName);
            updateFileList(selectedWeek);

        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Upload failed.");
        }
    }

    private void updateFileList(Week week) {
        fileListView.getItems().clear();
        String folderPath = "teach/study/" + currentSubject.getId() + "/week" + week.getNumber();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(folderPath))) {
            for (Path file : stream) {
                String fileName = file.getFileName().toString();
                if (fileName.endsWith(".link")) {
                    String link = Files.readAllLines(file).get(0);
                    fileListView.getItems().add("🔗 " + link);
                } else {
                    fileListView.getItems().add("📄 " + fileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        if (!(u instanceof com.example.teach.model.Teacher)) {
            addWeekButton.setVisible(false);
            removeWeekButton.setVisible(false);
        }
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
        int nextWeekNumber = weekDropdown.getItems().stream()
                .mapToInt(Week::getNumber)
                .max()
                .orElse(0) + 1;

        Week newWeek = new Week(nextWeekNumber);
        weekDropdown.getItems().add(newWeek);
        weekDropdown.getSelectionModel().select(newWeek);

        String weekDirPath = "teach/study/" + currentSubject.getId() + "/week" + nextWeekNumber;
        try {
            Files.createDirectories(Paths.get(weekDirPath));
            statusLabel.setText("Added Week " + nextWeekNumber);
            updateFileList(newWeek);
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Failed to add week.");
        }
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
    @FXML
    private void goBack() {
        if (dashboardController != null) {
            dashboardController.goToDashboard(null);
        }
    }

    @FXML
    private void onUploadLink() {
        if (weekDropdown.getValue() == null) {
            statusLabel.setText("Please select a week first.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Upload Link");
        dialog.setHeaderText("Enter a link to upload:");
        dialog.setContentText("URL:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(link -> {
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                statusLabel.setText("Invalid link.");
                return;
            }

            Week week = weekDropdown.getValue();
            String fileName = "link_" + System.currentTimeMillis() + ".link";
            String filePath = "teach/study/" + currentSubject.getId() + "/week" + week.getNumber() + "/" + fileName;

            try {
                Files.createDirectories(Paths.get(filePath).getParent());
                Files.write(Paths.get(filePath), Collections.singletonList(link));
                statusLabel.setText("Link uploaded.");
                updateFileList(week);
            } catch (IOException e) {
                e.printStackTrace();
                statusLabel.setText("Failed to save link.");
            }
        });
    }



    // TODO: Add @FXML methods for handling Study page actions (e.g., resource loading, quizzes)
}