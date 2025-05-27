package com.cab302ai_teacher.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @Test
    public void testConnectionIsNotNull() {
        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            assertNotNull(conn, "Database connection should not be null");
        } catch (Exception e) {
            fail("Exception during DB connection: " + e.getMessage());
        }
    }

    @Test
    public void testDatabaseInitialization() {
        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            // After initialization, check if the "users" table exists
            String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='users';";
            try (var stmt = conn.createStatement();
                 var rs = stmt.executeQuery(query)) {

                assertTrue(rs.next(), "The 'users' table should exist after initialization.");
            }
        } catch (Exception e) {
            fail("Exception during DB initialization: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleConnections() {
        try (Connection conn1 = DatabaseManager.getInstance().getConnection();
             Connection conn2 = DatabaseManager.getInstance().getConnection()) {

            assertNotNull(conn1, "First connection should not be null.");
            assertNotNull(conn2, "Second connection should not be null.");
        } catch (Exception e) {
            fail("Exception during multiple database connections: " + e.getMessage());
        }
    }
}