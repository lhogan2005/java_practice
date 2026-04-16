package museum.devices;

import museum.core.Exhibit;
import java.time.LocalDateTime;

public abstract class MonitoringDevice {
    private int deviceId;
    private String deviceName;
    private String deviceType;
    private String deviceStatus; // "NORMAL", "WARNING", "CRITICAL", "OFFLINE"
    private LocalDateTime lastServiced;
    private Exhibit attachedExhibit;

    public MonitoringDevice(int deviceId, String deviceName, String deviceType) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.deviceStatus = "NORMAL";
        this.lastServiced = LocalDateTime.now();
    }

    public abstract String detectCondition();
    public abstract void reportStatusChange();

    public void runSelfCheck() {
        System.out.println("  [" + deviceName + "] Running self-check... OK");
        deviceStatus = "NORMAL";
    }

    public int getDeviceId() { return deviceId; }
    public String getDeviceName() { return deviceName; }
    public String getDeviceType() { return deviceType; }
    public String getDeviceStatus() { return deviceStatus; }
    protected void setDeviceStatus(String status) { this.deviceStatus = status; }
    public LocalDateTime getLastServiced() { return lastServiced; }
    public Exhibit getAttachedExhibit() { return attachedExhibit; }
    public void setAttachedExhibit(Exhibit exhibit) { this.attachedExhibit = exhibit; }

    @Override
    public String toString() {
        return String.format("[%s] %s (ID:%d) | Status: %s | Exhibit: %s",
                deviceType, deviceName, deviceId, deviceStatus,
                attachedExhibit != null ? attachedExhibit.getExhibitName() : "Unassigned");
    }
}
