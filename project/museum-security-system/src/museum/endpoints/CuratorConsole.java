package museum.endpoints;

import museum.core.Exhibit;
import museum.core.Incident;
import java.util.ArrayList;
import java.util.List;

public class CuratorConsole implements AlertEndpoint {
    private int consoleId;
    private String assignedCurator;
    private List<Exhibit> connectedExhibits;
    private List<Incident> activeAlerts;

    public CuratorConsole(int consoleId, String assignedCurator) {
        this.consoleId = consoleId;
        this.assignedCurator = assignedCurator;
        this.connectedExhibits = new ArrayList<>();
        this.activeAlerts = new ArrayList<>();
    }

    @Override
    public void receiveAlert(Incident incident) {
        activeAlerts.add(incident);
        System.out.println("  [CuratorConsole #" + consoleId + " - " + assignedCurator + "] "
                + "Alert received: " + incident.getSummary());
    }

    @Override
    public void displayStatus() {
        System.out.println("  [CuratorConsole #" + consoleId + "] Curator: " + assignedCurator
                + " | Connected exhibits: " + connectedExhibits.size()
                + " | Active alerts: " + activeAlerts.size());
    }

    public String requestExhibitSummary(int exhibitId) {
        for (Exhibit e : connectedExhibits) {
            if (e.getExhibitId() == exhibitId) {
                return "  [CuratorConsole] Summary for " + e.toString();
            }
        }
        return "  [CuratorConsole] Exhibit #" + exhibitId + " not found.";
    }

    public void connectExhibit(Exhibit exhibit) {
        connectedExhibits.add(exhibit);
    }

    public int getConsoleId() { return consoleId; }
    public String getAssignedCurator() { return assignedCurator; }
}
