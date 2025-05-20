package com.cab302ai_teacher.model;

import java.util.Collections;
import java.util.List;

public class Quiz {
    private String quizName;
    private int id;
    private final List<Question> questions;

    public Quiz(String quizName, List<Question> questions, int id) {
        this.quizName = quizName;
        this.questions = List.copyOf(questions); // immutable
        this.id = id;
    }

    public Quiz(String quizName, List<Question> questions) {
        this(quizName, questions, -1); // default id
    }

    public String getQuizName() {
        return quizName;
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
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
}