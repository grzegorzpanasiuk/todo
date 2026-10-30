public class Task {
    private static int nextId = 0;
    private final int id;
    private String description;
    private boolean done;

    public Task(String description, boolean done) {
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

    public String convertStatus() {
        if (!this.isDone()) return "[]";
        else return "[x]";
    }

    public String displayTask() {
        return String.format("Task: %03d | Description: %-45.45s | Status: %-3.3s",
                this.getId(), this.getDescription(), this.convertStatus());
    }
}
