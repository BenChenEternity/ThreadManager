package ajie.wiki;

import java.util.HashMap;

public class CustomEvent {
    public enum EventType {
        ON_BAR_MOUSE_ENTER,
        ON_BAR_MOUSE_EXIT,
        THREAD_PROGRESS,
        THREAD_BLOCK,
        THREAD_READY,
        THREAD_RUNNING,
        THREAD_TERMINATED,
        CALCULATE_TOTAL_CONSUME, REFRESH_TABLE,
        THREAD_SPINNING, THREAD_ADD, THREAD_REMOVE, SCHEDULER_DONE, CLOCK_START, IDLE_STATE, THREAD_MODIFY,
    }

    public CustomEvent(EventType type, HashMap<String, Object> params) {
        this.eventType = type;
        this.params = params;
    }

    public EventType eventType;
    public HashMap<String, Object> params;
}
