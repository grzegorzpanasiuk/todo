package com.grzegorzpanasiuk;

public class Task {
    static final int MAX_DESCRIPTION_LENGTH = 45;
    private static int nextId = 0;
    private final int id;
    private String description;
    private boolean done;

    public Task(String description, boolean done) {
        if (!isValidDescription(description)) {
            throw new IllegalArgumentException("Invalid description: '" + description + "'");
        }
        this.id = ++nextId;
        this.description = description;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public static boolean isValidDescription(String description) {
        return !isDescriptionBlank(description) && !isDescriptionTooLong(description);
    }

    static boolean isDescriptionBlank(String description) {
        return description.isBlank();
    }

    static boolean isDescriptionTooLong(String description) {
        return description.length() > MAX_DESCRIPTION_LENGTH;
    }
}
