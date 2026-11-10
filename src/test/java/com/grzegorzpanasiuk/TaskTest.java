package com.grzegorzpanasiuk;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Random.class)
public class TaskTest {

    @Test
    public void newTaskHasEmptyStatus() {
        Task newTask = new Task("This is description", false);

        String taskStatus = newTask.convertStatus();

        assertEquals("[]", taskStatus);
    }

    @Test
    public void markingTaskAsDoneChangesStatusToX() {
        Task task = new Task("This is another description", false);

        task.setDone(true);
        String taskStatus = task.convertStatus();

        assertEquals("[x]", taskStatus);
    }
}
