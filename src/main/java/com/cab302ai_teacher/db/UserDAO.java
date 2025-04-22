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
     * @param hashedPassword The user's hashed password
     * @return true if a matching user is found; false otherwise
     */
    public static boolean isValidUser(String email, String hashedPassword) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Login query failed: " + e.getMessage());
            return false;
        }
    }


    /**
     * Registers a new user with the given email and hashed password.
     *
     * @param email The user's email address
     * @param hashedPassword The hashed password to store
     * @return true if registration is successful; false if user already exists or an error occurs
     */
    public static boolean registerUser(String email, String hashedPassword) {
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    // TODO: Add more user-related features such as findByEmail(), updatePassword(), etc.
}
