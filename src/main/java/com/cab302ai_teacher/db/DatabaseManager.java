package com.cab302ai_teacher.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:ai_teacher.db";

    // Singleton instance
    private static DatabaseManager instance;

    // Connection object (can be shared if needed)
    private Connection connection;

    // Private constructor: prevents instantiation from outside
    private DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("[DB] ❌ Connection failed: " + e.getMessage());
        }
    }

    // Static method to return the singleton instance
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Provides a new connection from the instance
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("[DB] ❌ Failed to open new connection: " + e.getMessage());
            return null;
        }
    }
    public void initializeDatabase() {
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

        try (Statement stmt = connection.createStatement()) {
            for (String sql : tableStatements) {
                stmt.execute(sql);
            }
            System.out.println("[DB] ✅ tables initialized.");
        } catch (SQLException e) {
            System.err.println("[DB] ❌ Failed to initialize database: " + e.getMessage());
        }
    }
}
