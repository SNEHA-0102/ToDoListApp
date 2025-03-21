package com.todo;
import java.util.Scanner;
public class ToDoListApp{
    public static void main(String[] args) {
        ToDoList toDoList = new ToDoList();
        Scanner scanner= new Scanner(System.in);
        while(true){
            System.out.println("/n=== To-Do List Menu ===");
            System.out.println("1. Add Task");
            System.out.println("2. Complete Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
             int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice){
            case 1:
                System.out.println("Enter the task description: ");
                String description = scanner.nextLine();
                toDoList.addTask(description);
                break;
            case 2:
                System.out.println("Enter the task number to mark as completed: ");
                int taskNumber = scanner.nextInt();
                toDoList.completeTask(taskNumber-1);
                break;
            case 3:
                System.out.println("\nYour To-Do List: ");
                toDoList.displayTasks();
                break;
            case 4:
                System.out.println("Exiting... Have a great day!");
                scanner.close();
                return;
            default:
                System.out.println("Invalid choice. Try again.");
        }
        }
    }
}