package museum.coordination;

import museum.core.Exhibit;
import museum.core.Incident;
import museum.devices.MonitoringDevice;
import museum.endpoints.AlertEndpoint;
import java.util.ArrayList;
import java.util.List;

public class SecurityCoordinationHub {
    private List<AlertEndpoint> registeredEndpoints;
    private List<Incident> activeIncidents;
    private List<Exhibit> knownExhibits;

    public SecurityCoordinationHub() {
        this.registeredEndpoints = new ArrayList<>();
        this.activeIncidents = new ArrayList<>();
        this.knownExhibits = new ArrayList<>();
    }

    public void registerEndpoint(AlertEndpoint endpoint) {
        registeredEndpoints.add(endpoint);
        System.out.println("  [Hub] Endpoint registered: " + endpoint.getClass().getSimpleName());
    }

    public void registerExhibit(Exhibit exhibit) {
        knownExhibits.add(exhibit);
        System.out.println("  [Hub] Exhibit registered: " + exhibit.getExhibitName());
    }

    public void processDeviceReport(MonitoringDevice device) {
        String condition = device.detectCondition();
        device.reportStatusChange();

        if (condition.equals("WARNING") || condition.equals("CRITICAL")) {
            Exhibit exhibit = device.getAttachedExhibit();
            if (exhibit != null) {
                Incident incident = exhibit.createIncidentRecord(condition,
                        device.getDeviceType() + " device '" + device.getDeviceName()
                                + "' detected " + condition + " condition.");
                incident.addDeviceDetail(device.toString());
                activeIncidents.add(incident);

                // Update exhibit protection status
                updateExhibitState(exhibit, condition.equals("CRITICAL"));

                // Notify appropriate endpoints based on severity
                notifyRelevantEndpoints(incident);
            }
        }
    }

    public void notifyRelevantEndpoints(Incident incident) {
        System.out.println("  [Hub] Notifying endpoints for " + incident.getSeverity() + " incident...");
        for (AlertEndpoint endpoint : registeredEndpoints) {
            // Critical incidents go to all endpoints; warnings only go to non-alarm endpoints
            if (incident.getSeverity().equals("CRITICAL")) {
                endpoint.receiveAlert(incident);
            } else if (incident.getSeverity().equals("WARNING")) {
                // Warnings skip AlarmPanel (don't trigger audible alarm for minor issues)
                if (!(endpoint instanceof museum.endpoints.AlarmPanel)) {
                    endpoint.receiveAlert(incident);
                }
            }
        }
    }

    public void updateExhibitState(Exhibit exhibit, boolean compromised) {
        exhibit.updateProtectionStatus(!compromised);
        System.out.println("  [Hub] Exhibit '" + exhibit.getExhibitName()
                + "' state updated: " + (compromised ? "COMPROMISED" : "PROTECTED"));
    }

    public List<Incident> getIncidentHistory() {
        return new ArrayList<>(activeIncidents);
    }

    public void printIncidentHistory() {
        System.out.println("\n  === Incident History ===");
        if (activeIncidents.isEmpty()) {
            System.out.println("  No incidents recorded.");
        } else {
            for (Incident i : activeIncidents) {
                System.out.println("  " + i.getSummary());
            }
        }
        System.out.println("  ========================");
    }

    public List<AlertEndpoint> getRegisteredEndpoints() { return registeredEndpoints; }
    public int getActiveIncidentCount() { return activeIncidents.size(); }
}
