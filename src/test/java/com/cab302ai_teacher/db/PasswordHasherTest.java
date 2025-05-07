package com.cab302ai_teacher.db;

import com.cab302ai_teacher.util.PasswordHasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {

    @Test
    public void testHashPasswordNull() {
        // Arrange
        String password = "TestPassword123!";

        // Act
        String hashedPassword = PasswordHasher.hashPassword(password);

        // Assert
        assertNotNull(hashedPassword, "Hashed password should not be null.");
        assertEquals(64, hashedPassword.length(), "Hashed password should be 64 characters long (SHA-256 hash).");
    }

    @Test
    public void testHashPasswordLongPassword() {
        // Arrange
        String password = "A".repeat(1000); // 1000 characters long

        // Act
        String hashedPassword = PasswordHasher.hashPassword(password);

        // Assert
        assertNotNull(hashedPassword, "Hashed password should not be null.");
        assertEquals(64, hashedPassword.length(), "Hashed password should always be 64 characters long (SHA-256 hash).");
    }

    @Test
    public void testHashPasswordConsistency() {
        // Arrange
        String password = "TestPassword123!";

        // Act
        String hashedPassword1 = PasswordHasher.hashPassword(password);
        String hashedPassword2 = PasswordHasher.hashPassword(password);

        // Assert: Hashing the same password should return the same result
        assertEquals(hashedPassword1, hashedPassword2, "Hashing the same password should return the same result.");
    }

    @Test
    public void testHashPasswordUniqueness() {
        // Arrange
        String password1 = "TestPassword123!";
        String password2 = "AnotherPassword456!";

        // Act
        String hashedPassword1 = PasswordHasher.hashPassword(password1);
        String hashedPassword2 = PasswordHasher.hashPassword(password2);

        // Assert: Hashing different passwords should return different results
        assertNotEquals(hashedPassword1, hashedPassword2, "Hashing different passwords should return different results.");
    }

    @Test
    public void testHashPasswordCaseSensitivity() {
        // Arrange
        String password1 = "Password123!";
        String password2 = "password123!";

        // Act
        String hashedPassword1 = PasswordHasher.hashPassword(password1);
        String hashedPassword2 = PasswordHasher.hashPassword(password2);

        // Assert: Hashing case-sensitive passwords should return different results
        assertNotEquals(hashedPassword1, hashedPassword2, "Hashes for case-sensitive passwords should be different.");
    }

}
