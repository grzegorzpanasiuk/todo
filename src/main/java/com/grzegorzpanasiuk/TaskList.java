package com.grzegorzpanasiuk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> findAll() {
        return Collections.unmodifiableList(tasks);
    }

    public void add(String description) {
        tasks.add(new Task(description));
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
