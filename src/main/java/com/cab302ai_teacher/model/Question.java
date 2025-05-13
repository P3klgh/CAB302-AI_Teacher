package com.cab302ai_teacher.model;

import java.util.List;

public class Question {
    private String question;
    private List<String> options;
    private int correctAnswerIndex;

    public Question(String question, List<String> options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion(String question) {
        return question;
    }

    public List<String> getOptions(List<String> options) {
        return options;
    }

    public int getCorrectAnswerIndex(int correctAnswerIndex) {
        return correctAnswerIndex;
    }
}
