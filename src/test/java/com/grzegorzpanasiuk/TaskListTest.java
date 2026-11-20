package com.grzegorzpanasiuk;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void newTaskListIsEmpty() {
        TaskList taskList = new TaskList();

        List<Task> tasks = taskList.findAll();

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void addedTaskIsReturnedByFindAll() {
        TaskList taskList = new TaskList();
        String description = "Learn Java";

        taskList.add(description);

        List<Task> tasks = taskList.findAll();
        assertEquals(1, tasks.size());
        assertEquals(description, tasks.getFirst().getDescription());
    }

    @Test
    public void tasksReturnedByFindAllCannotBeModified() {
        TaskList taskList = new TaskList();

        List<Task> tasks = taskList.findAll();

        assertThrows(UnsupportedOperationException.class, () -> tasks.add(new Task("Learn Java")));
    }

    @Test
    public void deletedTaskIsNotReturnedByFindAll() {
        TaskList taskList = new TaskList();
        taskList.add("Task 1");
        int id = taskList.findAll().getFirst().getId();

        taskList.deleteById(id);

        assertTrue(taskList.findAll().isEmpty());
    }

    @Test
    public void deletingExistingTaskReturnsTrue() {
        TaskList taskList = new TaskList();
        taskList.add("Task 2");
        int id = taskList.findAll().getFirst().getId();

        boolean deleted = taskList.deleteById(id);

        assertTrue(deleted);
    }

    @Test
    public void deletingNonExistentTaskReturnsFalse() {
        TaskList taskList = new TaskList();

        boolean deleted = taskList.deleteById(1);

        assertFalse(deleted);
    }

    @Test
    public void findByIdReturnsTaskWithGivenIdAmongMany() {
        TaskList taskList = new TaskList();
        String secondDescription = "Task 2";
        taskList.add("Task 1");
        taskList.add(secondDescription);
        int secondId = taskList.findAll().get(1).getId();

        Optional<Task> found = taskList.findById(secondId);

        assertEquals(secondDescription, found.orElseThrow().getDescription());
    }

    @Test
    public void findByIdReturnsEmptyWhenTaskDoesNotExist() {
        TaskList taskList = new TaskList();
        taskList.add("Task 1");
        int missingId = taskList.findAll().getFirst().getId() + 1;

        Optional<Task> found = taskList.findById(missingId);

        assertTrue(found.isEmpty());
    }
}
