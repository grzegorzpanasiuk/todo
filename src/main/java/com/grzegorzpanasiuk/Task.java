package com.grzegorzpanasiuk;

public class Task {
    static final int MIN_DESCRIPTION_LENGTH = 3;
    static final int MAX_DESCRIPTION_LENGTH = 45;

    private final int id;
    private final String description;
    private boolean done;

    public Task(int id, String description) {
        if (!isValidDescription(description)) {
            throw new IllegalArgumentException("Invalid description: '" + description + "'");
        }
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public boolean markAsDone() {
        if (isDone()) {
            return false;
        }
        this.done = true;
        return true;
    }

    public static boolean isValidDescription(String description) {
        return !isDescriptionBlank(description)
                && !isDescriptionTooShort(description)
                && !isDescriptionTooLong(description);
    }

    static boolean isDescriptionBlank(String description) {
        return description.isBlank();
    }

    static boolean isDescriptionTooShort(String description) {
        return description.length() < MIN_DESCRIPTION_LENGTH;
    }

    static boolean isDescriptionTooLong(String description) {
        return description.length() > MAX_DESCRIPTION_LENGTH;
    }
}
