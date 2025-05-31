package com.cab302ai_teacher.db;

import com.cab302ai_teacher.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing quizzes, questions, and options in the database.
 */
public class QuizDAO {

    /**
     * Retrieves all quizzes from the database, including their questions and options.
     *
     * @return list of all quizzes
     */
    public static List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String quizQuery = "SELECT * FROM quizzes";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement quizStmt = conn.prepareStatement(quizQuery);
             ResultSet quizRs = quizStmt.executeQuery()) {

            while (quizRs.next()) {
                int quizId = quizRs.getInt("id");
                String quizName = quizRs.getString("name");

                List<Question> questions = getQuestionsByQuizId(conn, quizId);
                Quiz quiz = new Quiz(quizName, questions, quizId);

                quizzes.add(quiz);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    /**
     * Retrieves all questions and their options for a specific quiz.
     *
     * @param conn   existing database connection
     * @param quizId the ID of the quiz
     * @return list of questions
     * @throws SQLException if a database access error occurs
     */
    private static List<Question> getQuestionsByQuizId(Connection conn, int quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();
        String questionQuery = "SELECT * FROM questions WHERE quiz_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
            stmt.setInt(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int questionId = rs.getInt("id");
                    String questionText = rs.getString("question_text");

                    List<String> options = new ArrayList<>();
                    List<Integer> correctIndexes = new ArrayList<>();

                    String optionQuery = "SELECT * FROM options WHERE question_id = ?";
                    try (PreparedStatement optionStmt = conn.prepareStatement(optionQuery)) {
                        optionStmt.setInt(1, questionId);
                        try (ResultSet optionRs = optionStmt.executeQuery()) {
                            int index = 0;
                            while (optionRs.next()) {
                                String optionText = optionRs.getString("option_text");
                                boolean isCorrect = optionRs.getBoolean("is_correct");
                                options.add(optionText);
                                if (isCorrect) {
                                    correctIndexes.add(index);
                                }
                                index++;
                            }
                        }
                    }

                    Question question = new Question(questionText, options, correctIndexes, questionId);
                    questions.add(question);
                }
            }
        }
        return questions;
    }

    /**
     * Retrieves the option IDs for a given question.
     *
     * @param conn       existing database connection
     * @param questionId the ID of the question
     * @return list of option IDs
     * @throws SQLException if a database access error occurs
     */
    private static List<Integer> getOptionIdsForQuestion(Connection conn, int questionId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM options WHERE question_id = ? ORDER BY id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id"));
                }
            }
        }
        return ids;
    }

    /**
     * Updates a quiz and its associated questions and options in the database.
     * Handles both inserting new and updating existing entries.
     *
     * @param quiz             the quiz to update
     * @param deletedQuestions list of questions that were removed and should be deleted
     * @throws SQLException if a database access error occurs
     */
    public static void updateQuiz(Quiz quiz, List<Question> deletedQuestions) throws SQLException {
        Connection conn = null;
        PreparedStatement quizStmt = null;
        PreparedStatement questionStmt = null;
        PreparedStatement insertQuestionStmt = null;
        PreparedStatement insertOptionStmt = null;
        PreparedStatement optionStmt = null;

        try {
            conn = DatabaseManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            String updateQuizSQL = "UPDATE quizzes SET name = ? WHERE id = ?";
            quizStmt = conn.prepareStatement(updateQuizSQL);
            quizStmt.setString(1, quiz.getName());
            quizStmt.setInt(2, quiz.getId());
            quizStmt.executeUpdate();

            String insertQuestionSQL = "INSERT INTO questions (quiz_id, question_text) VALUES (?, ?)";
            insertQuestionStmt = conn.prepareStatement(insertQuestionSQL, Statement.RETURN_GENERATED_KEYS);

            String insertOptionSQL = "INSERT INTO options (question_id, option_text, is_correct) VALUES (?, ?, ?)";
            insertOptionStmt = conn.prepareStatement(insertOptionSQL);

            String updateQuestionSQL = "UPDATE questions SET question_text = ? WHERE id = ?";
            questionStmt = conn.prepareStatement(updateQuestionSQL);

            String updateOptionSQL = "UPDATE options SET option_text = ?, is_correct = ? WHERE id = ?";
            optionStmt = conn.prepareStatement(updateOptionSQL);

            for (Question question : quiz.getQuestions()) {
                List<String> options = question.getOptions();
                List<Integer> correctIndexes = question.getCorrectIndexes();

                if (question.getId() == -1) {
                    insertQuestionStmt.setInt(1, quiz.getId());
                    insertQuestionStmt.setString(2, question.getQuestion());
                    insertQuestionStmt.executeUpdate();

                    try (ResultSet generatedKeys = insertQuestionStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            question.setId(generatedKeys.getInt(1));
                        } else {
                            throw new SQLException("Inserting question failed, no ID obtained.");
                        }
                    }

                    for (int i = 0; i < options.size(); i++) {
                        insertOptionStmt.setInt(1, question.getId());
                        insertOptionStmt.setString(2, options.get(i));
                        insertOptionStmt.setBoolean(3, correctIndexes.contains(i));
                        insertOptionStmt.executeUpdate();
                    }

                } else {
                    questionStmt.setString(1, question.getQuestion());
                    questionStmt.setInt(2, question.getId());
                    questionStmt.executeUpdate();

                    List<Integer> optionIds = getOptionIdsForQuestion(conn, question.getId());
                    for (int i = 0; i < options.size() && i < optionIds.size(); i++) {
                        int optionId = optionIds.get(i);
                        optionStmt.setString(1, options.get(i));
                        optionStmt.setBoolean(2, correctIndexes.contains(i));
                        optionStmt.setInt(3, optionId);
                        optionStmt.executeUpdate();
                    }
                }
            }

            if (deletedQuestions != null && !deletedQuestions.isEmpty()) {
                String deleteOptionsSQL = "DELETE FROM options WHERE question_id = ?";
                String deleteQuestionSQL = "DELETE FROM questions WHERE id = ?";
                try (PreparedStatement deleteOptionsStmt = conn.prepareStatement(deleteOptionsSQL);
                     PreparedStatement deleteQuestionStmt = conn.prepareStatement(deleteQuestionSQL)) {

                    for (Question deleted : deletedQuestions) {
                        deleteOptionsStmt.setInt(1, deleted.getId());
                        deleteOptionsStmt.executeUpdate();

                        deleteQuestionStmt.setInt(1, deleted.getId());
                        deleteQuestionStmt.executeUpdate();
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (quizStmt != null) quizStmt.close();
            if (questionStmt != null) questionStmt.close();
            if (insertQuestionStmt != null) insertQuestionStmt.close();
            if (insertOptionStmt != null) insertOptionStmt.close();
            if (optionStmt != null) optionStmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Inserts a new quiz into the database.
     *
     * @param quiz the quiz to insert
     * @return the generated quiz ID
     * @throws SQLException if a database access error occurs
     */
    public static int insertQuiz(Quiz quiz) throws SQLException {
        String insertQuizSQL = "INSERT INTO quizzes (name) VALUES (?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuizSQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, quiz.getName());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting quiz failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    quiz.setId(generatedId);
                    return generatedId;
                } else {
                    throw new SQLException("Inserting quiz failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Deletes a quiz and all associated questions and options from the database.
     *
     * @param quizId the ID of the quiz to delete
     * @throws SQLException if a database access error occurs
     */
    public static void deleteQuiz(int quizId) throws SQLException {
        String deleteOptionsSQL = "DELETE FROM options WHERE question_id IN (SELECT id FROM questions WHERE quiz_id = ?)";
        String deleteQuestionsSQL = "DELETE FROM questions WHERE quiz_id = ?";
        String deleteQuizSQL = "DELETE FROM quizzes WHERE id = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteOptionsStmt = conn.prepareStatement(deleteOptionsSQL);
                 PreparedStatement deleteQuestionsStmt = conn.prepareStatement(deleteQuestionsSQL);
                 PreparedStatement deleteQuizStmt = conn.prepareStatement(deleteQuizSQL)) {

                deleteOptionsStmt.setInt(1, quizId);
                deleteOptionsStmt.executeUpdate();

                deleteQuestionsStmt.setInt(1, quizId);
                deleteQuestionsStmt.executeUpdate();

                deleteQuizStmt.setInt(1, quizId);
                deleteQuizStmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}
