package com.cab302ai_teacher.tester;

import com.cab302ai_teacher.db.DatabaseManager;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @Test
    public void testConnectionIsNotNull() {
        try (Connection conn = DatabaseManager.connect()) {
            assertNotNull(conn, "Database connection should not be null");
        } catch (Exception e) {
            fail("Exception during DB connection: " + e.getMessage());
        }
    }
}