package com.cab302ai_teacher.model;

import java.util.List;

public class Question {
    private int id;
    private String question;
    private List<String> options;
    private List<Integer> correctIndexes;

    public Question(String question, List<String> options, List<Integer> correctIndexes, int id) {
        this.question = question;
        this.options = options;
        this.correctIndexes = correctIndexes;
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setCorrectIndexes(List<Integer> correctAnswerIndex) {
        this.correctIndexes = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getCorrectIndexes() {
        return correctIndexes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
