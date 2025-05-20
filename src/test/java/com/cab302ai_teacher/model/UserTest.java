package com.cab302ai_teacher.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void validUserCreationSucceeds() {
        assertDoesNotThrow(() -> new User("John", "Doe", "john@example.com", "SecurePass123", "student"));
    }

    @Test
    public void userCreationFailsWithEmptyFirstName() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new User("", "Doe", "john@example.com", "SecurePass123", "student")
        );
        assertTrue(ex.getMessage().contains("First name"));
    }

    @Test
    public void userCreationFailsWithInvalidEmail() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new User("John", "Doe", "invalid-email", "SecurePass123", "student")
        );
        assertTrue(ex.getMessage().contains("Invalid email"));
    }

    @Test
    public void userCreationFailsWithShortPassword() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new User("John", "Doe", "john@example.com", "123", "student")
        );
        assertTrue(ex.getMessage().contains("Password must"));
    }

    @Test
    public void userCreationFailsWithEmptyRole() {
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                new User("John", "Doe", "john@example.com", "SecurePass123", "")
        );
        assertTrue(ex.getMessage().contains("Role"));
    }
}
