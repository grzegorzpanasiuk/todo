package com.grzegorzpanasiuk;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
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
        List<Task> tasks = new ArrayList<>();
        String description = "Learn Java";

        List<String> errors = Main.addTask(description, tasks);

        assertTrue(errors.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals(description, tasks.getFirst().getDescription());
    }

    @Test
    public void emptyDescriptionIsNotAdded() {
        List<Task> tasks = new ArrayList<>();

        List<String> errors = Main.addTask("", tasks);

        assertTrue(tasks.isEmpty());
        assertTrue(errors.contains("Description cannot be empty!"));
    }
}
