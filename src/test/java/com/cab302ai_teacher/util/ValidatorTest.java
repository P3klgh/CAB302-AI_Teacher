package com.cab302ai_teacher.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    public void testValidEmail() {
        assertTrue(Validator.isValidEmail("user@example.com"));
    }

    @Test
    public void testInvalidEmailRegexFlaw1() {
        assertFalse(Validator.isValidEmail("userexample.com"));
    }

    @Test
    public void testInvalidEmailRegexFlaw2() {
        assertFalse(Validator.isValidEmail("user@.com"));

    }
    @Test
    public void testInvalidEmailRegexFlaw3() {
        assertFalse(Validator.isValidEmail(""));
    }

    @Test
    public void testValidPasswordIsSTRONG() {
        assertTrue(Validator.isValidPassword("Strong1@"));
    }

    @Test
    public void testInvalidPasswordTooShort() {
        assertFalse(Validator.isValidPassword("weak"));              // Too short
    }

    @Test
    public void testInvalidPasswordNoUpperCase() {
        assertFalse(Validator.isValidPassword("lowercase1!"));       // No uppercase
    }

    @Test
    public void testInvalidPasswordNoLowerCase() {
        assertFalse(Validator.isValidPassword("UPPERCASE1!"));       // No lowercase
    }

    @Test
    public void testInvalidPasswordNoDigit() {
        assertFalse(Validator.isValidPassword("NoNumber!"));         // No digit
    }
    @Test
    public void testInvalidPasswordNoSpecialChar() {
        assertFalse(Validator.isValidPassword("NoSpecial123"));      // No special char
    }
    @Test
    public void testInvalidPasswordNotNull() {
        assertFalse(Validator.isValidPassword(""));                  // Not Null
    }
}
