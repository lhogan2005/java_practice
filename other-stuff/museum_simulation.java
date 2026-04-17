import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// ================================================================
//  MUSEUM EXHIBIT SECURITY & MONITORING SYSTEM
//  Clean implementation based on brief
//
//  Structure:
//    - MuseumGallery         : contains Exhibits
//    - Exhibit (abstract)    : PaintingExhibit, DisplayCaseExhibit
//    - MonitoringDevice (abs): MotionSensor, GlassVibrationSensor,
//                              ClimateSensor
//    - Incident              : records security events
//    - AlertEndpoint (iface) : AlarmPanel, CuratorConsole, GuardUnit
//    - SecurityCoordinationHub : central coordinator (Observer pattern)
// ================================================================

public class MuseumSecurity {

    public static void main(String[] args) {
        System.out.println("=== MUSEUM EXHIBIT SECURITY & MONITORING SYSTEM — DEMO ===\n");

        // ── 1. Create the coordination hub ────────────────────
        System.out.println("1. Initialising Security Coordination Hub");
        SecurityCoordinationHub hub = new SecurityCoordinationHub();

        // ── 2. Create alert endpoints and register with hub ───
        System.out.println("\n2. Registering Alert Endpoints");
        AlarmPanel     northPanel = new AlarmPanel("AP-01", "North Wing Entrance", "Zone-N");
        AlarmPanel     southPanel = new AlarmPanel("AP-02", "South Wing Entrance", "Zone-S");
        CuratorConsole curator   = new CuratorConsole("CC-01", "Dr. Aoife Ryan");
        GuardUnit      guard1    = new GuardUnit("GU-01", "Officer Brennan");
        GuardUnit      guard2    = new GuardUnit("GU-02", "Officer Kelly");

        hub.registerEndpoint(northPanel);
        hub.registerEndpoint(southPanel);
        hub.registerEndpoint(curator);
        hub.registerEndpoint(guard1);
        hub.registerEndpoint(guard2);

        // ── 3. Create galleries ────────────────────────────────
        System.out.println("\n3. Setting Up Galleries");
        MuseumGallery ancientGallery = new MuseumGallery("G-01", "Ancient World",  "West Wing");
        MuseumGallery modernGallery  = new MuseumGallery("G-02", "Modern Masters", "East Wing");

        // ── 4. Create exhibits ─────────────────────────────────
        System.out.println("\n4. Creating Exhibits");
        PaintingExhibit    starryNight = new PaintingExhibit("EX-101", "The Starry Night",
                                          "Vincent van Gogh", "Oil on canvas", "1889", "HIGH");
        PaintingExhibit    guernica    = new PaintingExhibit("EX-102", "Guernica Study",
                                          "Pablo Picasso",    "Oil on canvas", "1937", "HIGH");
        DisplayCaseExhibit mingVase    = new DisplayCaseExhibit("EX-103", "Ming Dynasty Vase",
                                          "15th century porcelain", "1420", false, "CRITICAL");
        DisplayCaseExhibit rosetta     = new DisplayCaseExhibit("EX-104", "Rosetta Stone Fragment",
                                          "Granodiorite stele", "196 BC", true,  "HIGH");

        modernGallery.addExhibit(starryNight);
        modernGallery.addExhibit(guernica);
        ancientGallery.addExhibit(mingVase);
        ancientGallery.addExhibit(rosetta);

        hub.registerExhibit(starryNight);
        hub.registerExhibit(guernica);
        hub.registerExhibit(mingVase);
        hub.registerExhibit(rosetta);

        // ── 5. Create and attach monitoring devices ────────────
        System.out.println("\n5. Attaching Monitoring Devices");
        MotionSensor         ms1 = new MotionSensor("MS-01", "Motion — Starry Night", 2.0f, "HIGH");
        MotionSensor         ms2 = new MotionSensor("MS-02", "Motion — Guernica",     2.5f, "HIGH");
        GlassVibrationSensor gv1 = new GlassVibrationSensor("GV-01", "Vibration — Ming Vase",  4.0f, "HIGH");
        GlassVibrationSensor gv2 = new GlassVibrationSensor("GV-02", "Vibration — Rosetta",    3.5f, "MEDIUM");
        ClimateSensor        cs1 = new ClimateSensor("CS-01", "Climate — Modern Gallery",
                                        18f, 24f, 40f, 60f);
        ClimateSensor        cs2 = new ClimateSensor("CS-02", "Climate — Ancient Gallery",
                                        16f, 22f, 35f, 55f);

        starryNight.assignDevice(ms1);
        starryNight.assignDevice(cs1);
        guernica.assignDevice(ms2);
        mingVase.assignDevice(gv1);
        mingVase.assignDevice(cs2);
        rosetta.assignDevice(gv2);

        ms1.setHub(hub);  ms2.setHub(hub);
        gv1.setHub(hub);  gv2.setHub(hub);
        cs1.setHub(hub);  cs2.setHub(hub);

        // ── 6. Startup self-checks ─────────────────────────────
        System.out.println("\n6. Device Self-Checks");
        for (Exhibit e : List.of(starryNight, guernica, mingVase, rosetta))
            for (MonitoringDevice d : e.getDevices())
                d.runSelfCheck();

        // ── 7. Print initial gallery state ────────────────────
        System.out.println("\n7. Initial Gallery State");
        printGalleryState(ancientGallery);
        printGalleryState(modernGallery);

        // ═══════════════════════════════════════════════════════
        // SCENARIO A — Climate warning (WARNING)
        // ═══════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO A: Climate Warning in Modern Gallery ===");
        System.out.println("  Humidity rises to 74 % (safe max: 60 %)");
        cs1.updateReadings(21.5f, 74f);
        cs1.reportStatusChange();

        // ═══════════════════════════════════════════════════════
        // SCENARIO B — Critical motion near a painting
        // ═══════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO B: Motion Detected Near Starry Night ===");
        System.out.println("  After-hours movement detected 1.2 m from the painting");
        ms1.triggerMotion(1.2f);
        ms1.reportStatusChange();

        List<Incident> open = hub.getOpenIncidents();
        if (!open.isEmpty()) {
            Incident latest = open.get(open.size() - 1);
            System.out.println();
            guard1.confirmAttendance(latest);
            latest.markAcknowledged(guard1.getUnitId());
        }

        // ═══════════════════════════════════════════════════════
        // SCENARIO C — Glass vibration on the Ming Vase (CRITICAL)
        // ═══════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO C: Glass Vibration on Ming Dynasty Vase ===");
        System.out.println("  Vibration sensor records 7.2 units (threshold: 4.0)");
        gv1.recordVibration(7.2f);
        gv1.reportStatusChange();

        open = hub.getOpenIncidents();
        if (!open.isEmpty()) {
            Incident vaseIncident = open.get(open.size() - 1);
            System.out.println();
            vaseIncident.escalate();
            guard2.confirmAttendance(vaseIncident);
            vaseIncident.markAcknowledged(guard2.getUnitId());
            vaseIncident.closeIncident("Vibration from nearby construction. Case secured.");
        }

        // ═══════════════════════════════════════════════════════
        // SCENARIO D — Temperature drop in ancient gallery
        // ═══════════════════════════════════════════════════════
        System.out.println("\n=== SCENARIO D: Temperature Drop in Ancient Gallery ===");
        System.out.println("  Temperature falls to 12 °C (safe min: 16 °C)");
        cs2.updateReadings(12.0f, 48f);
        cs2.reportStatusChange();

        // ── Final report ───────────────────────────────────────
        System.out.println("\n=== FINAL SYSTEM REPORT ===\n");

        System.out.println("Gallery States:");
        printGalleryState(ancientGallery);
        printGalleryState(modernGallery);

        System.out.println("\nEndpoint Statuses:");
        northPanel.displayStatus();
        southPanel.displayStatus();
        curator.displayStatus();
        guard1.displayStatus();
        guard2.displayStatus();

        System.out.println("\nFull Incident Log:");
        hub.printIncidentLog();

        System.out.println("\nHub Summary:");
        System.out.printf("  Total incidents recorded : %d%n", hub.getAllIncidents().size());
        System.out.printf("  Open incidents           : %d%n", hub.getOpenIncidents().size());
        System.out.printf("  Registered endpoints     : %d%n", hub.getEndpointCount());
        System.out.printf("  Monitored exhibits       : %d%n", hub.getExhibitCount());

        System.out.println("\n=== END OF DEMO ===");
    }

    static void printGalleryState(MuseumGallery g) {
        System.out.printf("  Gallery: %s (%s)%n", g.getName(), g.getLocation());
        for (Exhibit e : g.getExhibits()) {
            System.out.printf("    [%s] %-32s  level=%-8s  protected=%s  devices=%d  incidents=%d%n",
                    e.getExhibitId(), e.getName(), e.getSecurityLevel(),
                    e.isProtected() ? "YES" : "NO ",
                    e.getDevices().size(), e.getIncidents().size());
        }
    }
}


public class MuseumSecurity {

    // ── helpers ────────────────────────────────────────────────
    static void banner(String title) {
        String line = "─".repeat(62);
        System.out.println("\n┌" + line + "┐");
        System.out.printf("│  %-60s│%n", title);
        System.out.println("└" + line + "┘");
    }

    static void section(String title) {
        System.out.println("\n  ▸ " + title);
        System.out.println("  " + "·".repeat(56));
    }

    // ── entry point ────────────────────────────────────────────
    public static void main(String[] args) {
        banner("MUSEUM EXHIBIT SECURITY & MONITORING SYSTEM — DEMO");

        // ── 1. Create the coordination hub ────────────────────
        section("1. Initialising Security Coordination Hub");
        SecurityCoordinationHub hub = new SecurityCoordinationHub();

        // ── 2. Create alert endpoints and register with hub ───
        section("2. Registering Alert Endpoints");
        AlarmPanel     northPanel = new AlarmPanel("AP-01", "North Wing Entrance", "Zone-N");
        AlarmPanel     southPanel = new AlarmPanel("AP-02", "South Wing Entrance", "Zone-S");
        CuratorConsole curator   = new CuratorConsole("CC-01", "Dr. Aoife Ryan");
        GuardUnit      guard1    = new GuardUnit("GU-01", "Officer Brennan");
        GuardUnit      guard2    = new GuardUnit("GU-02", "Officer Kelly");

        hub.registerEndpoint(northPanel);
        hub.registerEndpoint(southPanel);
        hub.registerEndpoint(curator);
        hub.registerEndpoint(guard1);
        hub.registerEndpoint(guard2);

        // ── 3. Create galleries ────────────────────────────────
        section("3. Setting Up Galleries");
        MuseumGallery ancientGallery = new MuseumGallery("G-01", "Ancient World",  "West Wing");
        MuseumGallery modernGallery  = new MuseumGallery("G-02", "Modern Masters", "East Wing");

        // ── 4. Create exhibits ─────────────────────────────────
        section("4. Creating Exhibits");
        PaintingExhibit    starryNight = new PaintingExhibit("EX-101", "The Starry Night",
                                          "Vincent van Gogh", "Oil on canvas", "1889", "HIGH");
        PaintingExhibit    guernica    = new PaintingExhibit("EX-102", "Guernica Study",
                                          "Pablo Picasso",    "Oil on canvas", "1937", "HIGH");
        DisplayCaseExhibit mingVase    = new DisplayCaseExhibit("EX-103", "Ming Dynasty Vase",
                                          "15th century porcelain", "1420", false, "CRITICAL");
        DisplayCaseExhibit rosetta     = new DisplayCaseExhibit("EX-104", "Rosetta Stone Fragment",
                                          "Granodiorite stele", "196 BC", true,  "HIGH");

        modernGallery.addExhibit(starryNight);
        modernGallery.addExhibit(guernica);
        ancientGallery.addExhibit(mingVase);
        ancientGallery.addExhibit(rosetta);

        hub.registerExhibit(starryNight);
        hub.registerExhibit(guernica);
        hub.registerExhibit(mingVase);
        hub.registerExhibit(rosetta);

        // ── 5. Create and attach monitoring devices ────────────
        section("5. Attaching Monitoring Devices");
        MotionSensor         ms1 = new MotionSensor("MS-01", "Motion — Starry Night", 2.0f, "HIGH");
        MotionSensor         ms2 = new MotionSensor("MS-02", "Motion — Guernica",     2.5f, "HIGH");
        GlassVibrationSensor gv1 = new GlassVibrationSensor("GV-01", "Vibration — Ming Vase",  4.0f, "HIGH");
        GlassVibrationSensor gv2 = new GlassVibrationSensor("GV-02", "Vibration — Rosetta",    3.5f, "MEDIUM");
        ClimateSensor        cs1 = new ClimateSensor("CS-01", "Climate — Modern Gallery",
                                        18f, 24f, 40f, 60f);
        ClimateSensor        cs2 = new ClimateSensor("CS-02", "Climate — Ancient Gallery",
                                        16f, 22f, 35f, 55f);

        starryNight.assignDevice(ms1);
        starryNight.assignDevice(cs1);
        guernica.assignDevice(ms2);
        mingVase.assignDevice(gv1);
        mingVase.assignDevice(cs2);
        rosetta.assignDevice(gv2);

        ms1.setHub(hub);  ms2.setHub(hub);
        gv1.setHub(hub);  gv2.setHub(hub);
        cs1.setHub(hub);  cs2.setHub(hub);

        // ── 6. Startup self-checks ─────────────────────────────
        section("6. Device Self-Checks");
        for (Exhibit e : List.of(starryNight, guernica, mingVase, rosetta))
            for (MonitoringDevice d : e.getDevices())
                d.runSelfCheck();

        // ── 7. Print initial gallery state ────────────────────
        section("7. Initial Gallery State");
        printGalleryState(ancientGallery);
        printGalleryState(modernGallery);

        // ═══════════════════════════════════════════════════════
        // SCENARIO A — Climate warning (WARNING)
        // Curator + Guards notified. Alarm panels stay silent.
        // ═══════════════════════════════════════════════════════
        banner("SCENARIO A  ·  Climate Warning in Modern Gallery");
        System.out.println("  Humidity rises to 74 % (safe max: 60 %)");
        cs1.updateReadings(21.5f, 74f);
        cs1.reportStatusChange();

        // ═══════════════════════════════════════════════════════
        // SCENARIO B — Critical motion near a painting
        // All endpoints notified. Exhibit marked compromised.
        // ═══════════════════════════════════════════════════════
        banner("SCENARIO B  ·  Motion Detected Near Starry Night");
        System.out.println("  After-hours movement detected 1.2 m from the painting");
        ms1.triggerMotion(1.2f);
        ms1.reportStatusChange();

        List<Incident> open = hub.getOpenIncidents();
        if (!open.isEmpty()) {
            Incident latest = open.get(open.size() - 1);
            System.out.println();
            guard1.confirmAttendance(latest);
            latest.markAcknowledged(guard1.getUnitId());
        }

        // ═══════════════════════════════════════════════════════
        // SCENARIO C — Glass vibration on the Ming Vase (CRITICAL)
        // All endpoints notified. Incident escalated then closed.
        // ═══════════════════════════════════════════════════════
        banner("SCENARIO C  ·  Glass Vibration on Ming Dynasty Vase");
        System.out.println("  Vibration sensor records 7.2 units (threshold: 4.0)");
        gv1.recordVibration(7.2f);
        gv1.reportStatusChange();

        open = hub.getOpenIncidents();
        if (!open.isEmpty()) {
            Incident vaseIncident = open.get(open.size() - 1);
            System.out.println();
            vaseIncident.escalate();
            guard2.confirmAttendance(vaseIncident);
            vaseIncident.markAcknowledged(guard2.getUnitId());
            vaseIncident.closeIncident("Vibration from nearby construction. Case secured.");
        }

        // ═══════════════════════════════════════════════════════
        // SCENARIO D — Temperature drop in ancient gallery
        // Conservation risk — warning issued.
        // ═══════════════════════════════════════════════════════
        banner("SCENARIO D  ·  Temperature Drop in Ancient Gallery");
        System.out.println("  Temperature falls to 12 °C (safe min: 16 °C)");
        cs2.updateReadings(12.0f, 48f);
        cs2.reportStatusChange();

        // ── Final report ───────────────────────────────────────
        banner("FINAL SYSTEM REPORT");

        section("Gallery States");
        printGalleryState(ancientGallery);
        printGalleryState(modernGallery);

        section("Endpoint Statuses");
        northPanel.displayStatus();
        southPanel.displayStatus();
        curator.displayStatus();
        guard1.displayStatus();
        guard2.displayStatus();

        section("Full Incident Log");
        hub.printIncidentLog();

        section("Hub Summary");
        System.out.printf("  Total incidents recorded : %d%n", hub.getAllIncidents().size());
        System.out.printf("  Open incidents           : %d%n", hub.getOpenIncidents().size());
        System.out.printf("  Registered endpoints     : %d%n", hub.getEndpointCount());
        System.out.printf("  Monitored exhibits       : %d%n", hub.getExhibitCount());

        banner("END OF DEMO");
    }

    static void printGalleryState(MuseumGallery g) {
        System.out.printf("  Gallery: %s (%s)%n", g.getName(), g.getLocation());
        for (Exhibit e : g.getExhibits()) {
            System.out.printf("    [%s] %-32s  level=%-8s  protected=%s  devices=%d  incidents=%d%n",
                    e.getExhibitId(), e.getName(), e.getSecurityLevel(),
                    e.isProtected() ? "YES" : "NO ",
                    e.getDevices().size(), e.getIncidents().size());
        }
    }
}


// ================================================================
//  INCIDENT
// ================================================================

class Incident {
    private static int counter = 1;

    private final int           id;
    private final String        exhibitId;
    private final String        deviceId;
    private final String        severity;
    private       String        status;
    private final String        description;
    private final LocalDateTime createdAt;
    private       String        closedNotes;
    private       String        acknowledgedBy;

    public Incident(String exhibitId, String deviceId, String severity, String description) {
        this.id          = counter++;
        this.exhibitId   = exhibitId;
        this.deviceId    = deviceId;
        this.severity    = severity;
        this.status      = "OPEN";
        this.description = description;
        this.createdAt   = LocalDateTime.now();
    }

    public void markAcknowledged(String staffId) {
        this.status         = "ACKNOWLEDGED";
        this.acknowledgedBy = staffId;
        System.out.printf("    [Incident #%03d] Acknowledged by %s%n", id, staffId);
    }

    public void escalate() {
        System.out.printf("    [Incident #%03d] Escalating to senior staff%n", id);
    }

    public void closeIncident(String notes) {
        this.status      = "CLOSED";
        this.closedNotes = notes;
        System.out.printf("    [Incident #%03d] Closed — %s%n", id, notes);
    }

    public String getSummary() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("#%03d | %-8s | %-12s | exhibit=%-7s | device=%-7s | %s | %s",
                id, severity, status, exhibitId, deviceId,
                createdAt.format(f), description);
    }

    public int    getId()        { return id; }
    public String getSeverity()  { return severity; }
    public String getStatus()    { return status; }
    public String getExhibitId() { return exhibitId; }
}


// ================================================================
//  EXHIBIT  (abstract)
// ================================================================

abstract class Exhibit {
    private final String              exhibitId;
    private final String              name;
    private final String              securityLevel;
    private       boolean             isProtected = true;
    private final List<MonitoringDevice> devices   = new ArrayList<>();
    private final List<Incident>         incidents = new ArrayList<>();

    public Exhibit(String exhibitId, String name, String securityLevel) {
        this.exhibitId     = exhibitId;
        this.name          = name;
        this.securityLevel = securityLevel;
    }

    public abstract void updateProtectionStatus(boolean protected_);
    public abstract void assignDevice(MonitoringDevice device);

    public Incident createIncidentRecord(String severity, String deviceId, String description) {
        Incident inc = new Incident(exhibitId, deviceId, severity, description);
        incidents.add(inc);
        return inc;
    }

    protected void addDevice(MonitoringDevice d)  { devices.add(d); d.setAttachedExhibit(this); }
    protected void setProtected(boolean b)         { this.isProtected = b; }

    public List<MonitoringDevice> getDevices()    { return devices; }
    public List<Incident>         getIncidents()  { return incidents; }
    public String  getExhibitId()     { return exhibitId; }
    public String  getName()          { return name; }
    public String  getSecurityLevel() { return securityLevel; }
    public boolean isProtected()      { return isProtected; }
}


// ================================================================
//  PAINTINGEXHIBIT
// ================================================================

class PaintingExhibit extends Exhibit {
    private final String artist;
    private final String medium;
    private final String year;

    public PaintingExhibit(String id, String name, String artist,
                           String medium, String year, String securityLevel) {
        super(id, name, securityLevel);
        this.artist = artist;
        this.medium = medium;
        this.year   = year;
    }

    @Override
    public void updateProtectionStatus(boolean protected_) {
        setProtected(protected_);
        System.out.printf("    [PaintingExhibit: %s] Protection -> %s%n",
                getName(), protected_ ? "ACTIVE" : "COMPROMISED");
    }

    @Override
    public void assignDevice(MonitoringDevice device) {
        addDevice(device);
        System.out.printf("    [PaintingExhibit: %s] Device assigned: %s (%s)%n",
                getName(), device.getName(), device.getDeviceType());
    }

    public String getArtist() { return artist; }
    public String getMedium() { return medium; }
    public String getYear()   { return year; }
}


// ================================================================
//  DISPLAYCASEEXHIBIT
// ================================================================

class DisplayCaseExhibit extends Exhibit {
    private final String  details;
    private final String  origin;
    private final boolean interactive;

    public DisplayCaseExhibit(String id, String name, String details,
                              String origin, boolean interactive, String securityLevel) {
        super(id, name, securityLevel);
        this.details     = details;
        this.origin      = origin;
        this.interactive = interactive;
    }

    @Override
    public void updateProtectionStatus(boolean protected_) {
        setProtected(protected_);
        System.out.printf("    [DisplayCaseExhibit: %s] Protection -> %s%n",
                getName(), protected_ ? "ACTIVE" : "COMPROMISED");
    }

    @Override
    public void assignDevice(MonitoringDevice device) {
        addDevice(device);
        System.out.printf("    [DisplayCaseExhibit: %s] Device assigned: %s (%s)%n",
                getName(), device.getName(), device.getDeviceType());
    }

    public String  getDetails()    { return details; }
    public String  getOrigin()     { return origin; }
    public boolean isInteractive() { return interactive; }
}


// ================================================================
//  MUSEUMGALLERY
// ================================================================

class MuseumGallery {
    private final String        galleryId;
    private final String        name;
    private final String        location;
    private final List<Exhibit> exhibits = new ArrayList<>();

    public MuseumGallery(String galleryId, String name, String location) {
        this.galleryId = galleryId;
        this.name      = name;
        this.location  = location;
    }

    public void addExhibit(Exhibit e) {
        exhibits.add(e);
        System.out.printf("    [Gallery: %s] Exhibit added: %s%n", name, e.getName());
    }

    public void removeExhibit(Exhibit e) {
        exhibits.remove(e);
        System.out.printf("    [Gallery: %s] Exhibit removed: %s%n", name, e.getName());
    }

    public List<Incident> getActiveIncidents() {
        List<Incident> active = new ArrayList<>();
        for (Exhibit e : exhibits)
            for (Incident i : e.getIncidents())
                if (!i.getStatus().equals("CLOSED"))
                    active.add(i);
        return active;
    }

    public List<Exhibit> getExhibits() { return exhibits; }
    public String        getId()       { return galleryId; }
    public String        getName()     { return name; }
    public String        getLocation() { return location; }
}


// ================================================================
//  MONITORINGDEVICE  (abstract)
// ================================================================

abstract class MonitoringDevice {
    private final String    deviceId;
    private final String    name;
    private final String    deviceType;
    private       String    status = "NORMAL";
    private       Exhibit   attachedExhibit;
    protected     SecurityCoordinationHub hub;

    public MonitoringDevice(String deviceId, String name, String deviceType) {
        this.deviceId   = deviceId;
        this.name       = name;
        this.deviceType = deviceType;
    }

    public abstract String detectCondition();
    public abstract void   reportStatusChange();

    public void runSelfCheck() {
        this.status = "NORMAL";
        System.out.printf("    [%s] Self-check -> OK%n", name);
    }

    public String buildReport() {
        return String.format("Device %s (%s) on '%s' — status: %s",
                deviceId, deviceType,
                attachedExhibit != null ? attachedExhibit.getName() : "unattached",
                status);
    }

    protected void setStatus(String s)             { this.status = s; }
    public void    setHub(SecurityCoordinationHub h){ this.hub = h; }
    public void    setAttachedExhibit(Exhibit e)    { this.attachedExhibit = e; }

    public String  getDeviceId()        { return deviceId; }
    public String  getName()            { return name; }
    public String  getDeviceType()      { return deviceType; }
    public String  getStatus()          { return status; }
    public Exhibit getAttachedExhibit() { return attachedExhibit; }
}


// ================================================================
//  MOTIONSENSOR
// ================================================================

class MotionSensor extends MonitoringDevice {
    private final float  rangeMeters;
    private final String sensitivity;
    private       boolean motionDetected  = false;
    private       float   detectedDistance;

    public MotionSensor(String id, String name, float rangeMeters, String sensitivity) {
        super(id, name, "MOTION");
        this.rangeMeters = rangeMeters;
        this.sensitivity = sensitivity;
    }

    public void triggerMotion(float distanceMeters) {
        this.motionDetected    = true;
        this.detectedDistance  = distanceMeters;
        System.out.printf("    [MotionSensor: %s] *** MOTION DETECTED — %.1f m away ***%n",
                getName(), distanceMeters);
    }

    public void clearMotion() { this.motionDetected = false; }

    @Override
    public String detectCondition() {
        if (motionDetected) { setStatus("CRITICAL"); return "CRITICAL"; }
        setStatus("NORMAL");
        return "NORMAL";
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        System.out.printf("    [MotionSensor: %s] Reporting -> %s%n", getName(), condition);
        if (hub != null && !condition.equals("NORMAL"))
            hub.processDeviceReport(this, condition);
    }

    public boolean isMotionDetected() { return motionDetected; }
    public String  getSensitivity()   { return sensitivity; }
}


// ================================================================
//  GLASSVIBRATIONSENSOR
// ================================================================

class GlassVibrationSensor extends MonitoringDevice {
    private final float  threshold;
    private final String sensitivity;
    private       float  lastVibration = 0f;

    public GlassVibrationSensor(String id, String name, float threshold, String sensitivity) {
        super(id, name, "VIBRATION");
        this.threshold   = threshold;
        this.sensitivity = sensitivity;
    }

    public void recordVibration(float level) {
        this.lastVibration = level;
        System.out.printf("    [VibrationSensor: %s] Vibration recorded: %.1f (threshold: %.1f)%n",
                getName(), level, threshold);
    }

    public void calibrateSensitivity(String newLevel) {
        System.out.printf("    [VibrationSensor: %s] Sensitivity recalibrated -> %s%n",
                getName(), newLevel);
    }

    @Override
    public String detectCondition() {
        if (lastVibration >= threshold)       public String detectCondition() {
        if (motionDetected) {
            setStatus("Critical");
            return "Critical";
        }
        setStatus("Normal");
        return "Normal";
    }       { setStatus("CRITICAL"); return "CRITICAL"; }
        if (lastVibration >= threshold * 0.7f)   { setStatus("WARNING");  return "WARNING";  }
        setStatus("NORMAL");
        return "NORMAL";
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        System.out.printf("    [VibrationSensor: %s] Reporting -> %s (reading: %.1f)%n",
                getName(), condition, lastVibration);
        if (hub != null && !condition.equals("NORMAL"))
            hub.processDeviceReport(this, condition);
    }

    public float getLastVibration() { return lastVibration; }
    public float getThreshold()     { return threshold; }
}


// ================================================================
//  CLIMATESENSOR
// ================================================================

class ClimateSensor extends MonitoringDevice {
    private final float tempMin, tempMax, humMin, humMax;
    private       float currentTemp, currentHumidity;

    public ClimateSensor(String id, String name,
                         float tempMin, float tempMax,
                         float humMin,  float humMax) {
        super(id, name, "CLIMATE");
        this.tempMin = tempMin; this.tempMax = tempMax;
        this.humMin  = humMin;  this.humMax  = humMax;
        this.currentTemp     = (tempMin + tempMax) / 2f;
        this.currentHumidity = (humMin  + humMax)  / 2f;
    }

    public void updateReadings(float temp, float humidity) {
        this.currentTemp     = temp;
        this.currentHumidity = humidity;
        System.out.printf("    [ClimateSensor: %s] Readings updated -> %.1f C / %.0f %%%n",
                getName(), temp, humidity);
    }

    public List<String> getCurrentReadings() {
        return List.of(
            String.format("Temp: %.1f C  (safe: %.0f-%.0f)", currentTemp, tempMin, tempMax),
            String.format("Humidity: %.0f %%  (safe: %.0f-%.0f)", currentHumidity, humMin, humMax)
        );
    }

    @Override
    public String detectCondition() {
        boolean tempOk = currentTemp >= tempMin && currentTemp <= tempMax;
        boolean humOk  = currentHumidity >= humMin && currentHumidity <= humMax;
        if (!tempOk || !humOk) { setStatus("WARNING"); return "WARNING"; }
        setStatus("NORMAL");
        return "NORMAL";
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        System.out.printf("    [ClimateSensor: %s] Reporting -> %s  (%.1f C / %.0f %%)%n",
                getName(), condition, currentTemp, currentHumidity);
        if (hub != null && !condition.equals("NORMAL"))
            hub.processDeviceReport(this, condition);
    }

    public float getCurrentTemp()     { return currentTemp; }
    public float getCurrentHumidity() { return currentHumidity; }
}


// ================================================================
//  ALERTENDPOINT  (interface)
// ================================================================

interface AlertEndpoint {
    void   receiveAlert(Incident incident);
    void   displayStatus();
    String getEndpointId();
}


// ================================================================
//  ALARMPANEL
// ================================================================

class AlarmPanel implements AlertEndpoint {
    private final String         panelId;
    private final String         location;
    private final String         zoneId;
    private       boolean        silenced = false;
    private final List<Incident> alerts   = new ArrayList<>();

    public AlarmPanel(String panelId, String location, String zoneId) {
        this.panelId  = panelId;
        this.location = location;
        this.zoneId   = zoneId;
        System.out.printf("    [AlarmPanel] Registered: %s @ %s (zone: %s)%n",
                panelId, location, zoneId);
    }

    @Override
    public void receiveAlert(Incident incident) {
        alerts.add(incident);
        String tag = silenced ? "(silenced)" : "*** ALARM ***";
        System.out.printf("    [AlarmPanel %s] %s  %s%n", panelId, tag, incident.getSummary());
    }

    @Override
    public void displayStatus() {
        System.out.printf("    [AlarmPanel  %s] zone=%-8s  alerts=%-3d  silenced=%s%n",
                panelId, zoneId, alerts.size(), silenced);
    }

    public void silenceTone() {
        silenced = true;
        System.out.printf("    [AlarmPanel %s] Tone silenced%n", panelId);
    }

    public void resetAlarm() {
        silenced = false;
        alerts.clear();
    }

    @Override public String getEndpointId() { return panelId; }
}


// ================================================================
//  CURATORCONSOLE
// ================================================================

class CuratorConsole implements AlertEndpoint {
    private final String         consoleId;
    private final String         curator;
    private final List<Exhibit>  watched  = new ArrayList<>();
    private final List<Incident> received = new ArrayList<>();

    public CuratorConsole(String consoleId, String curator) {
        this.consoleId = consoleId;
        this.curator   = curator;
        System.out.printf("    [CuratorConsole] Registered: %s — assigned to %s%n",
                consoleId, curator);
    }

    @Override
    public void receiveAlert(Incident incident) {
        received.add(incident);
        System.out.printf("    [CuratorConsole %s — %s] Alert: %s%n",
                consoleId, curator, incident.getSummary());
    }

    @Override
    public void displayStatus() {
        System.out.printf("    [CuratorConsole %s] curator=%-20s  alerts=%-3d  watching=%d%n",
                consoleId, curator, received.size(), watched.size());
    }

    public String requestExhibitSummary(String exhibitId) {
        for (Exhibit e : watched)
            if (e.getExhibitId().equals(exhibitId))
                return String.format("Exhibit '%s' — protected=%s, incidents=%d, devices=%d",
                        e.getName(), e.isProtected(), e.getIncidents().size(), e.getDevices().size());
        return "Exhibit " + exhibitId + " not in watch list";
    }

    public void watchExhibit(Exhibit e) { watched.add(e); }

    @Override public String getEndpointId() { return consoleId; }
}


// ================================================================
//  GUARDUNIT
// ================================================================

class GuardUnit implements AlertEndpoint {
    private final String         unitId;
    private final String         guardName;
    private       boolean        available = true;
    private       String         location  = "Base";
    private final List<Incident> assigned  = new ArrayList<>();

    public GuardUnit(String unitId, String guardName) {
        this.unitId    = unitId;
        this.guardName = guardName;
        System.out.printf("    [GuardUnit] Registered: %s — %s%n", unitId, guardName);
    }

    @Override
    public void receiveAlert(Incident incident) {
        assigned.add(incident);
        available = false;
        System.out.printf("    [GuardUnit %s — %s] Dispatching to exhibit %s!%n",
                unitId, guardName, incident.getExhibitId());
    }

    @Override
    public void displayStatus() {
        System.out.printf("    [GuardUnit   %s] name=%-18s  available=%s  assigned=%d  location=%s%n",
                unitId, guardName, available, assigned.size(), location);
    }

    public void confirmAttendance(Incident incident) {
        System.out.printf("    [GuardUnit %s — %s] On scene for incident #%03d%n",
                unitId, guardName, incident.getId());
    }

    public void returnToBase() {
        available = true;
        location  = "Base";
        System.out.printf("    [GuardUnit %s — %s] Returned to base%n", unitId, guardName);
    }

    public void    setLocation(String loc) { this.location = loc; }
    public boolean isAvailable()           { return available; }
    public String  getUnitId()             { return unitId; }

    @Override public String getEndpointId() { return unitId; }
}


// ================================================================
//  SECURITYCOORDINATIONHUB
//
//  Central coordinator using Observer pattern.
//  Devices report to it; it decides which endpoints to notify.
//  Sensors and exhibits have no knowledge of each other's endpoints.
// ================================================================

class SecurityCoordinationHub {
    private final List<AlertEndpoint> endpoints    = new ArrayList<>();
    private final List<Exhibit>       exhibits     = new ArrayList<>();
    private final List<Incident>      allIncidents = new ArrayList<>();

    public void registerEndpoint(AlertEndpoint ep) { endpoints.add(ep); }
    public void registerExhibit(Exhibit e)          { exhibits.add(e); }

    /**
     * Called by a MonitoringDevice when it detects an abnormal condition.
     * Hub creates an Incident, updates exhibit state, then fans out alerts.
     */
    public void processDeviceReport(MonitoringDevice device, String condition) {
        Exhibit exhibit = device.getAttachedExhibit();
        if (exhibit == null) return;

        String description = String.format(
                "%s sensor '%s' detected %s condition",
                device.getDeviceType(), device.getName(), condition);

        Incident incident = exhibit.createIncidentRecord(condition, device.getDeviceId(), description);
        allIncidents.add(incident);

        System.out.printf("%n    [Hub] New incident -> %s%n", incident.getSummary());

        updateExhibitState(exhibit, condition.equals("CRITICAL"));
        notifyRelevantEndpoints(incident);
    }

    public void notifyRelevantEndpoints(Incident incident) {
        System.out.printf("    [Hub] Notifying endpoints (severity: %s)...%n", incident.getSeverity());
        for (AlertEndpoint ep : endpoints) {
            if (incident.getSeverity().equals("CRITICAL")) {
                ep.receiveAlert(incident);
            } else {
                // WARNING: skip alarm panels — no audible alert for non-critical events
                if (!(ep instanceof AlarmPanel))
                    ep.receiveAlert(incident);
            }
        }
    }

    public void updateExhibitState(Exhibit exhibit, boolean compromised) {
        exhibit.updateProtectionStatus(!compromised);
        System.out.printf("    [Hub] Exhibit '%s' -> %s%n",
                exhibit.getName(), compromised ? "COMPROMISED" : "PROTECTED");
    }

    public List<Incident> getAllIncidents()  { return allIncidents; }
    public int            getEndpointCount(){ return endpoints.size(); }
    public int            getExhibitCount() { return exhibits.size(); }

    public List<Incident> getOpenIncidents() {
        List<Incident> open = new ArrayList<>();
        for (Incident i : allIncidents)
            if (!i.getStatus().equals("CLOSED")) open.add(i);
        return open;
    }

    public void printIncidentLog() {
        if (allIncidents.isEmpty()) { System.out.println("  No incidents recorded."); return; }
        for (Incident i : allIncidents)
            System.out.println("  " + i.getSummary());
    }
}