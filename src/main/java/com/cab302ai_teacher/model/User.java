package com.cab302ai_teacher.model;

import java.util.Objects;

/**
 * Represents a user in the system with basic authentication details.
 */
public class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String role;

    /**
     * Constructs a User with validation on all fields.
     *
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

    private String validateName(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty.");
        }
        return value.trim();
    }

    private String validateEmail(String email) {
        // Basic pattern for email format
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        return email;
    }

    private String validatePassword(String password) {
        // Enforce a minimum password length for security
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        return password;
    }

    private String validateRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
        return role.trim().toLowerCase();
    }

    // === Getters ===

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
