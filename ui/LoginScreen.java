package ui;

import models.Employee;
import services.EmployeeManager;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    public LoginScreen(EmployeeManager employeeManager) {
        super("Employee Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Enter your name:");
        JTextField nameField = new JTextField(20);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> {
            String name = nameField.getText();
            if (!name.isEmpty()) {
                Employee employee = new Employee(String.valueOf(System.currentTimeMillis()), name);
                employeeManager.setCurrentEmployee(employee);
                new TaskScreen(employeeManager).setVisible(true); // Open TaskScreen
                dispose(); // Close LoginScreen
            } else {
                JOptionPane.showMessageDialog(this, "Please enter your name.");
            }
        });

        add(nameLabel);
        add(nameField);
        add(loginButton);
    }
}
