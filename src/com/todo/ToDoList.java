package com.todo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class ToDoList {
    private List<Task> tasks;

    // Constructor to initialize task list
    public ToDoList() {
        tasks = new ArrayList<>();
    }

    // Function to add a task with category
    public void addTask(String description, String category) {
        LocalDate dueDate = LocalDate.now().plusDays(7); // Default due date = 7 days later
        LocalTime dueTime = LocalTime.of(23, 59); // Default time = 11:59 PM
        boolean isCompleted = false; // Default to incomplete

        // Create and add task with category
        tasks.add(new Task(description, dueDate, dueTime, isCompleted, category));

        System.out.println("Task added successfully!");
    }

    // Overloaded method: If category is not provided, use "General"
    public void addTask(String description) {
        addTask(description, "General"); // Calls the main addTask method with a default category
    }

    // Method to mark a task as completed
    public void completeTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsCompleted();
            System.out.println("Task marked as completed");
        } else {
            System.out.println("Invalid task number.");
        }
    }

    // Method to display all tasks
    public void displayTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available!");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println((i + 1) + ". " + tasks.get(i).toString());
            }
        }
    }

    // Method to search tasks by keyword
    public void searchTask(String keyword) {
        System.out.println("\n🔍 Search Results for '" + keyword + "':");

        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println((i + 1) + ". " + tasks.get(i));
                found = true;
            }
        }

        if (!found) {
            System.out.println("❌ No tasks found!");
        }
    }

    // Method to filter tasks based on completion status
    public void filterTasks(boolean showCompleted) {
        System.out.println("\n📋 " + (showCompleted ? "Completed" : "Pending") + " Tasks:");

        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isCompleted() == showCompleted) {
                System.out.println((i + 1) + ". " + tasks.get(i));
                found = true;
            }
        }

        if (!found) {
            System.out.println("❌ No " + (showCompleted ? "completed" : "pending") + " tasks found!");
        }
    }
}
