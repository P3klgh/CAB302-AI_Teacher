package com.cab302ai_teacher.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz {
    private String quizName;
    private int id;
    private List<Question> questions;

    // Constructor with quiz name, question list, and ID
    public Quiz(String quizName, List<Question> questions, int id) {
        this.quizName = quizName;
        this.questions = List.copyOf(questions); // Store as immutable list
        this.id = id;
    }

    // Constructor with default ID (-1)
    public Quiz(String quizName, List<Question> questions) {
        this(quizName, questions, -1);
    }

    // Getters and setters
    public String getQuizName() {
        return quizName;
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions); // Expose as unmodifiable
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return quizName;
    }

    // Add a new question to the quiz
    public void addQuestion(Question question) {
        List<Question> modifiable = new ArrayList<>(this.questions);
        modifiable.add(question);
        this.questions = List.copyOf(modifiable); // Re-wrap as immutable
    }

    // Remove a question from the quiz
    public void removeQuestion(Question question) {
        List<Question> modifiable = new ArrayList<>(this.questions);
        modifiable.remove(question);
        this.questions = List.copyOf(modifiable); // Re-wrap as immutable
    }

    // Replace all questions
    public void setQuestions(List<Question> questions) {
        this.questions = List.copyOf(questions); // Store as immutable
    }
}
