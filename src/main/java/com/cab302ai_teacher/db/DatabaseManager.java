package com.cab302ai_teacher.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the connection to the SQLite database and initializes tables.
 */
public class DatabaseManager {

    // JDBC URL for the SQLite database file
    private static final String DB_URL = "jdbc:sqlite:ai_teacher.db";

    /**
     * Establishes and returns a connection to the SQLite database.
     *
     * @return SQLite database connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Initializes the database by creating required tables if they do not exist.
     * In this case, it creates a "users" table with fields for id, email, and password.
     */
    public static void initializeDatabase() {
        // SQL statement to create the users table if it doesn't already exist
        String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );
        """;

        // Try to establish a connection and execute the SQL statement
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTable);
            System.out.println("[DB] ✅ users table initialized.");
        } catch (SQLException e) {
            // Print an error message if initialization fails
            System.err.println("[DB] ❌ Failed to initialize database: " + e.getMessage());
        }
    }
}
