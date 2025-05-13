package com.cab302ai_teacher.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private final List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
