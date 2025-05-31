package com.cab302ai_teacher.model;

/**
 * A simple factory class to create User instances.
 */
public class UserFactory {

    /**
     * Creates a new User object using the provided details.
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email
     * @param password  the user's password (should be hashed later if not already)
     * @param role      the user's role (e.g., "student" or "teacher")
     * @return a new User instance
     */
    public static User create(String firstName, String lastName, String email, String password, String role) {
        return new User(firstName, lastName, email, password, role);
    }

}
