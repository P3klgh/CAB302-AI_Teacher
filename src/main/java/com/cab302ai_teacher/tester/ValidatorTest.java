package com.cab302ai_teacher.tester;

import com.cab302ai_teacher.util.Validator;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    public void testValidEmail() {
        assertTrue(Validator.isValidEmail("user@example.com"));
    }

    @Test
    public void testInvalidEmail() {
        assertFalse(Validator.isValidEmail("userexample.com"));
        assertFalse(Validator.isValidEmail("user@.com"));
        assertFalse(Validator.isValidEmail(""));
    }

    @Test
    public void testValidPassword() {
        assertTrue(Validator.isValidPassword("Strong1@"));
    }

    @Test
    public void testInvalidPassword() {
        assertFalse(Validator.isValidPassword("weak"));              // Too short
        assertFalse(Validator.isValidPassword("lowercase1!"));       // No uppercase
        assertFalse(Validator.isValidPassword("UPPERCASE1!"));       // No lowercase
        assertFalse(Validator.isValidPassword("NoNumber!"));         // No digit
        assertFalse(Validator.isValidPassword("NoSpecial123"));      // No special char
    }
}
