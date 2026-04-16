package museum.core;

import museum.devices.MonitoringDevice;

public class PaintingExhibit extends Exhibit {
    private String artist;
    private String medium;
    private String created;
    private String details;

    public PaintingExhibit(int id, String name, String artist, String medium, String created) {
        super(id, name);
        this.artist = artist;
        this.medium = medium;
        this.created = created;
        this.details = "";
    }

    @Override
    public void updateProtectionStatus(boolean status) {
        setProtectionStatus(status);
        System.out.println("  [PaintingExhibit: " + getExhibitName() + "] Protection status updated to: " + (status ? "ACTIVE" : "COMPROMISED"));
    }

    @Override
    public void assignDevice(MonitoringDevice device) {
        addDeviceToList(device);
        device.setAttachedExhibit(this);
        System.out.println("  [PaintingExhibit: " + getExhibitName() + "] Device assigned: " + device.getDeviceName());
    }

    public String getArtist() { return artist; }
    public String getMedium() { return medium; }
    public String getCreated() { return created; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    @Override
    public String toString() {
        return "[PAINTING] " + super.toString() + " | Artist: " + artist + " | Medium: " + medium;
    }
}
