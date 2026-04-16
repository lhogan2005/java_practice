package museum.core;

import java.util.ArrayList;
import java.util.List;

public class MuseumGallery {
    private String galleryId;
    private String name;
    private String location;
    private List<Exhibit> exhibits;

    public MuseumGallery(String galleryId, String name, String location) {
        this.galleryId = galleryId;
        this.name = name;
        this.location = location;
        this.exhibits = new ArrayList<>();
    }

    public void addExhibit(Exhibit exhibit) {
        exhibits.add(exhibit);
        System.out.println("  [Gallery: " + name + "] Added exhibit: " + exhibit.getExhibitName());
    }

    public void removeExhibit(Exhibit exhibit) {
        exhibits.remove(exhibit);
        System.out.println("  [Gallery: " + name + "] Removed exhibit: " + exhibit.getExhibitName());
    }

    public List<Incident> getActiveIncidents() {
        List<Incident> active = new ArrayList<>();
        for (Exhibit e : exhibits) {
            for (Incident i : e.getIncidents()) {
                if (!i.getStatus().equals("CLOSED")) {
                    active.add(i);
                }
            }
        }
        return active;
    }

    public int getTotalExhibits() { return exhibits.size(); }
    public List<Exhibit> getExhibits() { return exhibits; }
    public String getGalleryId() { return galleryId; }
    public String getGalleryName() { return name; }
    public String getGalleryLocation() { return location; }
    public void setGalleryId(String id) { this.galleryId = id; }
    public void setName(String name) { this.name = name; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return String.format("Gallery '%s' [%s] @ %s | Exhibits: %d", name, galleryId, location, exhibits.size());
    }
}
