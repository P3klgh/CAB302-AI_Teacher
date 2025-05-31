package com.cab302ai_teacher.model;

import java.util.Objects;

/**
 * Represents a user in the system with authentication and role information.
 */
public class User {

    /** User's first name */
    private final String firstName;

    /** User's last name */
    private final String lastName;

    /** User's email address */
    private final String email;

    /** User's hashed password */
    private final String password;

    /** User's role (e.g., teacher or student) */
    private final String role;

    /**
     * Constructs a User object after validating all input fields.
     *
     * @param firstName user's first name
     * @param lastName  user's last name
     * @param email     user's email address
     * @param password  user's password (hashed or raw depending on usage)
     * @param role      user's role
     * @throws IllegalArgumentException if any field is invalid
     */
    public User(String firstName, String lastName, String email, String password, String role) {
        this.firstName = validateName(firstName, "First name");
        this.lastName = validateName(lastName, "Last name");
        this.email = validateEmail(email);
        this.password = validatePassword(password);
        this.role = validateRole(role);
    }

    // === Validation methods ===

    /**
     * Validates that the name is not null or empty.
     *
     * @param value     the name to validate
     * @param fieldName the field label for error message
     * @return trimmed valid name
     * @throws IllegalArgumentException if name is invalid
     */
    private String validateName(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
        return value.trim();
    }

    /**
     * Validates email format using a basic pattern.
     *
     * @param email the email to validate
     * @return the validated email
     * @throws IllegalArgumentException if email is invalid
     */
    private String validateEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        return email;
    }

    /**
     * Validates the password to meet a minimum length requirement.
     *
     * @param password the password to validate
     * @return the validated password
     * @throws IllegalArgumentException if password is invalid
     */
    private String validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        return password;
    }

    /**
     * Validates the user's role field.
     *
     * @param role the role to validate
     * @return validated role in lowercase
     * @throws IllegalArgumentException if role is empty
     */
    private String validateRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
        return role.trim().toLowerCase();
    }

    // === Getters ===

    /**
     * @return the user's first name
     */
    public String getFirstName() { return firstName; }

    /**
     * @return the user's last name
     */
    public String getLastName() { return lastName; }

    /**
     * @return the user's email
     */
    public String getEmail() { return email; }

    /**
     * @return the user's password
     */
    public String getPassword() { return password; }

    /**
     * @return the user's role
     */
    public String getRole() { return role; }
}
