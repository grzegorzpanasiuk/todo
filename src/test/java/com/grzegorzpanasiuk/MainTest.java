package com.grzegorzpanasiuk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void emptyDescriptionHasEmptyAndTooShortErrors() {
        List<String> errors = Main.findDescriptionErrors("");

        assertEquals(List.of(
                "Description cannot be empty!",
                "Description cannot be shorter than " + Task.MIN_DESCRIPTION_LENGTH + " characters!"
        ), errors);
    }

    @Test
    public void validDescriptionHasNoErrors() {
        List<String> errors = Main.findDescriptionErrors("Learn Java");

        assertTrue(errors.isEmpty());
    }

    @Test
    public void tooShortDescriptionHasOnlyTooShortError() {
        List<String> errors = Main.findDescriptionErrors("a".repeat(Task.MIN_DESCRIPTION_LENGTH - 1));

        assertEquals(List.of(
                "Description cannot be shorter than " + Task.MIN_DESCRIPTION_LENGTH + " characters!"
        ), errors);
    }

    @Test
    public void tooLongDescriptionHasOnlyTooLongError() {
        List<String> errors = Main.findDescriptionErrors("a".repeat(Task.MAX_DESCRIPTION_LENGTH + 1));

        assertEquals(List.of(
                "Description cannot be longer than " + Task.MAX_DESCRIPTION_LENGTH + " characters!"
        ), errors);
    }

    @Test
    public void validDescriptionIsAddedToTasks() {
        TaskList taskList = TaskList.empty();
        String description = "Learn Java";

        List<String> errors = Main.addTask(description, taskList);

        assertTrue(errors.isEmpty());
        assertEquals(1, taskList.findAll().size());
        assertEquals(description, taskList.findAll().getFirst().getDescription());
    }

    @Test
    public void emptyDescriptionIsNotAdded() {
        TaskList taskList = TaskList.empty();

        List<String> errors = Main.addTask("", taskList);

        assertTrue(taskList.findAll().isEmpty());
        assertTrue(errors.contains("Description cannot be empty!"));
    }
}
