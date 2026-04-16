package museum.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Incident {
    private static int nextId = 1;

    private int incidentId;
    private LocalDateTime timestamp;
    private String severity; // "LOW", "WARNING", "CRITICAL"
    private String description;
    private String status;   // "OPEN", "ACKNOWLEDGED", "CLOSED"
    private List<String> deviceDetails;
    private String acknowledgedBy;
    private LocalDateTime closedAt;
    private Exhibit relatedExhibit;

    public Incident(String severity, String description, Exhibit relatedExhibit) {
        this.incidentId = nextId++;
        this.timestamp = LocalDateTime.now();
        this.severity = severity;
        this.description = description;
        this.status = "OPEN";
        this.deviceDetails = new ArrayList<>();
        this.relatedExhibit = relatedExhibit;
    }

    public void markAcknowledged(String staffId) {
        this.status = "ACKNOWLEDGED";
        this.acknowledgedBy = staffId;
        System.out.println("  [Incident #" + incidentId + "] Acknowledged by: " + staffId);
    }

    public void escalate() {
        if (severity.equals("LOW")) severity = "WARNING";
        else if (severity.equals("WARNING")) severity = "CRITICAL";
        System.out.println("  [Incident #" + incidentId + "] ESCALATED to " + severity);
    }

    public void closeIncident(String notes) {
        this.status = "CLOSED";
        this.closedAt = LocalDateTime.now();
        System.out.println("  [Incident #" + incidentId + "] Closed. Notes: " + notes);
    }

    public String getSummary() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("Incident #%d | %s | %s | %s | Exhibit: %s | %s",
                incidentId, severity, status,
                timestamp.format(fmt),
                relatedExhibit != null ? relatedExhibit.getExhibitName() : "N/A",
                description);
    }

    public void addDeviceDetail(String detail) { deviceDetails.add(detail); }

    // Getters
    public int getIncidentId() { return incidentId; }
    public String getSeverity() { return severity; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Exhibit getRelatedExhibit() { return relatedExhibit; }
}
