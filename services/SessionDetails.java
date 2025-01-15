package services;

import java.time.LocalDateTime;
import java.util.Map;

public class SessionDetails {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final int mouseClicks;
    private final int keyPresses;
    private final int screenshots;
    private final Map<String, Boolean> tasks;

    public SessionDetails(LocalDateTime startTime, LocalDateTime endTime, int mouseClicks, int keyPresses, int screenshots, Map<String, Boolean> tasks) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.mouseClicks = mouseClicks;
        this.keyPresses = keyPresses;
        this.screenshots = screenshots;
        this.tasks = tasks;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public int getMouseClicks() {
        return mouseClicks;
    }

    public int getKeyPresses() {
        return keyPresses;
    }

    public int getScreenshots() {
        return screenshots;
    }

    public Map<String, Boolean> getTasks() {
        return tasks;
    }
}
