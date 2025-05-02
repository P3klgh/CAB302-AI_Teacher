package com.cab302ai_teacher.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    // Email validation

    @Test
    public void testValidEmail() {
        assertTrue(Validator.isValidEmail("user@example.com"));
    }

    @Test
    public void testEmailMissingAtSymbol() {
        assertFalse(Validator.isValidEmail("userexample.com"));
    }

    @Test
    public void testEmailMissingDomainName() {
        assertFalse(Validator.isValidEmail("user@.com"));
    }

    @Test
    public void testEmailEmpty() {
        assertFalse(Validator.isValidEmail(""));
    }

    // Password validation

    @Test
    public void testValidPassword() {
        assertTrue(Validator.isValidPassword("Strong1@"));
    }

    @Test
    public void testPasswordTooShort() {
        assertFalse(Validator.isValidPassword("weak"));
    }

    @Test
    public void testPasswordMissingUppercase() {
        assertFalse(Validator.isValidPassword("lowercase1!"));
    }

    @Test
    public void testPasswordMissingLowercase() {
        assertFalse(Validator.isValidPassword("UPPERCASE1!"));
    }

    @Test
    public void testPasswordMissingDigit() {
        assertFalse(Validator.isValidPassword("NoNumber!"));
    }

    @Test
    public void testPasswordMissingSpecialCharacter() {
        assertFalse(Validator.isValidPassword("NoSpecial123"));
    }
}
