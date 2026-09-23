import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);
        String command;
        ArrayList<Task> tasks = new ArrayList<Task>();

        System.out.println();
        do {
            command = s.nextLine();
            switch (command) {
                case "add":
                    System.out.print("Description: ");
                    String desc = s.nextLine();
                    if (desc.isBlank()) {
                        System.out.println("Description cannot be empty");
                        break;
                    }
                    tasks.add(new Task(desc, false));
                    System.out.println("New task added.");
                    break;
                case "list":
                    if (tasks.isEmpty()) {
                        System.out.println("Task list is empty.");
                        break;
                    }
                    for (Task task : tasks) {
                        System.out.println(task.displayTask());
                    }
                    break;
                case "done":
                    System.out.println("Mark given task as done");
                    break;
                case "delete":
                    System.out.println("Delete given task");
                    break;
            }
        } while (!command.equalsIgnoreCase("exit"));
    }
}