package museum.endpoints;

import museum.core.Incident;
import java.util.ArrayList;
import java.util.List;

public class AlarmPanel implements AlertEndpoint {
    private int panelId;
    private String location;
    private String zoneId;
    private boolean isSilenced;
    private List<Incident> activeAlerts;

    public AlarmPanel(int panelId, String location, String zoneId) {
        this.panelId = panelId;
        this.location = location;
        this.zoneId = zoneId;
        this.isSilenced = false;
        this.activeAlerts = new ArrayList<>();
    }

    @Override
    public void receiveAlert(Incident incident) {
        activeAlerts.add(incident);
        System.out.println("  [AlarmPanel #" + panelId + " @ " + location + "] "
                + (isSilenced ? "(SILENCED) " : "*** ALARM *** ")
                + "Alert received: " + incident.getSummary());
    }

    @Override
    public void displayStatus() {
        System.out.println("  [AlarmPanel #" + panelId + "] Zone: " + zoneId
                + " | Active alerts: " + activeAlerts.size()
                + " | Silenced: " + isSilenced);
    }

    public void silenceTone() {
        this.isSilenced = true;
        System.out.println("  [AlarmPanel #" + panelId + "] Alarm tone silenced.");
    }

    public int getPanelId() { return panelId; }
    public String getLocation() { return location; }
}
