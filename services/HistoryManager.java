package services;

import models.WorkSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryManager {
    private final Map<LocalDate, List<SessionDetails>> history = new HashMap<>();

    public void addSession(WorkSession session, int mouseClicks, int keyPresses, int screenshots, Map<String, Boolean> tasks) {
        LocalDate sessionDate = session.getStartTime().toLocalDate();
        SessionDetails sessionDetails = new SessionDetails(
                session.getStartTime(),
                session.getEndTime(),
                mouseClicks,
                keyPresses,
                screenshots,
                tasks
        );

        history.putIfAbsent(sessionDate, new ArrayList<>());
        history.get(sessionDate).add(sessionDetails);
    }

    public Map<LocalDate, List<SessionDetails>> getHistory() {
        return history;
    }
}
