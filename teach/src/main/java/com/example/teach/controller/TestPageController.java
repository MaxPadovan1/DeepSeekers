package com.example.teach.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestPageController {
    public void onRemoveAssignment(ActionEvent actionEvent) {
    }

    public void onAddAssignment(ActionEvent actionEvent) {
    }

    public void onEditAssignment(ActionEvent actionEvent) {
    }

    public void onSaveEditedAssignment(ActionEvent actionEvent) {
    }

    public void onReleaseAssignment(ActionEvent actionEvent) {
    }

    public void onAssignmentSelected(ActionEvent actionEvent) {
    }
    @FXML
    private void handleGoToTestPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/teach/view/TestPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }



}
