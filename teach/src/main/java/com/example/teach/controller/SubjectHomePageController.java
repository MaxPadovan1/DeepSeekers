package com.example.teach.controller;

import com.example.teach.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

/**
 * Controller for the Subject Home Page within the Dashboard.
 * <p>
 * Manages the display of the default home pane and navigation
 * to various subject-specific sections (Study, Homework, Test, etc.).
 * Relies on {@link SectionControllerBase} for section controllers.
 */
public class SubjectHomePageController {

    /** The authenticated user for this session. */
    private User currentUser;
    /** The subject currently being viewed. */
    private Subject currentSubject;
    /** Reference to the DashboardController for navigation and UI updates. */
    private DashboardController dashboardController;
    /** Root layout for this subject page, with a center region for content. */
    @FXML private BorderPane subjectRoot;
    /** The default "home" content pane, shown when returning home. */
    @FXML private VBox defaultContent;
    @FXML private TextArea subjectInfoArea;
    @FXML private TextArea teacherInfoArea;
    @FXML private VBox assignmentBox;

    /**
     * Injects the authenticated user into this controller.
     * Called by DashboardController immediately after loading this view.
     *
     * @param u the logged-in {@link User}
     */
    public void setUser(User u) {
        this.currentUser = u;
    }

    /**
     * Injects the current subject into this controller.
     * Called by DashboardController immediately after loading this view.
     *
     * @param s the {@link Subject} to display
     */
    public void setSubject(Subject s) {
        this.currentSubject = s;
        onGoHome();
    }

    /**
     * Injects the DashboardController for cross-controller navigation.
     * Called by DashboardController immediately after loading this view.
     *
     * @param d the parent {@link DashboardController}
     */
    public void setDashboardController(DashboardController d) {
        this.dashboardController = d;
    }

    /**
     * Restores the original home view in the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML
    private void onGoHome() {
        subjectRoot.setCenter(defaultContent);
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName());

        try {
            // --- Subject Description via Name from DB ---
            SubjectDAO subjectDAO = new SubjectDAO();
            Subject subjectFromDb = subjectDAO.findById(currentSubject.getId());

            if (subjectFromDb != null) {
                String name = subjectFromDb.getName();
                String description = switch (name) {
                    case "Calculus I" -> """
                                            Calculus I introduces the fundamental concepts of limits, derivatives, and integrals.
                                            Students explore how functions change, apply differentiation to solve real-world problems,
                                            and learn integration techniques. The course builds a strong foundation for advanced
                                            mathematics, physics, and engineering, emphasizing both theory and practical problem-solving
                                            using graphical and analytical methods.
                                            """;

                    case "English Literature" -> """
                                                This course explores major works of English literature, from Shakespeare to modern authors.
                                                Students analyze themes, characters, and historical context while developing critical thinking
                                                and writing skills. Through essays, discussions, and readings, the course fosters appreciation
                                                for literary style and cultural influence across genres like poetry, drama, and fiction.
                                                """;

                    case "Intro to Programming" -> """
                                                    Intro to Programming teaches foundational coding skills using Java.
                                                    Students learn about variables, conditionals, loops, functions, and object-oriented principles.
                                                    Emphasis is placed on solving real-world problems through hands-on coding.
                                                    This course prepares students for further studies in software development, computer science,
                                                    and algorithmic thinking through projects and lab exercises.
                                                    """;

                    case "World History" -> """
                                            World History examines key events, civilizations, and movements from ancient times to the modern era.
                                            Topics include empires, revolutions, and global conflicts. Students gain insights into political,
                                            economic, and cultural transformations, enhancing their understanding of how the past shapes the present.
                                            Primary sources and discussions encourage critical historical thinking.
                                            """;

                    case "General Biology" -> """
                                            Introduces key topics in biology, including cells, genetics, evolution, and ecology.
                                            Students develop scientific literacy through hands-on labs, critical analysis, and application
                                            of biological principles. Prepares students for further studies in life sciences and health.
                                            """;

                    default -> "No description available.";
                };
                subjectInfoArea.setText(description);
            } else {
                subjectInfoArea.setText("Subject info not found.");
            }

            // --- Teacher Info ---
            Teacher teacher = subjectDAO.findTeacherBySubject(currentSubject.getId());
            if (teacher != null) {
                teacherInfoArea.setText("Teacher Name: " + teacher.getFirstName() + " " + teacher.getLastName() + "\nTeacher Email: " + teacher.getEmail());
            } else {
                teacherInfoArea.setText("No teacher assigned.");
            }

            // --- Released Assignments ---
            AssignmentDAO assignmentDAO = new AssignmentDAO();
            assignmentBox.getChildren().clear();
            for (Assignment a : assignmentDAO.getReleasedAssignments(currentSubject.getId())) {
                Label label = new Label("Title:" + a.getTitle() + " – Due: " + a.getDueDate());
                assignmentBox.getChildren().add(label);
            }

        } catch (Exception e) {
            subjectInfoArea.setText("Error loading subject info.");
            teacherInfoArea.setText("Error loading teacher.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the Study section into the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML
    private void onStudy() {
        if (currentUser instanceof Teacher) {
            loadSection("TeacherStudyPage.fxml");
        } else {
            loadSection("studentstudypage.fxml");
        }
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Study");
    }

    /**
     * Loads the Homework section into the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML private void onHomework() {
        loadSection("HomeworkPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Homework");
    }

    /**
     * Placeholder for Test section: currently logs a click event.
     */
    @FXML private void onTest() {
        loadSection("TestPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Test");
    }

    /**
     * Loads the Assignment section into the center pane.
     * Updates the dashboard title accordingly.
     */
    @FXML
    private void onAssignment() {
        loadSection("AssignmentPage.fxml");
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Assignment");
    }



    /**
     * Placeholder for Grade section: currently logs a click event.
     */
    @FXML private void onGrade() {
        boolean isTeacher = currentUser instanceof Teacher;
        String fxmlName = isTeacher ? "HomeWorkGradePage.fxml" : "studentgradepage.fxml";
        loadSection(fxmlName);
        dashboardController.setPageLabel("Dashboard / " + currentSubject.getName() + " / Grade");
    }


    /**
     * Helper to load a section FXML into the center pane, performing
     * dependency injection for its controller if it extends SectionControllerBase.
     *
     * @param fxmlName name of the FXML file in the com/example/teach package
     */
    private void loadSection(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/example/teach/" + fxmlName
                    )
            );
            Node section = loader.load();

            Object ctrl = loader.getController();
            if (ctrl instanceof SectionControllerBase base) {
                base.setUser(currentUser);
                base.setSubject(currentSubject);
                base.setDashboardController(dashboardController);
            }

            subjectRoot.setCenter(section);
        } catch (IOException e) {
            e.printStackTrace();
            // Consider showing an Alert in production code
        }
    }
}