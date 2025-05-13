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
        String[] tableStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                firstName TEXT NOT NULL,
                lastName TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                role TEXT NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS quizzes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                quiz_id INTEGER NOT NULL,
                question_text TEXT NOT NULL,
                FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS options (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question_id INTEGER NOT NULL,
                option_text TEXT NOT NULL,
                is_correct BOOLEAN NOT NULL,
                FOREIGN KEY (question_id) REFERENCES questions(id)
            );
            """
        };


        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            for (String sql : tableStatements) {
                stmt.execute(sql);
            }
            System.out.println("[DB] ✅ tables initialized.");
        } catch (SQLException e) {
            System.err.println("[DB] ❌ Failed to initialize database: " + e.getMessage());
        }
    }
}
