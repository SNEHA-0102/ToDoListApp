package com.todo;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private static boolean darkMode = false; // Default is light mode

    // Apply theme to a container (entire app window)
    public static void applyTheme(JFrame frame) {
        Container contentPane = frame.getContentPane();
        if (darkMode) {
            contentPane.setBackground(Color.DARK_GRAY);
        } else {
            contentPane.setBackground(Color.WHITE);
        }
        updateComponents(contentPane);
        frame.repaint();
        frame.revalidate();
    }

    // Toggle theme and refresh the frame
    public static void toggleTheme(JFrame frame) {
        darkMode = !darkMode;
        applyTheme(frame);  // Ensure the theme updates properly
    }


    // Update all components recursively
    private static void updateComponents(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton || component instanceof JLabel || component instanceof JTextField) {
                component.setForeground(darkMode ? Color.WHITE : Color.BLACK);
                component.setBackground(darkMode ? Color.GRAY : Color.LIGHT_GRAY);
            }
            if (component instanceof JPanel) {
                updateComponents((Container) component);
            }
        }
    }
}
