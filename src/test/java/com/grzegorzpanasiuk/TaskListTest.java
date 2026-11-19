package com.grzegorzpanasiuk;

import org.junit.jupiter.api.Test;

import java.util.List;

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
}
