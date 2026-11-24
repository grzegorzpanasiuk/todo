package com.grzegorzpanasiuk;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Random.class)
public class TaskTest {

    @Test
    public void newTaskIsNotDone() {
        Task newTask = new Task(1, "This is description");

        assertFalse(newTask.isDone());
    }

    @Test
    public void taskMarkedAsDoneIsDone() {
        Task task = new Task(1, "This is another description");

        task.markAsDone();

        assertTrue(task.isDone());
    }

    @Test
    public void markingNotDoneTaskAsDoneReturnsTrue() {
        Task task = new Task(1, "This is another description");

        boolean marked = task.markAsDone();

        assertTrue(marked);
    }

    @Test
    public void markingAlreadyDoneTaskAsDoneReturnsFalse() {
        Task task = new Task(1, "This is description");
        task.markAsDone();

        boolean marked = task.markAsDone();

        assertFalse(marked);
    }

    @Test
    public void cannotCreateTaskWithBlankDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1, "  ")
        );
    }

    @Test
    public void cannotCreateTaskWithTooShortDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1, "a".repeat(Task.MIN_DESCRIPTION_LENGTH - 1))
        );
    }

    @Test
    public void cannotCreateTaskWithTooLongDescription() {
        assertThrows(IllegalArgumentException.class, () ->
                new Task(1, "a".repeat(Task.MAX_DESCRIPTION_LENGTH + 1))
        );
    }

    @Test
    public void blankDescriptionIsNotValid() {
        assertFalse(Task.isValidDescription("  "));
    }

    @Test
    public void nonBlankDescriptionIsValid() {
        assertTrue(Task.isValidDescription("Learn Java"));
    }

    @Test
    public void tooLongDescriptionIsNotValid() {
        assertFalse(Task.isValidDescription("a".repeat(Task.MAX_DESCRIPTION_LENGTH + 1)));
    }

    @Test
    public void descriptionWithMaxLengthIsValid() {
        assertTrue(Task.isValidDescription("a".repeat(Task.MAX_DESCRIPTION_LENGTH)));
    }

    @Test
    public void tooShortDescriptionIsNotValid() {
        assertFalse(Task.isValidDescription("a".repeat(Task.MIN_DESCRIPTION_LENGTH - 1)));
    }

    @Test
    public void descriptionWithMinLengthIsValid() {
        assertTrue(Task.isValidDescription("a".repeat(Task.MIN_DESCRIPTION_LENGTH)));
    }
}
