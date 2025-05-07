package com.cab302ai_teacher.db;

import com.cab302ai_teacher.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static boolean isValidUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String hashedPassword = PasswordHasher.hashPassword(password);
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
     * Registers a new user with the given email, hashed password, and role.
     *
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param email The user's email address
     * @param password The hashed password to store
     * @param role The role of the user (e.g., "student", "teacher", "admin")
     * @return true if registration is successful; false otherwise
     */
    public static boolean registerUser(String firstName, String lastName, String email, String password, String role) {
        //do a check for existing user with select statement using email
        String sql = "INSERT INTO users (firstName,lastName, email, password, role) VALUES (?, ?, ?, ?, ?)";
        String hashedPassword = PasswordHasher.hashPassword(password);
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1,firstName);
            stmt.setString(2,lastName);
            stmt.setString(3,email);
            stmt.setString(4,hashedPassword);
            stmt.setString(5,role);

            stmt.executeUpdate();

           return true;

        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email The user's email address
     * @return A User object if found; null otherwise
     */
    public static String getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String password = rs.getString("password");
                String role = rs.getString("role");
                return (email + password + role);
            }

        } catch (SQLException e) {
            System.err.println("User lookup failed: " + e.getMessage());
        }
        return null;
    }
}
