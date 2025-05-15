package com.example.teach.session;

import com.example.teach.model.User;
import com.example.teach.model.UserDAO;
import javafx.scene.control.Labeled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility to display username from persisted session file into a JavaFX UI component.
 */
public class SessionTool {

    public static String getUsernameFromSessionFile() {
        Path sessionPath = Path.of(
                "src", "main", "java", "com", "example", "teach", "session", "user", ".session"
        );

        try {
            if (Files.exists(sessionPath)) {
                String userId = Files.readString(sessionPath).trim();
                User user = new UserDAO().findByUserId(userId);
                if (user != null) {
                    return user.getFirstName() + " " + user.getLastName();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read session file: " + e.getMessage());
        }

        return "Guest";  // 默认显示
    }
}