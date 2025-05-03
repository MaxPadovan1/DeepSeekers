package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class DashboardController implements Initializable {

    private User currentUser; //Current logged in user
    private List<Subject> subjects; //List of units the user is doing
    private Node originalCenter; //Place to save the dashboard center pane to load when we return to dashboard

    @FXML private BorderPane rootBorderPane;
    @FXML private VBox drawer;
    @FXML private MenuButton userMenu;
    @FXML private Label pageLabel;
    @FXML private VBox subject1Tile;
    @FXML private VBox subject2Tile;
    @FXML private VBox subject3Tile;
    @FXML private VBox subject4Tile;

    @Override public void initialize(URL location, ResourceBundle resources) {
        // Save the dashboard center pane
        originalCenter = rootBorderPane.getCenter();
    }

    /** Called by LoginPageController right after load(). */
    public void setUser(User user) {
        this.currentUser = user;
        userMenu.setText(user.getFirstName() + " " + user.getLastName()); //Update the top right dropdown with the users name
        pageLabel.setText("Dashboard"); //Set current page title

        //Build subject list depending on student or teacher
        if (user instanceof Student s) {
            subjects = s.getSubjects();
        } else if (user instanceof Teacher t) {
            subjects = List.of(t.getSubject());
        } else {
            subjects = List.of();
        }

        // Populate up to 4 tiles
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

    public void setPageLabel(String text) {
        pageLabel.setText(text);
    }

    //Show or hide the hamburger menu
    @FXML public void toggleDrawer() {
        boolean open = drawer.isVisible();
        drawer.setVisible(!open);
        drawer.setManaged(!open);
    }

    //Return to original dashboard grid view
    @FXML public void goToDashboard(MouseEvent ev) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        rootBorderPane.setCenter(originalCenter);
        setPageLabel("Dashboard");
    }

    /** VVV ---- Buttons Within The Hamburger Menu ---- VVV */
    //Load the profile pane into the center
    @FXML public void goToProfile(MouseEvent ev) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        setPageLabel("Dashboard / Profile");
        loadCenter("/com/example/teach/ProfilePage.fxml", ctrl -> {
            if (ctrl instanceof ProfilePageController p) {
                p.setDashboardController(this);
                p.setUser(currentUser);
            }
        });
    }

    //Load the class info pane into the center
    @FXML public void goToClassInfo(MouseEvent ev) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        setPageLabel("Dashboard / Class Info");
        loadCenter("/com/example/teach/ClassInfo-view.fxml", ctrl -> {
            if (ctrl instanceof ClassInfoController c) {
                c.setDashboardController(this);
                c.setUser(currentUser);
                c.setSubjects(subjects);
            }
        });
    }

    //Load the lesson plan into the center
    @FXML public void goToLessonPlan(MouseEvent ev) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        //setPageLabel("Dashboard / Lesson Plan");
        //loadCenter("/com/example/teach/LessonPlan-view.fxml");
        System.out.println("Clicked on Lesson plan");
    }

    @FXML private void showNotifications() { /* … */ }

    // Subject‐tile clicks delegate to a common method
    @FXML private void onSubject1Clicked(MouseEvent ev) { goToSubject(0); }
    @FXML private void onSubject2Clicked(MouseEvent ev) { goToSubject(1); }
    @FXML private void onSubject3Clicked(MouseEvent ev) { goToSubject(2); }
    @FXML private void onSubject4Clicked(MouseEvent ev) { goToSubject(3); }

    private void goToSubject(int index) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        Subject subj = subjects.get(index);
        setPageLabel("Dashboard / " + subj.getName());
        loadCenter("/com/example/teach/SubjectHomePage-view.fxml", ctrl -> {
            if (ctrl instanceof SubjectHomePageController sh) {
                sh.setDashboardController(this);
                sh.setUser(currentUser);
                sh.setSubject(subj);
            }
        });
    }

    /** Overload: no extra injection needed. */
    private void loadCenter(String fxmlPath) {
        loadCenter(fxmlPath, ctrl -> { /* no-op */ });
    }

    /** Core helper: loads FXML, lets you initialize its controller, then swaps it in. */
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
}
