import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static void displayMessage(String message, boolean addNewLine) {
        if (addNewLine) message += "\n";
        System.out.print(message);
    }

    private static Task findGivenTask(List<Task> tasks, int taskNumber) {
        Task taskFound = null;
        for (Task task: tasks) {
            if (task.getId() == taskNumber) {
                taskFound = task;
                break;
            }
        }
        return taskFound;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();
        Command command = null;
        String userText;
        int taskNumber;
        boolean found;

        do {
            userText = scanner.nextLine().toUpperCase().trim();
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
                    String desc = scanner.nextLine().trim();
                    if (desc.isBlank()) {
                        displayMessage("Description cannot be empty!", true);
                        break;
                    }
                    tasks.add(new Task(desc, false));
                    displayMessage("New task added.", true);
                    break;
                case DELETE:
                    displayMessage("Delete task with number: ", false);
                    String taskToDelete = scanner.nextLine().trim();
                    try {
                        taskNumber = Integer.parseInt(taskToDelete);
                    } catch (NumberFormatException e) {
                        displayMessage("You didn't enter a number!", true);
                        break;
                    }
                    found = false;
                    Task taskToBeDeleted = findGivenTask(tasks, taskNumber);
                    if (taskToBeDeleted != null) {
                        found = true;
                        tasks.remove(taskToBeDeleted);
                        displayMessage("Task with number " + taskNumber + " was deleted.", true);
                    }
                    if (!found) displayMessage("Task with number " + taskNumber + " does not exist!", true);
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
                case MARK:
                    displayMessage("Mark given task as done, enter task number: ", false);
                    String taskToMarkAsDone = scanner.nextLine().trim();
                    try {
                        taskNumber = Integer.parseInt(taskToMarkAsDone);
                    } catch (NumberFormatException e) {
                        displayMessage("You didn't enter a number!", true);
                        break;
                    }
                    found = false;
                    Task taskToBeMarked = findGivenTask(tasks, taskNumber);
                    if (taskToBeMarked != null) {
                        if (taskToBeMarked.isDone()) {
                            displayMessage("Task with number " + taskNumber + " was already marked as done!", true);
                            break;
                        }
                        found = true;
                        taskToBeMarked.setDone(true);
                        displayMessage("Task with number " + taskNumber + " was marked as done.", true);
                    }
                    if (!found) displayMessage("Task with number " + taskNumber + " does not exist!", true);
                    break;
            }
        } while (command != Command.EXIT);
        scanner.close();
    }
}