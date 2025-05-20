package com.cab302ai_teacher.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    @Test
    public void testQuestionCreationAndAccessors() {
        List<String> options = Arrays.asList("Yes", "No", "Maybe");
        List<Integer> correct = List.of(0, 2);

        Question q = new Question("Is Java fun?", options, correct, 42);

        assertEquals("Is Java fun?", q.getQuestion());
        assertEquals(options, q.getOptions());
        assertEquals(correct, q.getCorrectIndexes());
        assertEquals(42, q.getId());
    }

    @Test
    public void testSettersModifyValues() {
        Question q = new Question("Initial", List.of("A", "B"), List.of(0), 1);

        q.setQuestion("Updated?");
        q.setOptions(List.of("X", "Y", "Z"));
        q.setCorrectIndexes(List.of(2));
        q.setId(99);

        assertEquals("Updated?", q.getQuestion());
        assertEquals(List.of("X", "Y", "Z"), q.getOptions());
        assertEquals(List.of(2), q.getCorrectIndexes());
        assertEquals(99, q.getId());
    }
}
