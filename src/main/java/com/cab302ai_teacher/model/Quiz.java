package com.cab302ai_teacher.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String name;
    private int id;
    private final List<Question> questions;

    public Quiz(String name, List<Question> questions, int id) {
        this.name = name;
        this.questions = questions;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
