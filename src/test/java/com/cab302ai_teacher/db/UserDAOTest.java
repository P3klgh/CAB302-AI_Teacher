package com.cab302ai_teacher.db;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private final String testFirstName = "First";
    private final String testLastName = "Last";
    private final String testOccupation = "Student";
    private final String testEmail = "testuser@example.com";
    private final String testPassword = "Test123!";

    @BeforeEach
    public void setup() {
    
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE email = ?")) {
            stmt.setString(1, testEmail);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("[Test Setup] Failed to clear test user: " + e.getMessage());
        }
    }

    @Test
    public void testRegisterUserSuccess() {
        boolean result = UserDAO.registerUser(testFirstName, testLastName, testOccupation, testEmail, testPassword);
        assertTrue(result, "User should be registered successfully");
    }

    @Test
    public void testIsValidUserWithCorrectCredentials() {
        UserDAO.registerUser(testFirstName, testLastName, testOccupation, testEmail, testPassword);

        boolean valid = UserDAO.isValidUser(testEmail, testPassword);
        assertTrue(valid, "Valid user should be authenticated successfully");
    }

    @Test
    public void testIsValidUserWithIncorrectPassword() {
        UserDAO.registerUser(testFirstName, testLastName, testOccupation, testEmail, testPassword);

        boolean valid = UserDAO.isValidUser(testEmail, "WrongPass123");
        assertFalse(valid, "Invalid password should not authenticate");
    }

    @Test
    public void testRegisterDuplicateUserFails() {
        UserDAO.registerUser(testFirstName, testLastName, testOccupation, testEmail, testPassword);
        boolean secondAttempt = UserDAO.registerUser(testFirstName, testLastName, testOccupation, testEmail, testPassword);
        assertFalse(secondAttempt, "Duplicate registration should fail");
    }


}
