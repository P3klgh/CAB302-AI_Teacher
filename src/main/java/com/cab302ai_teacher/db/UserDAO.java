package com.cab302ai_teacher.db;

import com.cab302ai_teacher.model.User;
import com.cab302ai_teacher.util.PasswordHasher;
import com.cab302ai_teacher.util.Validator;
import com.cab302ai_teacher.model.UserFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public static boolean isValidUser(String email, String password) {
        if (isInvalidCredentials(email, password)) {
            System.out.println("Invalid email or password format.");
            return false;
        }

        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, PasswordHasher.hashPassword(password));

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Login query failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean registerUser(String firstName, String lastName, String email, String password, String role) {
        if (isInvalidCredentials(email, password)) return false;

        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            if (emailExists(conn, email)) {
                System.err.println("Registration failed: Email already exists.");
                return false;
            }

            String insertQuery = "INSERT INTO users (firstName, lastName, email, password, role) VALUES (?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, firstName);
                stmt.setString(2, lastName);
                stmt.setString(3, email);
                stmt.setString(4, PasswordHasher.hashPassword(password));
                stmt.setString(5, role);

                stmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }

    public static User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return UserFactory.create(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        email,
                        rs.getString("password"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            System.err.println("User lookup failed: " + e.getMessage());
        }
        return null;
    }

    // === Private helpers ===

    private static boolean isInvalidCredentials(String email, String password) {
        return email == null || email.isEmpty() || !Validator.isValidEmail(email)
                || password == null || password.isEmpty() || !Validator.isValidPassword(password);
    }

    private static boolean emailExists(Connection conn, String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
}
