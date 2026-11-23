package com.grzegorzpanasiuk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final int DESCRIPTION_COLUMN_WIDTH = Task.MAX_DESCRIPTION_LENGTH;
    private static final String TASK_ROW_FORMAT =
            "Task: %03d"
                    + " | Description: %-" + DESCRIPTION_COLUMN_WIDTH + "." + DESCRIPTION_COLUMN_WIDTH + "s"
                    + " | Status: %-3.3s";

    private static void displayMessage(String message, boolean addNewLine) {
        if (addNewLine) message += "\n";
        System.out.print(message);
    }

    private static Task findGivenTask(List<Task> tasks, int taskNumber) {
        Task taskFound = null;
        for (Task task : tasks) {
            if (task.getId() == taskNumber) {
                taskFound = task;
                break;
            }
        }
        return taskFound;
    }

    private static String formatStatus(Task task) {
        return task.isDone() ? "[x]" : "[]";
    }

    private static String formatTask(Task task) {
        return String.format(TASK_ROW_FORMAT, task.getId(), task.getDescription(), formatStatus(task));
    }

    private static void printAllTasks(List<Task> tasks) {
        for (Task task : tasks) {
            System.out.println(formatTask(task));
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        TaskList taskList = TaskList.empty();
        Command command = null;
        String userText;
        int taskNumber;
        boolean found;

        do {
            System.out.println("Enter the command (type HELP for a list of allowed commands)");
            // account for CMD+D combination
            if (!scanner.hasNextLine()) {
                displayMessage("Goodbye!", true);
                System.exit(0);
            }
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
                    displayMessage("Description (" + Task.MIN_DESCRIPTION_LENGTH
                            + "-" + Task.MAX_DESCRIPTION_LENGTH + " characters): ", false);
                    String description = scanner.nextLine().trim();
                    addTaskAndReport(description, taskList);
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
                    boolean deleted = taskList.deleteById(taskNumber);
                    if (deleted) {
                        displayMessage("Task with number " + taskNumber + " was deleted.", true);
                    } else {
                        displayMessage("Task with number " + taskNumber + " does not exist!", true);
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
                    String taskToMarkAsDone = scanner.nextLine().trim();
                    try {
                        taskNumber = Integer.parseInt(taskToMarkAsDone);
                    } catch (NumberFormatException e) {
                        displayMessage("You didn't enter a number!", true);
                        break;
                    }
                    found = false;
                    Task taskToBeMarked = findGivenTask(taskList.findAll(), taskNumber);
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
}
