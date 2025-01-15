package models;

import java.time.LocalDateTime;

public class Employee {
    private String id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int screenshotInterval = 10; // default screenshot interval in seconds
    private String sessionFolderPath;

    public Employee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getScreenshotInterval() {
        return screenshotInterval;
    }

    public void setScreenshotInterval(int screenshotInterval) {
        this.screenshotInterval = screenshotInterval;
    }

    public String getSessionFolderPath() {
        return sessionFolderPath;
    }

    public void setSessionFolderPath(String sessionFolderPath) {
        this.sessionFolderPath = sessionFolderPath;
    }
}
