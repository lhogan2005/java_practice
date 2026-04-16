package museum.core;

import museum.devices.MonitoringDevice;
import java.util.ArrayList;
import java.util.List;

public abstract class Exhibit {
    private int exhibitId;
    private String exhibitName;
    private boolean isActive;
    private List<MonitoringDevice> devices;
    private boolean protectionStatus;
    private List<Incident> incidents;

    public Exhibit(int exhibitId, String exhibitName) {
        this.exhibitId = exhibitId;
        this.exhibitName = exhibitName;
        this.isActive = true;
        this.protectionStatus = true;
        this.devices = new ArrayList<>();
        this.incidents = new ArrayList<>();
    }

    public abstract void updateProtectionStatus(boolean status);
    public abstract void assignDevice(MonitoringDevice device);

    public Incident createIncidentRecord(String severity, String description) {
        Incident incident = new Incident(severity, description, this);
        incidents.add(incident);
        System.out.println("  [" + exhibitName + "] Incident record created: " + description);
        return incident;
    }

    public boolean getProtectionStatus() { return protectionStatus; }
    protected void setProtectionStatus(boolean status) { this.protectionStatus = status; }

    public List<MonitoringDevice> getDeviceList() { return devices; }
    protected void addDeviceToList(MonitoringDevice device) { devices.add(device); }

    public List<Incident> getIncidents() { return incidents; }

    public int getExhibitId() { return exhibitId; }
    public String getExhibitName() { return exhibitName; }
    public boolean isActive() { return isActive; }

    public String getDeviceStatus(int deviceId) {
        for (MonitoringDevice d : devices) {
            if (d.getDeviceId() == deviceId) return d.getDeviceStatus();
        }
        return "Device not found";
    }

    @Override
    public String toString() {
        return String.format("%s (ID:%d) | Protected: %s | Devices: %d | Incidents: %d",
                exhibitName, exhibitId, protectionStatus ? "YES" : "NO",
                devices.size(), incidents.size());
    }
}
