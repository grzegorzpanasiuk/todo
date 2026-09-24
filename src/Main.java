import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static void displayMessage(String message, boolean addNewLine) {
        if (addNewLine) message += "\n";
        System.out.print(message);
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();
        Command command = Command.HELP;
        String userText;

        do {
            userText = scanner.nextLine().toUpperCase();
            try {
                command = Command.valueOf(userText);
            } catch (IllegalArgumentException e) {
                displayMessage("Invalid command, allowed commands are: "
                        + Arrays.stream(Command.values()).toList(), true);
                continue;
            }
            switch (command) {
                case ADD:
                    displayMessage("Description: ", false);
                    String desc = scanner.nextLine();
                    if (desc.isBlank()) {
                        displayMessage("Description cannot be empty", true);
                        break;
                    }
                    tasks.add(new Task(desc, false));
                    displayMessage("New task added.", true);
                    break;
                case DELETE:
                    displayMessage("Delete task with number: ", false);
                    String taskToDelete = scanner.nextLine();
                    // TODO
                    displayMessage("You want to delete task with number " + taskToDelete, true);
                    break;
                case HELP:
                    displayMessage("Commands: " + Arrays.stream(Command.values()).toList(), true);
                    break;
                case LIST:
                    if (tasks.isEmpty()) {
                        displayMessage("Task list is empty.", true);
                        break;
                    }
                    for (Task task : tasks) {
                        displayMessage(task.displayTask(), true);
                    }
                    break;
                case MARK_AS_DONE:
                    displayMessage("Mark given task as done: ", false);
                    String taskToMarkAsDone = scanner.nextLine();
                    // TODO
                    displayMessage("You want to mark task number " + taskToMarkAsDone + " as done", true);
                    break;
            }
        } while (command != Command.EXIT);
        scanner.close();
    }
}