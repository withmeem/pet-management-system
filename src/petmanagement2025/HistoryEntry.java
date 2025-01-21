package petmanagement2025;

public class HistoryEntry {
    private String timestamp;
    private String action;

    public HistoryEntry(String timestamp, String action) {
        this.timestamp = timestamp;
        this.action = action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getAction() {
        return action;
    }
}