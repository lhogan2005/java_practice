package museum.core;

import museum.devices.MonitoringDevice;

public class DisplayCaseExhibit extends Exhibit {
    private String details;
    private String created;
    private boolean interactive;

    public DisplayCaseExhibit(int id, String name, String details, String created, boolean interactive) {
        super(id, name);
        this.details = details;
        this.created = created;
        this.interactive = interactive;
    }

    @Override
    public void updateProtectionStatus(boolean status) {
        setProtectionStatus(status);
        System.out.println("  [DisplayCaseExhibit: " + getExhibitName() + "] Protection status updated to: " + (status ? "ACTIVE" : "COMPROMISED"));
    }

    @Override
    public void assignDevice(MonitoringDevice device) {
        addDeviceToList(device);
        device.setAttachedExhibit(this);
        System.out.println("  [DisplayCaseExhibit: " + getExhibitName() + "] Device assigned: " + device.getDeviceName());
    }

    public String getDetails() { return details; }
    public String getCreated() { return created; }
    public boolean isInteractive() { return interactive; }

    @Override
    public String toString() {
        return "[DISPLAY CASE] " + super.toString() + " | Interactive: " + interactive;
    }
}
