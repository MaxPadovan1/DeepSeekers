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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private User currentUser;
    private List<Subject> subjects;
    private Node originalCenter;

    @FXML private BorderPane rootBorderPane;
    @FXML private VBox drawer;
    @FXML private MenuButton userMenu;
    @FXML private VBox subject1Tile;
    @FXML private VBox subject2Tile;
    @FXML private VBox subject3Tile;
    @FXML private VBox subject4Tile;

    /** Called by FXMLLoader after the @FXML injections. */
    @Override public void initialize(URL location, ResourceBundle resources) {
        // store the original UI center so we can restore it later
        originalCenter = rootBorderPane.getCenter();
    }

    /** Called immediately after FXMLLoader.load() in LoginPageController. */
    public void setUser(User user) {
        this.currentUser = user;
        userMenu.setText(user.getFirstName() + " " + user.getLastName());

        // build the list of subjects for student or teacher
        if (user instanceof Student s) {
            subjects = s.getSubjects();
        } else if (user instanceof Teacher t) {
            subjects = List.of(t.getSubject());
        } else {
            subjects = List.of();
        }

        // populate or hide the four subject tiles
        List<VBox> tiles = List.of(subject1Tile, subject2Tile, subject3Tile, subject4Tile);
        for (int i = 0; i < tiles.size(); i++) {
            VBox tile = tiles.get(i);
            if (i < subjects.size()) {
                Subject subj = subjects.get(i);
                Label lbl = (Label) tile.getChildren().get(0);
                lbl.setText(subj.getName());
                tile.setVisible(true);
                tile.setManaged(true);
            } else {
                tile.setVisible(false);
                tile.setManaged(false);
            }
        }
    }

    /** Expose the root pane so child controllers can swap its center. */
    public BorderPane getRootBorderPane() {
        return rootBorderPane;
    }

    /** Toggle the side‐drawer open/closed. */
    @FXML public void toggleDrawer()
    {
        boolean open = drawer.isVisible();
        drawer.setVisible(!open);
        drawer.setManaged(!open);
    }

    /** Hide drawer & restore the original dashboard grid. */
    @FXML public void goToDashboard(MouseEvent ev)
    {
        drawer.setVisible(false);
        drawer.setManaged(false);
        rootBorderPane.setCenter(originalCenter);
    }

    @FXML public void goToProfile(MouseEvent ev)
    {
        drawer.setVisible(false);
        drawer.setManaged(false);
        loadCenter("/com/example/teach/ProfilePage.fxml");

//        try {
//            // 1) load the ProfilePage FXML
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/com/example/teach/ProfilePage.fxml")
//            );
//            Parent profileRoot = loader.load();
//
//            // 2) inject the same user into its controller
//            ProfilePageController profileCtrl = loader.getController();
//            profileCtrl.setUser(currentUser);
//
//            // 3) swap the entire Scene on the current Stage
//            Stage stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(profileRoot, 1280, 720));
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @FXML public void goToClassInfo(MouseEvent ev)
    {
        drawer.setVisible(false);
        drawer.setManaged(false);
        loadCenter("/com/example/teach/HomePage-view.fxml");
    }

    @FXML public void goToLessonPlan(MouseEvent ev)
    {
        drawer.setVisible(false);
        drawer.setManaged(false);
        loadCenter("/com/example/teach/LessonPlan-view.fxml");
    }

    @FXML private void showNotifications() { /* … */ }

    // --- Subject tile clicks: load ClassHomePage into center ---------

    @FXML private void onSubject1Clicked(MouseEvent ev) { openClassInfo(0); }
    @FXML private void onSubject2Clicked(MouseEvent ev) { openClassInfo(1); }
    @FXML private void onSubject3Clicked(MouseEvent ev) { openClassInfo(2); }
    @FXML private void onSubject4Clicked(MouseEvent ev) { openClassInfo(3); }

    private void openClassInfo(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/HomePage-view.fxml"));
            Parent view = loader.load();

            // pass user, subject, and this controller into the new child
            ClassHomePageController ctrl = loader.getController();
            ctrl.setUser(currentUser);
            ctrl.setSubject(subjects.get(index));
            ctrl.setDashboardController(this);

            rootBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper to load any FXML into center, injecting the user (and subject if applicable).
     */
    private void loadCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // if the controller needs the user:
            Object ctrl = loader.getController();
            if (ctrl instanceof ProfilePageController p) {
                p.setUser(currentUser);
                p.setDashboardController(this);
            }
            if (ctrl instanceof ClassHomePageController c) {
                c.setUser(currentUser);
                // find the index of the clicked subject
                // you could store lastIndex in a field if you want
            }
            // (and similarly for LessonPlanController, etc.)

            rootBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
