package com.example.teach.controller;

import com.example.teach.model.*;
import com.example.teach.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller for the "Homework" section of a subject.
 * <p>
 * Implements {@link SectionControllerBase} to allow dependency injection of
 * the current user, subject, and dashboard controller. Manages an Accordion
 * UI component to display homework-related content.
 */
public class HomeworkPageController implements SectionControllerBase {

    /** The authenticated user viewing this section. */
    private User currentUser;
    /** The subject context for this Homework page. */
    private Subject currentSubject;
    /** Parent DashboardController for navigation and UI control. */
    private DashboardController dashboardController;
    /** Accordion UI container for organizing homework panes. */
    @FXML private Accordion accordion;

    /**
     * Injects the authenticated user into this controller.
     *
     * @param user the logged-in {@link User}
     */
    @Override public void setUser(User user) {
        this.currentUser = user;
    }

    /**
     * Injects the current subject into this controller.
     * Updates each titled pane's text to include the subject context.
     *
     * @param subject the {@link Subject} being viewed
     */
    @Override public void setSubject(Subject subject) {
        this.currentSubject = subject;
        // Prefix each pane title with the subject name
        accordion.getPanes().forEach(pane ->
                pane.setText(currentSubject.getName() + " / " + pane.getText())
        );
    }

    /**
     * Injects the DashboardController for navigation actions.
     *
     * @param dash the parent {@link DashboardController}
     */
    @Override public void setDashboardController(DashboardController dash) {
        this.dashboardController = dash;
    }

    /**
     * Handler for the "Home" button in the Homework section.
     * Navigates back to the dashboard or subject home view.
     */
    @FXML private void onGoHome() {
        dashboardController.goToDashboard(null);
    }

    @FXML
    private Button teachermakehomework;

    @FXML
    public void initialize() {
        // 获取当前用户
        User user = SessionManager.getUser();

        // 只有教师才显示按钮
        if (user instanceof Teacher) {
            teachermakehomework.setVisible(true);
            teachermakehomework.setManaged(true);
        } else {
            teachermakehomework.setVisible(false);
            teachermakehomework.setManaged(false);
        }
    }




    @FXML
    public void handleAddHomework(javafx.event.ActionEvent actionEvent) {

        Button button = (Button) actionEvent.getSource();

        // 向上查找按钮所属的 TitledPane
        Parent parent = button.getParent();
        while (parent != null && !(parent instanceof TitledPane)) {
            parent = parent.getParent();
        }

        if (parent instanceof TitledPane titledPane) {
            String weekText = titledPane.getText(); // e.g., "Math / Week 1"
            String[] parts = weekText.split("Week ");
            if (parts.length < 2) {
                System.out.println("❌ can't identity week number");
                return;
            }
            int week = Integer.parseInt(parts[1].trim());

            // ===== 弹窗 1：输入标题 =====
            TextInputDialog titleDialog = new TextInputDialog();
            titleDialog.setTitle("Create Homework");
            titleDialog.setHeaderText("Enter Homework Title for Week " + week);
            Optional<String> titleResult = titleDialog.showAndWait();
            if (titleResult.isEmpty()) return;
            String title = titleResult.get();

            // ===== 弹窗 2：输入描述 =====
            TextInputDialog descDialog = new TextInputDialog();
            descDialog.setTitle("Create Homework");
            descDialog.setHeaderText("Enter Homework Description");
            Optional<String> descResult = descDialog.showAndWait();
            String description = descResult.orElse("No description");

            // ===== 弹窗 3：输入截止日期（due date）=====
            TextInputDialog dueDialog = new TextInputDialog(LocalDate.now().plusDays(7).toString());
            dueDialog.setTitle("Create Homework");
            dueDialog.setHeaderText("Enter Due Date (yyyy-MM-dd)");
            Optional<String> dueResult = dueDialog.showAndWait();
            String dueDate = dueResult.orElse(LocalDate.now().plusDays(7).toString());

            // ===== 弹窗 4：输入发布时间（release date）=====
            TextInputDialog releaseDialog = new TextInputDialog(LocalDate.now().toString());
            releaseDialog.setTitle("Create Homework");
            releaseDialog.setHeaderText("Enter Release Date (yyyy-MM-dd)");
            Optional<String> releaseResult = releaseDialog.showAndWait();
            String releaseDate = releaseResult.orElse(LocalDate.now().toString());

            // ===== 弹窗 5：输入开放时间（open date）=====
            TextInputDialog openDialog = new TextInputDialog(LocalDate.now().toString());
            openDialog.setTitle("Create Homework");
            openDialog.setHeaderText("Enter Open Date for Students (yyyy-MM-dd)");
            Optional<String> openResult = openDialog.showAndWait();
            String openDate = openResult.orElse(LocalDate.now().toString());

            // ===== 创建并保存作业对象 =====
            Homework hw = new Homework(
                    currentSubject.getId(),
                    String.valueOf(week),
                    title,
                    description,
                    dueDate,
                    releaseDate,
                    openDate
            );

            try {
                new HomeworkDAO().add(hw);
                System.out.println("✅ homework added to " + week + ": " + title);
            } catch (SQLException e) {
                System.err.println("❌ fale to add homework: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ can't identity the week（TitledPane）");
        }
    }
}
