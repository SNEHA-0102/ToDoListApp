package com.todo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
    private String description;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private boolean completed;
    private String category;

    public Task(String description, LocalDate dueDate, LocalTime dueTime, boolean completed, String category) {
        this.description = description;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.completed = completed;
        this.category = category;
    }

    // Getters
    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    // Setters
    public void setCategory(String category) {
        this.category = category;
    }

    public void markAsCompleted() {
        this.completed = true;
    }

    // Override toString for better representation
    @Override
    public String toString() {
        return "[ " + (completed ? "✔" : "❌") + " ] " + description + " (Category: " + category + ", Due: " + dueDate + " " + dueTime + ")";
    }
}
