package com.cab302ai_teacher.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for interacting with the "users" table in the database.
 * This class contains methods related to user authentication and management.
 */
public class UserDAO {

    /**
     * Checks if a user with the given email and password exists in the database.
     *
     * @param email The user's email address
     * @param password The user's password
     * @return true if a matching user is found; false otherwise
     */
    public static boolean isValidUser(String email, String password) {
        // SQL query with placeholders to prevent SQL injection
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        // Try-with-resources ensures the connection and statement are closed automatically
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Bind the email and password to the placeholders
            stmt.setString(1, email);
            stmt.setString(2, password);

            // Execute the query and return true if a matching user is found
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            // Log error message if query fails
            System.err.println("Login query failed: " + e.getMessage());
            return false;
        }
    }

    // TODO: Add more user-related features here such as registration, user lookup, etc.
}
