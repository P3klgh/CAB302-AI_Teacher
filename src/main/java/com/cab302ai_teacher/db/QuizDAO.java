package com.cab302ai_teacher.db;

import com.cab302ai_teacher.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    public static List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String quizQuery = "SELECT * FROM quizzes";

        try (Connection conn = DatabaseManager.connect();
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

    private static List<Question> getQuestionsByQuizId(Connection conn, int quizId) throws SQLException {
        List<Question> questions = new ArrayList<>();

        String questionQuery = "SELECT * FROM questions WHERE quiz_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(questionQuery)) {
            stmt.setInt(1, quizId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int questionId = rs.getInt("id");
                    String questionText = rs.getString("question_text");

                    // Get options and correct indexes
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


    public static void updateQuiz(Quiz quiz) throws SQLException {
        Connection conn = null;
        PreparedStatement quizStmt = null;
        PreparedStatement questionStmt = null;
        PreparedStatement optionStmt = null;

        try {
            conn = DatabaseManager.connect();
            conn.setAutoCommit(false);  // Start transaction

            // 1. Update quiz name
            String updateQuizSQL = "UPDATE quizzes SET name = ? WHERE id = ?";
            quizStmt = conn.prepareStatement(updateQuizSQL);
            quizStmt.setString(1, quiz.getName());
            quizStmt.setInt(2, quiz.getId());
            quizStmt.executeUpdate();

            // 2. Update each question and its options
            String updateQuestionSQL = "UPDATE questions SET question_text = ? WHERE id = ?";
            questionStmt = conn.prepareStatement(updateQuestionSQL);

            String updateOptionSQL = "UPDATE options SET option_text = ?, is_correct = ? WHERE id = ?";
            optionStmt = conn.prepareStatement(updateOptionSQL);

            for (Question question : quiz.getQuestions()) {
                // Update question
                questionStmt.setString(1, question.getQuestion());
                questionStmt.setInt(2, question.getId());
                questionStmt.executeUpdate();

                // Get option IDs for this question
                List<Integer> optionIds = getOptionIdsForQuestion(conn, question.getId());
                List<String> options = question.getOptions();
                List<Integer> correctIndexes = question.getCorrectIndexes();

                for (int i = 0; i < options.size() && i < optionIds.size(); i++) {
                    int optionId = optionIds.get(i);

                    optionStmt.setString(1, options.get(i));
                    optionStmt.setBoolean(2, correctIndexes.contains(i));
                    optionStmt.setInt(3, optionId);
                    optionStmt.executeUpdate();
                }
            }

            conn.commit();  // All good
        } catch (SQLException e) {
            if (conn != null) conn.rollback();  // Roll back on failure
            throw e;
        } finally {
            if (quizStmt != null) quizStmt.close();
            if (questionStmt != null) questionStmt.close();
            if (optionStmt != null) optionStmt.close();
            if (conn != null) conn.close();
        }
    }
}