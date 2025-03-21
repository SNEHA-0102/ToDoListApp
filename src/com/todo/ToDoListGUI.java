package com.todo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ToDoListGUI extends JFrame {
    private final DefaultListModel<String> taskListModel;
    private final JList<String> taskList;
    private final JTextField taskInputField, searchField;
    private final List<Task> tasks;
    private final JComboBox<String> categoryFilter, statusFilter;
    private final JButton toggleThemeButton;
    private boolean isDarkMode = false;





    public ToDoListGUI() {
        setTitle("To-Do List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskInputField = new JTextField(25);
        searchField = new JTextField(20);
        toggleThemeButton = new JButton("Toggle Theme");
        toggleThemeButton.addActionListener(e -> toggleTheme());
// Enable Drag-and-Drop
        taskList.setDragEnabled(true);
        taskList.setDropMode(DropMode.INSERT);
        taskList.setTransferHandler(new TransferHandler("selectedValue"));


        JButton addButton = new JButton("Add Task");
        JButton completeButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete Task");
        JButton searchButton = new JButton("🔍");

        categoryFilter = new JComboBox<>(new String[]{"All", "Work", "Personal", "Urgent"});
        statusFilter = new JComboBox<>(new String[]{"All", "Completed", "Pending"});

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Task: "));
        topPanel.add(taskInputField);
        topPanel.add(addButton);
        topPanel.add(new JLabel("Search: "));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.add(completeButton);
        sidebarPanel.add(deleteButton);
        sidebarPanel.add(new JLabel("Filter by Category:"));
        sidebarPanel.add(categoryFilter);
        sidebarPanel.add(new JLabel("Filter by Status:"));
        sidebarPanel.add(statusFilter);
        sidebarPanel.add(toggleThemeButton);

        add(topPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        loadTasksFromFile();

        addButton.addActionListener(e -> addTask());
        completeButton.addActionListener(e -> markTaskCompleted());
        deleteButton.addActionListener(e -> deleteTask());
        searchButton.addActionListener(e -> updateTaskList());
        categoryFilter.addActionListener(e -> updateTaskList());
        statusFilter.addActionListener(e -> updateTaskList());

        applyTheme(); // Apply initial theme
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        applyTheme();
    }

    private void applyTheme() {
        Color bgColor = isDarkMode ? Color.DARK_GRAY : Color.WHITE;
        Color fgColor = isDarkMode ? Color.WHITE : Color.BLACK;
        Color btnBg = isDarkMode ? Color.BLACK : Color.LIGHT_GRAY;
        Color btnFg = isDarkMode ? Color.WHITE : Color.BLACK;

        getContentPane().setBackground(bgColor);
        taskList.setBackground(bgColor);
        taskList.setForeground(fgColor);
        taskInputField.setBackground(bgColor);
        taskInputField.setForeground(fgColor);
        searchField.setBackground(bgColor);
        searchField.setForeground(fgColor);
        toggleThemeButton.setBackground(btnBg);
        toggleThemeButton.setForeground(btnFg);

        repaint();
        revalidate();
    }

    private void addTask() {
        String taskText = taskInputField.getText().trim();
        if (taskText.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Task description cannot be empty!");
            return;
        }

        String[] categories = {"Work", "Personal", "Urgent"};
        String category = (String) JOptionPane.showInputDialog(
                null, "Select task category:", "Task Category",
                JOptionPane.QUESTION_MESSAGE, null, categories, categories[0]);
        if (category == null) return;

        LocalDate dueDate = LocalDate.now().plusDays(7);
        LocalTime dueTime = LocalTime.of(23, 59);
        tasks.add(new Task(taskText, dueDate, dueTime, false, category));
        updateTaskList();
        taskInputField.setText("");
        saveTasksToFile();
    }

    private void markTaskCompleted() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            tasks.get(index).markAsCompleted();
            updateTaskList();
            saveTasksToFile();
        } else {
            JOptionPane.showMessageDialog(null, "Select a task to mark as completed.");
        }
    }

    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            tasks.remove(index);
            updateTaskList();
            saveTasksToFile();
        } else {
            JOptionPane.showMessageDialog(null, "Select a task to delete.");
        }
    }

    private void updateTaskList() {
        taskListModel.clear();
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        String searchKeyword = searchField.getText().trim().toLowerCase();
        String selectedStatus = (String) statusFilter.getSelectedItem();

        for (Task task : tasks) {
            boolean matchesCategory = selectedCategory.equals("All") || task.getCategory().equals(selectedCategory);
            boolean matchesSearch = searchKeyword.isEmpty() || task.getDescription().toLowerCase().contains(searchKeyword);
            boolean matchesStatus = selectedStatus.equals("All") ||
                    (selectedStatus.equals("Completed") && task.isCompleted()) ||
                    (selectedStatus.equals("Pending") && !task.isCompleted());

            if (matchesCategory && matchesSearch && matchesStatus) {
                taskListModel.addElement((task.isCompleted() ? "[✔] " : "") + task.getCategory() + " - " +
                        task.getDescription() + " (Due: " + task.getDueDate() + " " + task.getDueTime() + ")");
            }
        }
    }

    private void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("tasks.txt", false))) {
            for (Task task : tasks) {
                writer.println(task.isCompleted() + "##" + task.getDescription() + "##" +
                        task.getDueDate() + "##" + task.getDueTime() + "##" + task.getCategory());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile() {
        File file = new File("tasks.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] taskData = line.split("##");
                if (taskData.length < 5) continue;

                boolean isCompleted = Boolean.parseBoolean(taskData[0].trim());
                String description = taskData[1].trim();
                LocalDate dueDate = LocalDate.parse(taskData[2].trim());
                LocalTime dueTime = LocalTime.parse(taskData[3].trim());
                String category = taskData[4].trim();

                tasks.add(new Task(description, dueDate, dueTime, isCompleted, category));
            }
            updateTaskList();
        } catch (IOException | DateTimeParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListGUI().setVisible(true));
    }
}
