package com.grzegorzpanasiuk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TaskList {
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> findAll() {
        return Collections.unmodifiableList(tasks);
    }

    public void add(String description) {
        tasks.add(new Task(description));
    }

    public boolean deleteById(int id) {
        return tasks.removeIf(task -> task.getId() == id);
    }

    public Optional<Task> findById(int id) {
        return tasks
                .stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }
}
