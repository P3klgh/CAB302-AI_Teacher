package com.cab302ai_teacher.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class responsible for managing database connections and initialization.
 */
public class DatabaseManager {

    /**
     * JDBC URL for SQLite database.
     */
    private static final String DB_URL = "jdbc:sqlite:ai_teacher.db";

    /**
     * Singleton instance of the DatabaseManager.
     */
    private static DatabaseManager instance;

    /**
     * Connection object used for initialization.
     */
    private Connection connection;

    /**
     * Private constructor to enforce singleton pattern. Establishes initial DB connection.
     */
    private DatabaseManager() {
        try {
            this.connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("[DB] ❌ Connection failed: " + e.getMessage());
        }
    }

    /**
     * Provides access to the singleton instance of DatabaseManager.
     * @return the singleton DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Returns a new connection to the SQLite database.
     * @return a new Connection object or null if connection fails
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.err.println("[DB] ❌ Failed to open new connection: " + e.getMessage());
            return null;
        }
    }

    /**
     * Initializes the database schema by creating required tables if they do not exist.
     */
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
