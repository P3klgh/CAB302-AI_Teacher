package com.cab302ai_teacher.db;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private final String testFirstName = "First";
    private final String testLastName = "Last";
    private final String testEmail = "testuser@example.com";
    private final String testPassword = "Test123!";
    private final String testOccupation = "Student";

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
        boolean result = UserDAO.registerUser(testFirstName, testLastName, testEmail, testPassword, testOccupation);
        assertTrue(result, "User should be registered successfully");
    }

    @Test
    public void testIsValidUserWithCorrectCredentials() {
        UserDAO.registerUser(testFirstName, testLastName, testEmail, testPassword, testOccupation);
        boolean valid = UserDAO.isValidUser(testEmail, testPassword);
        assertTrue(valid, "Valid user should be authenticated successfully");
    }

    @Test
    public void testIsValidUserWithIncorrectPassword() {
        UserDAO.registerUser(testFirstName, testLastName, testEmail, testPassword, testOccupation);
        boolean valid = UserDAO.isValidUser(testEmail, "WrongPass123");
        assertFalse(valid, "Invalid password should not authenticate");
    }

    @Test
    public void testRegisterDuplicateUserFails() {
        UserDAO.registerUser(testFirstName, testLastName, testEmail, testPassword, testOccupation);
        boolean secondAttempt = UserDAO.registerUser(testFirstName, testLastName, testEmail, testPassword, testOccupation);
        assertFalse(secondAttempt, "Duplicate registration should fail");
    }

    @Test
    public void testRegisterUserWithEmptyEmail() {
        boolean result = UserDAO.registerUser(testFirstName, testLastName, "", testPassword, testOccupation);
        assertFalse(result, "User with an empty email should not be registered.");
    }

    @Test
    public void testIsValidUserWithEmptyEmail() {
        boolean valid = UserDAO.isValidUser("", testPassword);
        assertFalse(valid, "Empty email should not authenticate.");
    }

    @Test
    public void testRegisterUserWithEmptyPassword() {
        boolean result = UserDAO.registerUser(testFirstName, testLastName, testEmail, "", testOccupation);
        assertFalse(result, "User with an empty password should not be registered.");
    }

    @Test
    public void testIsValidUserWithEmptyPassword() {
        UserDAO.registerUser(testFirstName, testLastName, testEmail, testPassword, testOccupation);
        boolean valid = UserDAO.isValidUser(testEmail, "");
        assertFalse(valid, "Empty password should not authenticate.");
    }

    @Test
    public void testRegisterUserWithInvalidEmail() {
        boolean result = UserDAO.registerUser(testFirstName, testLastName, "invalid-email", testPassword, testOccupation);
        assertFalse(result, "User with an invalid email format should not be registered.");
    }

    @Test
    public void testIsValidUserWithInvalidEmail() {
        boolean valid = UserDAO.isValidUser("invalid-email", testPassword);
        assertFalse(valid, "Invalid email format should not authenticate.");
    }

    @Test
    public void testRegisterUserWithShortPassword() {
        boolean result = UserDAO.registerUser(testFirstName, testLastName, testEmail, "123", testOccupation);
        assertFalse(result, "User with a password shorter than 6 characters should not be registered.");
    }

    @Test
    public void testRegisterUserWithRole() {
        boolean result = UserDAO.registerUser(testFirstName, testLastName, testEmail, testPassword, "Admin");
        assertTrue(result, "User with a role should be registered successfully.");

        String query = "SELECT role FROM users WHERE email = ?";
        String roleFromDb = null;

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, testEmail);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                roleFromDb = rs.getString("role");
            }
        } catch (SQLException e) {
            fail("Database query failed: " + e.getMessage());
        }

        assertNotNull(roleFromDb, "Role should be retrieved from the database.");
        assertEquals("Admin", roleFromDb, "User role should be correctly stored as Admin.");
    }
}