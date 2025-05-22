package com.example.teach.session;

import com.example.teach.model.User;
import com.example.teach.model.UserDAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoginPersistenceManager {

    private static final Path SESSION_FILE = Path.of(
            "src", "main", "java", "com", "example", "teach", "session", "user", ".session"
    );
    public static void saveuser(User user){
        try{
            Files.createDirectories(SESSION_FILE.getParent());
            Files.writeString(SESSION_FILE,user.getId());
        }catch (IOException e){
            System.err.println("Failed to write session file: " + e.getMessage());
        }
    }

    public static User loaduser(){
        try{
            if(Files.exists(SESSION_FILE)){
                String userid = Files.readString(SESSION_FILE).trim();
                return new UserDAO().findByUserId(userid);

            }

        }catch (IOException e) {
            System.err.println("Failed to read session file: " + e.getMessage());
        }
        return null;
    }

    public static void clearUser() {
        try {
            Files.deleteIfExists(SESSION_FILE);
        } catch (IOException e) {
            System.err.println("Failed to delete session file: " + e.getMessage());
        }
    }

}
