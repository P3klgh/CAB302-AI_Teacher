package com.cab302ai_teacher.util;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL = Pattern.compile("[^a-zA-Z0-9]");

    // Validates the email format
    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && EMAIL_REGEX.matcher(email).matches();
    }

    // Validates the password (length and complexity)
    public static boolean isValidPassword(String password) {
        return password != null && !password.isEmpty() &&
                password.length() >= 6 &&
                UPPERCASE.matcher(password).find() &&
                LOWERCASE.matcher(password).find() &&
                DIGIT.matcher(password).find() &&
                SPECIAL.matcher(password).find();
    }
}