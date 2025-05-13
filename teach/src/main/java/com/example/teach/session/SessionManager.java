package com.example.teach.session;

import com.example.teach.model.User;

/**
 * Manages the current user session across the application.
 * <p>
 * This singleton-style utility stores the currently logged-in {@link User} object
 * and provides methods to access or reset the session from any controller or service.
 */
public final class SessionManager {

    /** Private constructor to prevent instantiation. */
    private SessionManager() {}

    /** Static reference to the current user session. */
    private static User currentUser;

    /**
     * Stores the authenticated user in the current session.
     *
     * @param user the logged-in {@link User} instance (Student or Teacher)
     */
    public static void setUser(User user) {
        currentUser = user;
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the {@link User} object, or null if no session is active
     */
    public static User getUser() {
        return currentUser;
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return true if session is active; false otherwise
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Clears the current session (typically during logout).
     */
    public static void clearSession() {
        currentUser = null;
    }
}