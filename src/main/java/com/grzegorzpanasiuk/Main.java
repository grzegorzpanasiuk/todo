package com.grzegorzpanasiuk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final int DESCRIPTION_COLUMN_WIDTH = Task.MAX_DESCRIPTION_LENGTH;
    private static final String TASK_ROW_FORMAT =
            "Task: %03d"
                    + " | Description: %-" + DESCRIPTION_COLUMN_WIDTH + "." + DESCRIPTION_COLUMN_WIDTH + "s"
                    + " | Status: %-3.3s";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TaskList taskList = TaskList.empty();
        Command command = null;
        String userText;

        do {
            System.out.println("Enter the command (type HELP for a list of allowed commands)");
            userText = readTrimmedLine(scanner).orElse(Command.EXIT.name()).toUpperCase();
            try {
                command = Command.valueOf(userText);
            } catch (IllegalArgumentException e) {
                displayMessage("Invalid command, allowed commands are: "
                        + Arrays.stream(Command.values()).toList(), true);
                continue;
            }
            switch (command) {
                case ADD:
                    displayMessage("Description (" + Task.MIN_DESCRIPTION_LENGTH
                            + "-" + Task.MAX_DESCRIPTION_LENGTH + " characters): ", false);
                    Optional<String> description = readTrimmedLine(scanner);
                    if (description.isEmpty()) {
                        System.out.println();
                        command = Command.EXIT;
                        break;
                    }
                    addTaskAndReport(description.orElseThrow(), taskList);
                    break;
                case DELETE:
                    displayMessage("Delete task with number: ", false);
                    Optional<String> taskToDelete = readTrimmedLine(scanner);
                    if (taskToDelete.isEmpty()) {
                        System.out.println();
                        command = Command.EXIT;
                        break;
                    }
                    int taskNumberToBeDeleted;
                    try {
                        taskNumberToBeDeleted = Integer.parseInt(taskToDelete.orElseThrow());
                    } catch (NumberFormatException e) {
                        displayMessage("You didn't enter a number!", true);
                        break;
                    }
                    boolean deleted = taskList.deleteById(taskNumberToBeDeleted);
                    if (deleted) {
                        displayMessage("Task with number " + taskNumberToBeDeleted + " was deleted.", true);
                    } else {
                        displayMessage("Task with number " + taskNumberToBeDeleted + " does not exist!", true);
                    }
                    break;
                case HELP:
                    displayMessage("Commands: " + Arrays.stream(Command.values()).toList(), true);
                    break;
                case LIST:
                    List<Task> allTasks = taskList.findAll();
                    if (allTasks.isEmpty()) {
                        displayMessage("Task list is empty.", true);
                        break;
                    }
                    printAllTasks(allTasks);
                    break;
                case MARK:
                    displayMessage("Mark given task as done, enter task number: ", false);
                    Optional<String> taskToMarkAsDone = readTrimmedLine(scanner);
                    if (taskToMarkAsDone.isEmpty()) {
                        System.out.println();
                        command = Command.EXIT;
                        break;
                    }
                    int taskNumberToBeMarked;
                    try {
                        taskNumberToBeMarked = Integer.parseInt(taskToMarkAsDone.orElseThrow());
                    } catch (NumberFormatException e) {
                        displayMessage("You didn't enter a number!", true);
                        break;
                    }

                    Optional<Task> found = taskList.findById(taskNumberToBeMarked);
                    found.ifPresentOrElse(
                            task -> {
                                boolean marked = task.markAsDone();
                                if (marked) {
                                    displayMessage("Task with number " + task.getId() + " was marked as done.", true);
                                } else {
                                    displayMessage("Task with number " + task.getId() + " was already marked as done!", true);
                                }
                            },
                            () -> displayMessage("Task with number " + taskNumberToBeMarked + " does not exist!", true)
                    );
                    break;
            }
        } while (command != Command.EXIT);
        scanner.close();
    }

    static Optional<String> readTrimmedLine(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return Optional.empty();
        }
        return Optional.of(scanner.nextLine().trim());
    }

    private static void displayMessage(String message, boolean addNewLine) {
        if (addNewLine) message += "\n";
        System.out.print(message);
    }

    private static void addTaskAndReport(String description, TaskList taskList) {
        List<String> errors = addTask(description, taskList);
        if (!errors.isEmpty()) {
            for (String error : errors) {
                displayMessage(error, true);
            }
            return;
        }
        displayMessage("New task added.", true);
        printAllTasks(taskList.findAll());
    }

    static List<String> addTask(String description, TaskList taskList) {
        List<String> errors = findDescriptionErrors(description);
        if (errors.isEmpty()) {
            taskList.add(description);
        }
        return errors;
    }

    static List<String> findDescriptionErrors(String description) {
        List<String> errors = new ArrayList<>();
        if (Task.isDescriptionBlank(description)) {
            errors.add("Description cannot be empty!");
        }
        if (Task.isDescriptionTooShort(description)) {
            errors.add("Description cannot be shorter than "
                    + Task.MIN_DESCRIPTION_LENGTH
                    + " characters!");
        }
        if (Task.isDescriptionTooLong(description)) {
            errors.add("Description cannot be longer than "
                    + Task.MAX_DESCRIPTION_LENGTH
                    + " characters!");
        }
        return errors;
    }

    private static void printAllTasks(List<Task> tasks) {
        for (Task task : tasks) {
            System.out.println(formatTask(task));
        }
    }

    private static String formatTask(Task task) {
        return String.format(TASK_ROW_FORMAT, task.getId(), task.getDescription(), formatStatus(task));
    }

    private static String formatStatus(Task task) {
        return task.isDone() ? "[x]" : "[]";
    }
}
