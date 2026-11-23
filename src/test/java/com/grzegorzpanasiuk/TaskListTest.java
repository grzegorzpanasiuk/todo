package com.grzegorzpanasiuk;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    public void newTaskListIsEmpty() {
        TaskList taskList = TaskList.empty();

        List<Task> tasks = taskList.findAll();

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void addedTaskIsReturnedByFindAll() {
        TaskList taskList = TaskList.empty();
        String description = "Learn Java";

        taskList.add(description);

        List<Task> tasks = taskList.findAll();
        assertEquals(1, tasks.size());
        assertEquals(description, tasks.getFirst().getDescription());
    }

    @Test
    public void addReturnsIdOfAddedTask() {
        TaskList taskList = TaskList.empty();

        int id = taskList.add("Learn TDD");

        assertEquals(taskList.findAll().getFirst().getId(), id);
    }

    @Test
    public void tasksReturnedByFindAllCannotBeModified() {
        TaskList taskList = TaskList.empty();

        List<Task> tasks = taskList.findAll();

        assertThrows(UnsupportedOperationException.class, () -> tasks.add(new Task("Learn Java")));
    }

    @Test
    public void deletedTaskIsNotReturnedByFindAll() {
        TaskList taskList = TaskList.empty();
        int id = taskList.add("Task 1");

        taskList.deleteById(id);

        assertTrue(taskList.findAll().isEmpty());
    }

    @Test
    public void deletingExistingTaskReturnsTrue() {
        TaskList taskList = TaskList.empty();
        int id = taskList.add("Task 2");

        boolean deleted = taskList.deleteById(id);

        assertTrue(deleted);
    }

    @Test
    public void deletingNonExistentTaskReturnsFalse() {
        TaskList taskList = TaskList.empty();

        boolean deleted = taskList.deleteById(1);

        assertFalse(deleted);
    }
}
