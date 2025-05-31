package com.cab302ai_teacher.model;

import java.util.List;

/**
 * Represents a quiz question including its text, options, and correct answers.
 */
public class Question {

    /** Unique identifier of the question in the database */
    private int id;

    /** The question text */
    private String question;

    /** List of answer options */
    private List<String> options;

    /** Indexes of the correct options */
    private List<Integer> correctIndexes;

    /**
     * Constructs a new Question instance.
     *
     * @param question        the question text
     * @param options         the list of answer options
     * @param correctIndexes  the list of indexes representing correct answers
     * @param id              unique identifier for the question
     */
    public Question(String question, List<String> options, List<Integer> correctIndexes, int id) {
        this.question = question;
        this.options = options;
        this.correctIndexes = correctIndexes;
        this.id = id;
    }

    /**
     * @return the question text
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return the list of answer options
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * @return the list of indexes indicating correct answers
     */
    public List<Integer> getCorrectIndexes() {
        return correctIndexes;
    }

    /**
     * @return the question ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the question text.
     * @param question the new question text
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets the list of answer options.
     * @param options the new list of options
     */
    public void setOptions(List<String> options) {
        this.options = options;
    }

    /**
     * Sets the list of indexes representing the correct answers.
     * @param correctIndexes the new list of correct indexes
     */
    public void setCorrectIndexes(List<Integer> correctIndexes) {
        this.correctIndexes = correctIndexes;
    }

    /**
     * Sets the question ID.
     * @param id the new question ID
     */
    public void setId(int id) {
        this.id = id;
    }
}
