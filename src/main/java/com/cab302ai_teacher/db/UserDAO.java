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

            String hashedPassword = PasswordHasher.hashPassword(password);  // ✅ 해싱
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);  // ✅ 해시된 비번 저장

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Login query failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean registerUser(String firstName, String lastName, String occupation, String email, String password) {
        String sql = "INSERT INTO users (firstName, lastName, occupation, email, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = PasswordHasher.hashPassword(password);  // ✅ 해싱
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, occupation);
            stmt.setString(4, email);
            stmt.setString(5, hashedPassword);  // ✅ 해시된 비번 저장


            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Registration failed: " + e.getMessage());
            return false;
        }
    }
}
