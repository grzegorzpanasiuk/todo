import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String name;
        int age;

        Scanner s = new Scanner(System.in);

        System.out.print("Enter your name: ");
        name = s.next();

        System.out.print("Enter your age: ");
        age = s.nextInt();

        System.out.println("Hello " + name + "! " + "You're " + age + " years old.");
    }
}