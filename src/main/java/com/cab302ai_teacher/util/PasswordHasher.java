package com.cab302ai_teacher.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for securely hashing passwords using SHA-256 algorithm.
 */
public class PasswordHasher {

    /**
     * Hashes a given plain text password using SHA-256 and returns the result as a hexadecimal string.
     *
     * @param password The plain text password to hash
     * @return The SHA-256 hashed password in hexadecimal format
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    public static String hashPassword(String password) {
        try {
            // Create  SHA-256 digest
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input password to a byte array and compute the hash
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hash = new StringBuilder();
            for (byte b : hashBytes) {
                hash.append(String.format("%02x", b));
            }

            return hash.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
