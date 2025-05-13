package com.cab302ai_teacher.util;

import com.cab302ai_teacher.model.*;

import java.util.Arrays;
import java.util.List;

public class MockQuizData {

    private static Quiz quiz;

    static {
        quiz = new Quiz();

        quiz.addQuestion(new Question(
                "What is the capital of France?",
                Arrays.asList("Berlin", "Madrid", "Paris", "Rome"),
                List.of(2)
        ));

        quiz.addQuestion(new Question(
                "Which planet is known as the Red Planet?",
                Arrays.asList("Earth", "Mars", "Jupiter", "Venus"),
                List.of(1)
        ));

        quiz.addQuestion(new Question(
                "Which of the following are programming languages?",
                Arrays.asList("HTML", "Java", "CSS", "Python"),
                Arrays.asList(1, 3)
        ));

        quiz.addQuestion(new Question(
                "What is the boiling point of water?",
                Arrays.asList("90°C", "100°C", "110°C", "120°C"),
                List.of(1)
        ));

        quiz.addQuestion(new Question(
                "Which language is used for Android development?",
                Arrays.asList("Swift", "Kotlin", "Ruby", "JavaScript"),
                List.of(1)
        ));

        // Question 1
        quiz.addQuestion(new Question(
                "What is the capital city of Australia?",
                List.of("Melbourne", "Sydney", "Canberra", "Perth"),
                List.of(2) // Correct answer: Canberra
        ));

        // Question 2
        quiz.addQuestion(new Question(
                "Which of these is a prime number?",
                List.of("4", "6", "7", "9"),
                List.of(2) // Correct answer: 7
        ));

        // Question 3
        quiz.addQuestion(new Question(
                "What is the largest planet in our solar system?",
                List.of("Earth", "Mars", "Jupiter", "Saturn"),
                List.of(2) // Correct answer: Jupiter
        ));

        // Question 4
        quiz.addQuestion(new Question(
                "Which country is known as the Land of the Rising Sun?",
                List.of("China", "Japan", "Korea", "Thailand"),
                List.of(1) // Correct answer: Japan
        ));

        // Question 5
        quiz.addQuestion(new Question(
                "Which gas do plants absorb from the atmosphere during photosynthesis?",
                List.of("Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"),
                List.of(2) // Correct answer: Carbon Dioxide
        ));

        // Question 6
        quiz.addQuestion(new Question(
                "What is the square root of 64?",
                List.of("6", "7", "8", "9"),
                List.of(2) // Correct answer: 8
        ));

        // Question 7 (Multiple Correct Answers)
        quiz.addQuestion(new Question(
                "Which of these are programming languages?",
                List.of("Java", "Python", "HTML", "Banana"),
                List.of(0, 1, 2) // Correct answers: Java, Python, HTML
        ));

        // Question 8
        quiz.addQuestion(new Question(
                "Who wrote 'Romeo and Juliet'?",
                List.of("William Shakespeare", "Charles Dickens", "Jane Austen", "J.K. Rowling"),
                List.of(0) // Correct answer: William Shakespeare
        ));

        // Question 9
        quiz.addQuestion(new Question(
                "What is the chemical symbol for water?",
                List.of("H2O", "O2", "CO2", "H2"),
                List.of(0) // Correct answer: H2O
        ));

        // Question 10
        quiz.addQuestion(new Question(
                "Which of these is the tallest mountain on Earth?",
                List.of("Mount Kilimanjaro", "Mount Everest", "Mount Fuji", "Mount McKinley"),
                List.of(1) // Correct answer: Mount Everest
        ));
    }

    public static Quiz getQuiz() {
        return quiz;
    }
}
