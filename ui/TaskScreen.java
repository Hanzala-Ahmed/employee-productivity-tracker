package ui;

import models.Employee;
import models.WorkSession;
import services.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskScreen extends JFrame {
    private Timer updateTimer;
    private Timer screenshotTimer;
    private final JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
    private final DefaultListModel<String> taskListModel = new DefaultListModel<>();
    private DefaultListModel<String> taskHistoryListModel = new DefaultListModel<>();
    private final DefaultListModel<String> historyListModel = new DefaultListModel<>();
    private final Map<String, Boolean> tasks = new HashMap<>();
    private final InputTracker inputTracker = new InputTracker();
    private int mouseClicks = 0;
    private int keyPresses = 0;
    private int screenshotCount = 0;

    public TaskScreen(EmployeeManager employeeManager) {
        super("Employee Tasks");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel taskPanel = new JPanel(new BorderLayout());
        JPanel historyPanel = new JPanel(new BorderLayout());

        JButton startButton = new JButton("Start Day");
        JButton endButton = new JButton("End Day");
        JButton logoutButton = new JButton("Logout");
        JButton screenshotGalleryButton = new JButton("View Screenshots");
        JTextField screenshotIntervalField = new JTextField(5);
        JButton setScreenshotIntervalButton = new JButton("Set Interval");

        Employee employee = employeeManager.getCurrentEmployee();
        JLabel nameLabel = new JLabel("<html><h2>Welcome, " + employee.getName() + "</h2></html>", SwingConstants.CENTER);

        ScreenCaptureService screenCaptureService = new ScreenCaptureService();
        HistoryManager historyManager = new HistoryManager();

        createEmployeeFolder(employee);

        startButton.addActionListener(e -> {
    if (employee.getStartTime() == null || employee.getEndTime() != null) {
        employee.setStartTime(LocalDateTime.now());
          employee.setEndTime(null);

            screenCaptureService.initializeSessionFolder(employee.getName());

        String employeeFolderPath = "screenshots/" + employee.getName();
        String dateFolderPath = employeeFolderPath + "/" + LocalDate.now();
        String sessionFolderPath = dateFolderPath + "/Session-" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss"));
        File sessionFolder = new File(sessionFolderPath);
        if (!sessionFolder.exists()) {
            sessionFolder.mkdirs();
        }

        employee.setSessionFolderPath(sessionFolderPath);
        startScreenshotTimer(employee, screenCaptureService);
        startUpdateTimer(employee);

        mouseClicks = 0;
        keyPresses = 0;
        screenshotCount = 0;
        timerLabel.setText("00:00:00");
        timerLabel.setVisible(true);
        startButton.setEnabled(false);
        endButton.setEnabled(true);
    }
});

      endButton.addActionListener(e -> {
          if (employee.getEndTime() == null) {
              employee.setEndTime(LocalDateTime.now());
              historyManager.addSession(
                      new WorkSession(employee.getId(), employee.getStartTime(), employee.getEndTime()),
                      inputTracker.getMouseClicks(),
                      inputTracker.getKeyPresses(),
                      screenshotCount,
                      new HashMap<>(tasks)
              );
              stopScreenshotTimer();
              stopUpdateTimer();
              updateHistoryList(historyManager);
              updateHistoryPanel(historyPanel, historyManager);
              resetForNewDay();
              timerLabel.setVisible(false);
              startButton.setEnabled(true);
              endButton.setEnabled(false);
          }
      });

      logoutButton.addActionListener(e -> {
        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );
        if (confirmation == JOptionPane.YES_OPTION) {
            dispose();
            new LoginScreen(employeeManager).setVisible(true);
        }
    });
        setScreenshotIntervalButton.addActionListener(e -> {
            try {
                int interval = Integer.parseInt(screenshotIntervalField.getText());
                employee.setScreenshotInterval(interval);
                JOptionPane.showMessageDialog(this, "Screenshot interval updated to " + interval + " seconds.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for the interval.");
            }
        });

        screenshotGalleryButton.addActionListener(e -> new ScreenshotGallery().setVisible(true));

        topPanel.add(nameLabel, BorderLayout.NORTH);
        topPanel.add(timerLabel, BorderLayout.CENTER);
        topPanel.add(logoutButton, BorderLayout.EAST);
        timerLabel.setVisible(false);

        buttonPanel.add(startButton);
        buttonPanel.add(endButton);
        buttonPanel.add(screenshotGalleryButton);
        buttonPanel.add(new JLabel("Screenshot Interval (s):"));
        buttonPanel.add(screenshotIntervalField);
        buttonPanel.add(setScreenshotIntervalButton);

        taskPanel.add(createTaskManagerPanel(), BorderLayout.CENTER);

        historyPanel.add(new JLabel("<html><h3>Session History</h3></html>"), BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(createHistoryPanel(historyManager)), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(taskPanel, BorderLayout.WEST);
        add(historyPanel, BorderLayout.EAST);
    }

    private void createEmployeeFolder(Employee employee) {
      File employeeFolder = new File("screenshots/" + employee.getName());
      if (!employeeFolder.exists()) {
        employeeFolder.mkdirs();
      }
    }

    private void updateHistoryPanel(JPanel historyPanel, HistoryManager historyManager) {
      historyPanel.removeAll();
      historyPanel.add(new JLabel("<html><h3>Session History</h3></html>"), BorderLayout.NORTH);
      historyPanel.add(new JScrollPane(createHistoryPanel(historyManager)), BorderLayout.CENTER);
      historyPanel.revalidate();
      historyPanel.repaint();
  }


  private JPanel createTaskManagerPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JPanel taskInputPanel = new JPanel(new FlowLayout());
    JTextField taskField = new JTextField(15);
    JButton addTaskButton = new JButton("Add Task");

    JList<String> taskList = new JList<>(taskListModel);
    JList<String> taskHistoryList = new JList<>(taskHistoryListModel); // List for completed tasks

    taskInputPanel.add(taskField);
    taskInputPanel.add(addTaskButton);

    // Add Task Logic
    addTaskButton.addActionListener(e -> {
        String task = taskField.getText();
        if (!task.isEmpty()) {
            tasks.put(task, false);
            taskListModel.addElement(task + " (Not Completed)");
            taskField.setText("");
            updateTaskList();
        }
    });

    // Create Task List Panel
    JPanel taskListPanel = new JPanel(new GridLayout(0, 1));
    for (Map.Entry<String, Boolean> entry : tasks.entrySet()) {
        String task = entry.getKey();
        boolean isCompleted = entry.getValue();
        JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel taskLabel = new JLabel(task + (isCompleted ? " (Completed)" : " (Not Completed)"));
        JButton completeButton = new JButton("Complete");
        JButton deleteButton = new JButton("Delete");

        // Complete Button Logic
        completeButton.addActionListener(e -> {
            tasks.put(task, true);
            taskListModel.removeElement(task + " (Not Completed)");
            taskListModel.addElement(task + " (Completed)");
            taskHistoryListModel.addElement(task + " (Completed)"); // Move to history
            updateTaskList();
        });

        // Delete Button Logic
        deleteButton.addActionListener(e -> {
            tasks.remove(task);
            taskListModel.removeElement(task + " (Not Completed)");
            taskListModel.removeElement(task + " (Completed)");
            updateTaskList();
        });

        taskPanel.add(taskLabel);
        taskPanel.add(completeButton);
        taskPanel.add(deleteButton);
        taskListPanel.add(taskPanel);
    }

    // Layout Setup
    panel.add(taskInputPanel, BorderLayout.NORTH);
    panel.add(new JScrollPane(taskList), BorderLayout.CENTER);
    panel.add(new JLabel("<html><h3>Task History:</h3></html>"), BorderLayout.SOUTH);
    panel.add(new JScrollPane(taskHistoryList), BorderLayout.SOUTH);

    return panel;
}

private void updateTaskList() {
  taskListModel.clear();
  for (Map.Entry<String, Boolean> entry : tasks.entrySet()) {
      String task = entry.getKey();
      boolean isCompleted = entry.getValue();
      taskListModel.addElement(task + (isCompleted ? " (Completed)" : " (Not Completed)"));
  }
}

  private JPanel createHistoryPanel(HistoryManager historyManager) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);

    Map<LocalDate, List<SessionDetails>> history = historyManager.getHistory();

    for (LocalDate date : history.keySet()) {
        // Add Date Header
        JLabel dateLabel = new JLabel("<html><h2 style='color: blue;'>Date: " + date + "</h2></html>");
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(dateLabel);

        for (SessionDetails session : history.get(date)) {
            JPanel sessionPanel = new JPanel();
            sessionPanel.setLayout(new BoxLayout(sessionPanel, BoxLayout.Y_AXIS));
            sessionPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            sessionPanel.setBackground(new Color(240, 240, 240)); // Light gray background

            // Add Session Details Header
            JLabel sessionHeader = new JLabel("<html><b>Session Details:</b></html>");
            sessionHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            sessionPanel.add(sessionHeader);

            // Add Session Metrics
            sessionPanel.add(new JLabel("   Start Time: " + session.getStartTime()));
            sessionPanel.add(new JLabel("   End Time: " + session.getEndTime()));
            sessionPanel.add(new JLabel("   Mouse Clicks: " + session.getMouseClicks()));
            sessionPanel.add(new JLabel("   Key Presses: " + session.getKeyPresses()));
            sessionPanel.add(new JLabel("   Screenshots Taken: " + session.getScreenshots()));

            // Add Task Details Header
            JLabel taskHeader = new JLabel("<html><b>Tasks:</b></html>");
            taskHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            sessionPanel.add(taskHeader);

            // Add Task Details
            StringBuilder tasksInfo = new StringBuilder("   ");
            for (Map.Entry<String, Boolean> task : session.getTasks().entrySet()) {
                tasksInfo.append(task.getKey())
                        .append(task.getValue() ? " (Completed)" : " (Not Completed)")
                        .append(", ");
            }
            sessionPanel.add(new JLabel(tasksInfo.toString()));

            // Add Spacing Below Each Session
            sessionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(sessionPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between sessions
        }
    }

    return panel;
}

    private void updateHistoryList(HistoryManager historyManager) {
        historyListModel.clear();
        for (LocalDate date : historyManager.getHistory().keySet()) {
            historyListModel.addElement("Date: " + date);
            for (SessionDetails session : historyManager.getHistory().get(date)) {
                historyListModel.addElement(
                        String.format("  Session: %s - %s | Mouse: %d | Keys: %d | Screenshots: %d",
                                session.getStartTime(),
                                session.getEndTime(),
                                session.getMouseClicks(),
                                session.getKeyPresses(),
                                session.getScreenshots()
                        )
                );
            }
        }
    }

    private void startScreenshotTimer(Employee employee, ScreenCaptureService screenCaptureService) {
      screenshotTimer = new Timer(employee.getScreenshotInterval() * 1000, e -> {
          String path = screenCaptureService.captureScreen();
          if (path != null) {
              screenshotCount++;
          }
      });
      screenshotTimer.start();
  }



    private void stopScreenshotTimer() {
        if (screenshotTimer != null) {
            screenshotTimer.stop();
        }
    }

    private void startUpdateTimer(Employee employee) {
      updateTimer = new Timer(1000, e -> {
          Duration duration = Duration.between(employee.getStartTime(), LocalDateTime.now());
          long hours = duration.toHours();
          long minutes = duration.toMinutes() % 60;
          long seconds = duration.getSeconds() % 60;

          mouseClicks = inputTracker.getMouseClicks();
          keyPresses = inputTracker.getKeyPresses();

          timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
      });
      updateTimer.start();
  }

    private void stopUpdateTimer() {
        if (updateTimer != null) {
            updateTimer.stop();
        }
    }

    private void resetForNewDay() {
      tasks.clear();
      taskListModel.clear();
      inputTracker.reset();
      mouseClicks = 0;
      keyPresses = 0;
  }

}
