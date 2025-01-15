package models;

import java.time.LocalDateTime;

public class WorkSession {
    private final String employeeId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public WorkSession(String employeeId, LocalDateTime startTime, LocalDateTime endTime) {
        this.employeeId = employeeId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
