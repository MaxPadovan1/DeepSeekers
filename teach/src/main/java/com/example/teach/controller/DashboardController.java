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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // store the original center so we can restore it later
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
    @FXML
    public void toggleDrawer() {
        boolean open = drawer.isVisible();
        drawer.setVisible(!open);
        drawer.setManaged(!open);
    }

    /** Hide drawer & restore the original dashboard grid. */
    @FXML
    public void goToDashboard(MouseEvent ev) {
        drawer.setVisible(false);
        drawer.setManaged(false);
        rootBorderPane.setCenter(originalCenter);
    }

    @FXML private void showNotifications() { /* … */ }
    @FXML private void goBack() { /* … */ }

    // --- Subject tile clicks: load ClassHomePage into center ---------

    @FXML private void onSubject1Clicked(MouseEvent ev) { openClassInfo(0); }
    @FXML private void onSubject2Clicked(MouseEvent ev) { openClassInfo(1); }
    @FXML private void onSubject3Clicked(MouseEvent ev) { openClassInfo(2); }
    @FXML private void onSubject4Clicked(MouseEvent ev) { openClassInfo(3); }

    private void openClassInfo(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/teach/HomePage-view.fxml")
            );
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
}
