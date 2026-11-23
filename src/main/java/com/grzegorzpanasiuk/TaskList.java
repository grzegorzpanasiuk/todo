package com.grzegorzpanasiuk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    private TaskList() {
    }

    public static TaskList empty() {
        return new TaskList();
    }

    public List<Task> findAll() {
        return Collections.unmodifiableList(tasks);
    }

    public int add(String description) {
        Task newTask = new Task(description);
        tasks.add(newTask);
        return newTask.getId();
    }

    /**
     * Removes the task with the given id.
     * <p>
     * This repository holds no business rules, so {@code false} means only that
     * no task with this id exists. Rules that may forbid deleting a task must be
     * checked by the caller before calling this method.
     *
     * @param id the id of the task to remove
     * @return {@code true} if a task was removed, {@code false} if no task has this id
     */
    public boolean deleteById(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }
}
