package museum.endpoints;

import museum.core.Incident;
import java.util.ArrayList;
import java.util.List;

public class GuardUnit implements AlertEndpoint {
    private int unitId;
    private String guardName;
    private String currentLocation;
    private boolean isAvailable;
    private List<Incident> assignedIncidents;

    public GuardUnit(int unitId, String guardName, String currentLocation) {
        this.unitId = unitId;
        this.guardName = guardName;
        this.currentLocation = currentLocation;
        this.isAvailable = true;
        this.assignedIncidents = new ArrayList<>();
    }

    @Override
    public void receiveAlert(Incident incident) {
        assignedIncidents.add(incident);
        isAvailable = false;
        System.out.println("  [GuardUnit #" + unitId + " - " + guardName + "] "
                + "Dispatching! Alert: " + incident.getSummary());
    }

    @Override
    public void displayStatus() {
        System.out.println("  [GuardUnit #" + unitId + "] Guard: " + guardName
                + " | Location: " + currentLocation
                + " | Available: " + isAvailable
                + " | Assigned incidents: " + assignedIncidents.size());
    }

    public void confirmAttendance(int incidentId) {
        for (Incident i : assignedIncidents) {
            if (i.getIncidentId() == incidentId) {
                i.markAcknowledged(guardName);
                System.out.println("  [GuardUnit #" + unitId + " - " + guardName
                        + "] Confirmed attendance at incident #" + incidentId);
                return;
            }
        }
        System.out.println("  [GuardUnit #" + unitId + "] Incident #" + incidentId + " not assigned to this unit.");
    }

    public void returnToPatrol() {
        this.isAvailable = true;
        System.out.println("  [GuardUnit #" + unitId + " - " + guardName + "] Returned to patrol.");
    }

    public int getUnitId() { return unitId; }
    public String getGuardName() { return guardName; }
    public boolean isAvailable() { return isAvailable; }
}
