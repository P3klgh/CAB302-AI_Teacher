package com.cab302ai_teacher.util;

import java.util.regex.Pattern;

/**
 * Utility class for validating user input such as email and password.
 */
public class Validator {

    // Regular expression pattern for validating email addresses
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    // Patterns used to validate password complexity
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[^a-zA-Z0-9]");

    /**
     * Validates whether the given email is in a proper format.
     *
     * @param email The email string to validate
     * @return true if the email is valid; false otherwise
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && EMAIL_REGEX.matcher(email).matches();
    }

    /**
     * Validates whether the given password meets complexity requirements:
     * at least 6 characters, and includes uppercase, lowercase, digit, and special character.
     *
     * @param password The password string to validate
     * @return true if the password meets all criteria; false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && !password.isEmpty() &&
                password.length() >= 6 &&
                UPPERCASE.matcher(password).find() &&
                LOWERCASE.matcher(password).find() &&
                DIGIT.matcher(password).find() &&
                SPECIAL.matcher(password).find();
    }
}
