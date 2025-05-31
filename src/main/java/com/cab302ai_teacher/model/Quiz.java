package com.cab302ai_teacher.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a quiz that contains a list of questions and a quiz name.
 */
public class Quiz {

    /** The name of the quiz */
    private String quizName;

    /** Unique identifier for the quiz */
    private int id;

    /** List of questions in the quiz */
    private List<Question> questions;

    /**
     * Constructs a Quiz object with a given name, list of questions, and ID.
     *
     * @param quizName  the title of the quiz
     * @param questions the list of questions in the quiz
     * @param id        the quiz's unique ID
     */
    public Quiz(String quizName, List<Question> questions, int id) {
        this.quizName = quizName;
        this.questions = List.copyOf(questions); // Store as immutable list
        this.id = id;
    }

    /**
     * Constructs a Quiz with default ID (-1).
     *
     * @param quizName  the quiz title
     * @param questions the list of questions
     */
    public Quiz(String quizName, List<Question> questions) {
        this(quizName, questions, -1);
    }

    /**
     * Returns the quiz name.
     *
     * @return the name of the quiz
     */
    public String getQuizName() {
        return quizName;
    }

    /**
     * Returns an unmodifiable list of questions in the quiz.
     *
     * @return the quiz questions
     */
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    /**
     * Sets the name of the quiz.
     *
     * @param quizName new quiz name
     */
    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    /**
     * Returns the ID of the quiz.
     *
     * @return quiz ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the quiz.
     *
     * @param id new quiz ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the quiz name (duplicate of getQuizName for compatibility).
     *
     * @return quiz name
     */
    public String getName() {
        return quizName;
    }

    /**
     * Adds a new question to the quiz.
     *
     * @param question the question to add
     */
    public void addQuestion(Question question) {
        List<Question> modifiable = new ArrayList<>(this.questions);
        modifiable.add(question);
        this.questions = List.copyOf(modifiable);
    }

    /**
     * Removes a question from the quiz.
     *
     * @param question the question to remove
     */
    public void removeQuestion(Question question) {
        List<Question> modifiable = new ArrayList<>(this.questions);
        modifiable.remove(question);
        this.questions = List.copyOf(modifiable);
    }

    /**
     * Replaces all questions in the quiz.
     *
     * @param questions new list of questions
     */
    public void setQuestions(List<Question> questions) {
        this.questions = List.copyOf(questions);
    }
}
