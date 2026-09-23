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
        StringBuilder sb = new StringBuilder("[");
        if (this.isDone()) sb.append("x");
        return sb.append("]").toString();
    }

    public String displayTask() {
        return "Task: " + this.getId() +
                " | Description: " + this.getDescription() +
                " | Status: " + this.convertStatus();
    }
}
