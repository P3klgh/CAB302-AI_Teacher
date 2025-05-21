package com.cab302ai_teacher.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizTest {

    @Test
    public void testQuizInitializationWithId() {
        Question q1 = new Question("Question?", List.of("A", "B"), List.of(0), 1);
        Quiz quiz = new Quiz("Sample Quiz", List.of(q1), 42);

        assertEquals("Sample Quiz", quiz.getQuizName());
        assertEquals(1, quiz.getQuestions().size());
        assertEquals(42, quiz.getId());
    }

    @Test
    public void testQuizInitializationWithoutId() {
        Question q1 = new Question("Question?", List.of("A", "B"), List.of(0), 1);
        Quiz quiz = new Quiz("Quiz Without ID", List.of(q1));

        assertEquals("Quiz Without ID", quiz.getQuizName());
        assertEquals(-1, quiz.getId()); // default ID
    }

    @Test
    public void testImmutableQuestionsList() {
        Question q1 = new Question("Question?", List.of("A", "B"), List.of(0), 1);
        Quiz quiz = new Quiz("Immutable Test", List.of(q1));

        assertThrows(UnsupportedOperationException.class, () -> {
            quiz.getQuestions().add(new Question("Another?", List.of("C", "D"), List.of(1), 2));
        });
    }

    @Test
    public void testSetQuizName() {
        Question q1 = new Question("Q?", List.of("Yes", "No"), List.of(0), 1);
        Quiz quiz = new Quiz("Old Name", List.of(q1));

        quiz.setQuizName("New Name");
        assertEquals("New Name", quiz.getQuizName());
    }

    @Test
    public void testSetId() {
        Quiz quiz = new Quiz("Any Quiz", List.of(
                new Question("Q?", List.of("X", "Y"), List.of(1), 1)
        ));
        quiz.setId(123);
        assertEquals(123, quiz.getId());
    }
}
