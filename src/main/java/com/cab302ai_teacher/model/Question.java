package com.cab302ai_teacher.model;

import java.util.List;

public class Question {
    private String question;
    private List<String> options;
    private List<Integer> correctIndexes;

    public Question(String question, List<String> options, List<Integer> correctIndexes) {
        this.question = question;
        this.options = options;
        this.correctIndexes = correctIndexes;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setCorrectIndexes(List<Integer> correctAnswerIndex) {
        this.correctIndexes = correctIndexes;
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
}
