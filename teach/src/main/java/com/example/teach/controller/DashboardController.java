package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller for the main Dashboard view.
 * <p>
 * Manages the navigation drawer, user session, and dynamic loading of
 * different center panes (Profile, Class Info, Subject pages, etc.).
 * Implements {@link Initializable} to capture the original dashboard layout.
 */
public class DashboardController implements Initializable {

    /** The authenticated user for this session. */
    private User currentUser;
    /** The list of subjects associated with the current user. */
    private List<Subject> subjects;
    /** Saved reference to the original dashboard center pane. */
    private Node originalCenter;

    @FXML private BorderPane rootBorderPane;
    @FXML private VBox drawer;
    @FXML private MenuButton userMenu;
    @FXML private Label pageLabel;
    @FXML private VBox subject1Tile;
    @FXML private VBox subject2Tile;
    @FXML private VBox subject3Tile;
    @FXML private VBox subject4Tile;
    @FXML private Label greetingLabel;
    @FXML private Label lessonPlanLabel;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Saves the default center pane for later restoring when returning to the dashboard.
     *
     * @param location  URL of the FXML file
     * @param resources resource bundle for localization (unused)
     */
    @Override public void initialize(URL location, ResourceBundle resources) {
        this.originalCenter = rootBorderPane.getCenter();

    }

    /**
     * Injects the authenticated user into the dashboard.
     * Updates the user menu label and populates subject tiles based on role.
     * <p>
     * Called by {@code LoginPageController} immediately after loading.
     *
     * @param user the logged-in {@link User}
     */
    public void setUser(User user) {
        this.currentUser = user;
        userMenu.setText(user.getFirstName() + " " + user.getLastName());
        greetingLabel.setText(buildGreeting(user));
        pageLabel.setText("Dashboard");

        if (user instanceof Student s) {
            subjects = s.getSubjects();
            lessonPlanLabel.setVisible(false);   // hide from students
            lessonPlanLabel.setManaged(false);
        } else if (user instanceof Teacher t) {
            subjects = List.of(t.getSubject());
            lessonPlanLabel.setVisible(true);    // show for teachers
            lessonPlanLabel.setManaged(true);
        } else {
            subjects = List.of();
            lessonPlanLabel.setVisible(false);
            lessonPlanLabel.setManaged(false);
        }

        List<VBox> tiles = List.of(subject1Tile, subject2Tile, subject3Tile, subject4Tile);
        for (int i = 0; i < tiles.size(); i++) {
            VBox tile = tiles.get(i);
            if (i < subjects.size()) {
                Subject subj = subjects.get(i);
                ((Label) tile.getChildren().get(0)).setText(subj.getName());
                tile.setVisible(true);
                tile.setManaged(true);
            } else {
                tile.setVisible(false);
                tile.setManaged(false);
            }
        }
    }
    private String buildGreeting(User user) {
        String role = (user instanceof Teacher) ? "Teacher" :
                (user instanceof Student) ? "Student" : "User";
        String name = user.getFirstName();

        int hour = java.time.LocalTime.now().getHour();
        String timeGreeting;
        if (hour < 12) {
            timeGreeting = "Good morning";
        } else if (hour < 18) {
            timeGreeting = "Good afternoon";
        } else {
            timeGreeting = "Good evening";
        }

        return timeGreeting + ", " + role + " " + name + "!";
    }
    /**
     * Updates the page title label.
     *
     * @param text new title text
     */
    public void setPageLabel(String text) {
        pageLabel.setText(text);
    }

    /**
     * Toggles visibility of the side drawer (hamburger menu).
     */
    @FXML public void toggleDrawer() {
        boolean open = drawer.isVisible();
        drawer.setVisible(!open);
        drawer.setManaged(!open);
    }

    /**
     * Returns the user to the original dashboard center pane.
     *
     * @param ev mouse event from clicking the Dashboard button
     */
    @FXML public void goToDashboard(MouseEvent ev) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        rootBorderPane.setCenter(originalCenter);
        setPageLabel("Dashboard");
    }

    /**
     * Loads the Profile page into the center pane and injects dependencies.
     *
     * @param ev mouse event from clicking the Profile menu item
     */
    @FXML public void goToProfile(MouseEvent ev) {
        navigateTo("/com/example/teach/ProfilePage.fxml", "Dashboard / Profile",
                ctrl -> {
                    if (ctrl instanceof ProfilePageController p) {
                        p.setDashboardController(this);
                        p.setUser(currentUser);
                    }
                });
    }

    /**
     * Loads the Class Info view into the center pane and injects dependencies.
     *
     * @param ev mouse event from clicking the Class Info menu item
     */
    @FXML public void goToClassInfo(MouseEvent ev) {
        navigateTo("/com/example/teach/ClassInfo-view.fxml", "Dashboard / Class Info",
                ctrl -> {
                    if (ctrl instanceof ClassInfoController c) {
                        c.setDashboardController(this);
                        c.setUser(currentUser);
                        c.setSubjects(subjects);
                    }
                });
    }

    /**
     * Placeholder for Lesson Plan action; currently logs a click event.
     *
     * @param ev mouse event from clicking the Lesson Plan menu item
     */
    @FXML
    public void goToLessonPlan(MouseEvent ev) {
        navigateTo("/com/example/teach/LessonPlan-view.fxml", "Dashboard / Lesson Plan",
                ctrl -> {
                    if (ctrl instanceof LessonPlanController lp) {
                        lp.setDashboardController(this);
                        lp.setUser(currentUser);
                        lp.setSubject(subjects);
                    }
                });
    }
    @FXML
    public void goToGradePage(MouseEvent ev) {
        String fxml;
        if (currentUser instanceof Teacher) {
            fxml = "/com/example/teach/homeworkgradepage.fxml";
        } else {
            fxml = "/com/example/teach/studentgradepage.fxml";
        }

        navigateTo(fxml, "Dashboard / Grade Page", ctrl -> {
            if (ctrl instanceof SectionControllerBase c) {
                c.setDashboardController(this);
                c.setUser(currentUser);
                if (currentUser instanceof Teacher t) {
                    c.setSubject(t.getSubject());
                } else if (currentUser instanceof Student s && !s.getSubjects().isEmpty()) {
                    c.setSubject(s.getSubjects().get(0)); // assuming single subject
                }
            }
        });
    }

    /**
     * Stub for showing notifications (implementation omitted).
     */
    @FXML
    private void showNotifications() {
        // TODO: implement notifications view
    }

    /**
     * Handles clicks on subject tiles by index.
     *
     * @param ev mouse event from clicking a subject tile
     */
    @FXML private void onSubject1Clicked(MouseEvent ev) { goToSubject(0); }
    @FXML private void onSubject2Clicked(MouseEvent ev) { goToSubject(1); }
    @FXML private void onSubject3Clicked(MouseEvent ev) { goToSubject(2); }
    @FXML private void onSubject4Clicked(MouseEvent ev) { goToSubject(3); }

    /**
     * Loads the given subject view into center with controller injection.
     *
     * @param index position in subjects list
     */
    private void goToSubject(int index) {
        Subject subj = subjects.get(index);
        navigateTo("/com/example/teach/SubjectHomePage-view.fxml",
                "Dashboard / " + subj.getName(), ctrl -> {
                    if (ctrl instanceof SubjectHomePageController sh) {
                        sh.setDashboardController(this);
                        sh.setUser(currentUser);
                        sh.setSubject(subj);
                    }
                });
    }


    /**
     * Helper to load an FXML into the center pane, set title, and initialize controller.
     *
     * @param fxmlPath         classpath to the FXML file
     * @param pageTitle        title to set on the page label
     * @param initController   consumer to perform additional controller setup
     */
    public void navigateTo(String fxmlPath, String pageTitle, Consumer<Object> initController) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        setPageLabel(pageTitle);
        loadCenter(fxmlPath, initController);
    }

    /**
     * Loads the specified FXML into the center pane, then runs controller init.
     *
     * @param fxmlPath       classpath to the FXML file
     * @param initController consumer that accepts the controller instance
     */
    private void loadCenter(String fxmlPath, Consumer<Object> initController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            initController.accept(loader.getController());
            rootBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles the logout action triggered from the user menu.
     * Loads the login page FXML and replaces the current scene's root with the login view,
     * effectively logging the user out and returning them to the login screen.
     *
     * @param event the ActionEvent triggered by clicking the "Logout" menu item
     */
    @FXML
    private void handleLogout(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/LoginPage-view.fxml"));
            Parent loginRoot = loader.load();

            // Get current stage and scene
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            Scene currentScene = stage.getScene();

            // Replace the root of the current scene without changing size or stage
            currentScene.setRoot(loginRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}