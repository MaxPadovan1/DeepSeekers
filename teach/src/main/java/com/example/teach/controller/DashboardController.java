package com.example.teach.controller;

import com.example.teach.model.Student;
import com.example.teach.model.Subject;
import com.example.teach.model.Teacher;
import com.example.teach.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    private User currentUser;
    private List<Subject> subjects;

    @FXML private BorderPane rootBorderPane;
    @FXML private MenuButton userMenu;
    @FXML private VBox drawer;
    @FXML private VBox subject1Tile;
    @FXML private VBox subject2Tile;
    @FXML private VBox subject3Tile;
    @FXML private VBox subject4Tile;

    /** Called immediately _after_ FXMLLoader.load() */
    public void setUser(User user) {
        this.currentUser = user;
        userMenu.setText(user.getFirstName() + " " + user.getLastName());

        // Build subjects list for Student or Teacher
        if (user instanceof Student s) {
            subjects = s.getSubjects();
        } else if (user instanceof Teacher t) {
            subjects = List.of(t.getSubject());
        } else {
            subjects = List.of();
        }

        // Populate the tiles
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

    @FXML
    private void toggleDrawer() {
        boolean open = drawer.isVisible();
        drawer.setVisible(!open);
        drawer.setManaged(!open);
    }

    @FXML
    private void goBack() {
        System.out.println("Back button clicked!");
    }

    @FXML
    private void showNotifications() {
        System.out.println("Notification button clicked!");
    }

    @FXML
    private void onSubject1Clicked(MouseEvent ev) {
        //openClassInfo(subjects.get(0));
    }

    @FXML
    private void onSubject2Clicked(MouseEvent ev) {
        //openClassInfo(subjects.get(1));
    }

    @FXML
    private void onSubject3Clicked(MouseEvent ev) {
        //openClassInfo(subjects.get(2));
    }

    @FXML
    private void onSubject4Clicked(MouseEvent ev) {
        //openClassInfo(subjects.get(3));
    }

    /**
     * Loads the Class Info view into the center of the dashboard,
     * injecting both the current user and the selected subject.
     */
//    private void openClassInfo(Subject subj) {
//        try {
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("/com/example/teach/ClassInfo-view.fxml")
//            );
//            Parent view = loader.load();
//            ClassInfoController ctrl = loader.getController();
//            ctrl.setUser(currentUser);
//            ctrl.setSubject(subj);
//            rootBorderPane.setCenter(view);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
