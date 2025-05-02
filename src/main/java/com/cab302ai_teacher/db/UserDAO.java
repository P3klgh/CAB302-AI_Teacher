package com.cab302ai_teacher.db;

import com.cab302ai_teacher.util.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Checks if a user exists with the given email and password.
     * Password is hashed before checking.
     */
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
     * Registers a new user with the given email, password, and role.
     * Password is hashed before being stored.
     *
     * @param email The user's email address
     * @param rawPassword The raw password to be hashed
     * @param role The user's role (student, teacher, etc.)
     * @return true if registration succeeded
     */
    public static boolean registerUser(String email, String rawPassword, String role) {
        String hashedPassword = PasswordHasher.hashPassword(rawPassword);  // 🔐 해시 처리

        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, role);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Looks up a user by email and returns email+password+role for debugging.
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
