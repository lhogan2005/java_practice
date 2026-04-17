import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

class MuseumGallery {
    private int galleryId;
    private String name;
    private String location;
    private final List<Exhibit> exhibits = new ArrayList<>();

    public MuseumGallery(int galleryId, String name, String location) {
        this.galleryId = galleryId;
        this.name = name;
        this.location = location;
    }

    public void addExhibit(Exhibit exhibit) {
        exhibits.add(exhibit);
    }

    public void removeExhibit(Exhibit exhibit) {
        exhibits.remove(exhibit);
    }

    public List<Incident> getActiveIncidents() {
        // Return list of incidents.
        return List<Incident> active;
    }

    public int getId() {
        return galleryId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Exhibit> getExhibits() {
        return exhibits;
    }
}

class Incident {
    private static int counter = 0;
    private final int id;
    private final int exhibitId;
    private final int deviceId;
    private final String severity;
    private String status;
    private final String incidentDescription;
    private final LocalDateTime triggeredAt;
    private String acknowledgedBy;

    public Incident(int exhibitId, int deviceId, String severity, String incidentDescription) {
        this.id = counter++;
        this.exhibitId = exhibitId;
        this.deviceId = deviceId;
        this.severity = severity;
        // Defaults to the alarm triggered state when incident is called.
        this.status = "Triggered";
        this.incidentDescription = incidentDescription;
        this.triggeredAt = LocalDateTime.now();
    }

    public void markAcknowledged(String staffId) {
        this.status = "Normal";
        this.acknowledgedBy = staffId;
        System.out.printf("[Incident #%d] Acknowledged by %s%n", id, staffId);
    }

    public void escalate() {
        System.out.printf("[Incident #%d] Being Escalated", id);
    }

    public String getSummary() {
        //DateTimeFormatter timeInfo = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format(incidentDescription);
    }

    public int getId() {
        return id;
    }
    
    public String getSeverity() {
        return severity;
    }

    public String getStatus() {
        return status;
    }

    public int getExhibitId() {
        return exhibitId;
    }
}

abstract class Exhibit {
    private final int exhibitId;
    private final String name;
    private final String securityLevel;
    private boolean isProtected = true;
    private final List<MonitoringDevice> devices = new ArrayList<>();
    private final List<Incident> incidents = new ArrayList<>();

    public Exhibit(int exhibitId, String name, String securityLevel) {
        this.exhibitId = exhibitId;
        this.name = name;
        this.securityLevel = securityLevel;
    }

    abstract void updateProtectionStatus(boolean currentProtection);
    abstract void assignDevice(MonitoringDevice device);

    public Incident createIncidentRecord(String severity, int deviceId, String description) {
        Incident incident = new Incident(exhibitId, deviceId, severity, description);
        incidents.add(incident);
        return incident;
    }

    protected void addDevice(MonitoringDevice device) {
        devices.add(device);
        device.setAttachedExhibit(this);
    }

    public void setProtected(boolean protection) {
        this.isProtected = protection;
    }

    public int getExhibitId() {
        return exhibitId;
    }

    public String getName() {
        return name;
    }

    public List<Incident> getIncidents() {
        return incidents;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public List<MonitoringDevice> getDevices() {
        return devices;
    }
}

class PaintingExhibit extends Exhibit {
    private final String artist;
    private final String medium;
    private final int year;
    private String paintingDescription;

    public PaintingExhibit(int id, String name, String artist, String medium, int year, String securityLevel, String paintingDescription) {
        super(id, name, securityLevel);
        this.artist = artist;
        this.medium = medium;
        this.year = year;
        this.paintingDescription = paintingDescription;
    }

    @Override
    public void updateProtectionStatus(boolean currentProtection) {
        setProtected(currentProtection);
    }

    @Override
    public void assignDevice(MonitoringDevice device) {
        addDevice(device);
    }

    public String getArtist() {
        return artist;
    }

    public String getMedium() {
        return medium;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return paintingDescription;
    }
}

class DisplayCaseExhibit extends Exhibit {
    private final String details;
    private final String origin;
    private final boolean interactive;

    public DisplayCaseExhibit(int id, String name, String details, String origin, boolean interactive, String securityLevel) {
        super(id, name, securityLevel);
        this.details = details;
        this.origin = origin;
        this.interactive = interactive;
    }

    @Override
    public void updateProtectionStatus(boolean isProtected) {
        setProtected(isProtected);
    }

    @Override
    public void assignDevice(MonitoringDevice device) {
        addDevice(device);
    }

    public String getDetails() {
        return details;
    }

    public String getOrigin() {
        return origin;
    }

    public boolean isInteractive() {
        return interactive;
    }
}

// class MuseumGallery {
//     private final int galleryId;
//     private final String name;
//     private final String location;
//     private final List<Exhibit> exhibits = new ArrayList<>();

//     public MuseumGallery(int galleryId, String name, String location) {
//         this.galleryId = galleryId;
//         this.name = name;
//         this.location = location;
//     }

//     public void addExhibit(Exhibit newExhibit) {
//         exhibits.add(newExhibit);
//     }

//     public void removeExhibit(Exhibit removedExhibit) {
//         exhibits.remove(removedExhibit);
//     }

//     public List<Incident> getActiveIncidents() {
//         List<Incident> active = new ArrayList<>();
//         for (Exhibit currentExhibits : exhibits) {
//             for (Incident currentIncidents : currentExhibits.getIncidents()) {
//                 if (!currentIncident.getStatus().equals("CLOSED")) {
//                     active.add(currentIncidents);
//                 }
//             }
//         }
//         return active;
//     }

//     public List<Exhibit> getExhibits() {
//         return exhibits;
//     }

//     public int getId() {
//         return galleryId;
//     }

//     public String getName() {
//         return name;
//     }

//     public String getLocation() {
//         return location;
//     }


// }

abstract class MonitoringDevice {
    private final int deviceId;
    private final String name;
    private final String deviceType;
    private String status = "Normal";
    private Exhibit attachedExhibit;
    protected SecurityCoordinationHub hub;

    public MonitoringDevice(int deviceId, String name, String deviceType) {
        this.deviceId = deviceId;
        this.name = name;
        this.deviceType = deviceType;
    }

    public abstract String detectCondition();
    public abstract void reportStatusChange();

    public void runSelfCheck() {
        this.status = "Normal";
    }

    public String buildReport() {
        return String.format("Device %d (%s) on '%s' - status: %s", deviceId, deviceType,, attachedExhibit != null ? attachedExhibit.getName(): "unattached", status);
    }

    protected void setStatus(String newStatus) {
        this.status = newStatus;
    }

    public void setHub(SecurityCoordinationHub hub) {
        this.hub = hub;
    }

    public void setAttachedExhibit(Exhibit newExhibit) {
        this.attachedExhibit = newExhibit;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getStatus() {
        return status;
    }

    public Exhibit getAttachedExhibit() {
        return attachedExhibit;
    }
}

class MotionSensor extends MonitoringDevice {
    private final float range;
    // Sensitivity measured on a scale 1 -> 10.
    private int sensitivity;
    private boolean motionDetected = false;
    private float detectedDistance;

    public MotionSensor(int id, String name, float range, int sensitivity) {
        super(id, name, "Motion");
        if (sensitivity > 10 || sensitivity < 0) {
            this.sensitivity = sensitivity;
        }
        this.range = range;
    }

    public void triggerMotion(float distance) {
        this.motionDetected = true;
        this.detectedDistance = distance;
    }

    public void clearMotion() {
        this.motionDetected = false;
    }

    @Override
    public String detectCondition() {
        if (motionDetected) {
            setStatus("Critical");
            return "Critical";
        }
        setStatus("Normal");
        return "Normal";
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        if (hub != null && !condition.equals("Normal")) {
            hub.processDeviceReport(this, condition);
        }
    }

    public boolean isMotionDetected() {
        return motionDetected;
    }

    public int getSensitivity() {
        return sensitivity;
    }

}

class GlassVibrationSensor extends MonitoringDevice {
    private final float threshold;
    private int sensitivity;
    private float lastVibration = 0f;

    public GlassVibrationSensor(int id, String name, float threshold, int sensitivity) {
        super(id, name, "Vibration");
        this.threshold = threshold;
        if (sensitivity > 0 || sensitivity < 11) {
            this.sensitivity = sensitivity;
        }
    }

    public void recordVibration(float vibration) {
        this.lastVibration = vibration;
    }

    public void calibrateSensitivity(int newLevel){
        if (sensitivity > 0 || sensitivity < 11) {
            this.sensitivity = newLevel;
        }
    }

    @Override
    public String detectCondition() {
        if (lastVibration >= threshold) {
            setStatus("Critical");
            return "Critical";
        } else {
            setStatus("Normal");
            return "Normal";
        }
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        // Print something
    }

    public float getLastVibration() {
        return lastVibration;
    }

    public float getThreshold() {
        return threshold;
    }
}

class ClimateSensor extends MonitoringDevice {
    private float tempMin;
    private float tempMax;
    private float humMin;
    private float humMax;
    private float currentTemp;
    private float currentHumidity;

    public ClimateSensor(int id, String name, float tempMin, float tempMax, float humMin, float humMax) {
        super(id, name, "Climate");
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.humMin = humMin;
        this.humMax = humMax;
        this.currentTemp = (tempMin + tempMax) / 2;
        this.currentHumidity = (humMin + humMax) / 2;
    }

    public void updateSensorInfo(float temp, float humidity) {
        this.currentTemp = temp;
        this.currentHumidity = humidity;
    }

 //   public List<String> getCurrentReadings() {
        // Return List of data where we have the current temp, the safe high and low
        // Return List of data where we have the current humidity, the safe high and lows.
   // }

    @Override
    public String detectCondition() {
        if ((currentTemp >= tempMin && currentTemp <= tempMax) && (currentHumidity >= humMin && currentHumidity <= humMax)) {
            return "Normal Temp + Humidity";
        } else if ((currentTemp >= tempMin && currentTemp <= tempMax) && (currentHumidity <= humMin || currentHumidity >= humMax)) {
            return "Normal Temp + Critical Humidity";
        } else if ((currentTemp <= tempMin || currentTemp >= tempMax) && (currentHumidity >= humMin && currentHumidity <= humMax)) {
            return "Critical Temp + Normal Humidity";
        } else {
            return "Critical Temp + Humidity";
        }
    }
    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        // print a string with sensor info   
    }

    public float getCurrentTemp() {
        return currentTemp;
    }

    public float getCurrentHumidity() {
        return currentHumidity;
    }

    public void updateCurrentTemp(float currentTemp) {
        this.currentTemp = currentTemp;
    } 

    public void updateCurrentHumidity(float currentHumidity) {
        this.currentHumidity = currentHumidity;
    }
}

interface AlertEndpoint {
    void receiveAlert(Incident incident);
    void displayStatus();
    int getEndpointId();
}

class AlarmPanel implements AlertEndpoint {
    private int panelId;
    private String location;
    private int zoneId;
    private boolean silenced = false;
    private final List<Incident> alerts = new ArrayList<>();

    public AlarmPanel(int panelId, String location, int zoneId) {
        this.panelId = panelId;
        this.location = location;
        this.zoneId = zoneId;
    }

    @Override
    public void receiveAlert(Incident incident) {
        alerts.add(incident);
    }

    public void displayStatus() {
        // Prints the info about gallery alarm set up
    }

    public void resertAlarm() {
        silenced = false;
        alerts.clear();
    }

    @Override 
    public int getEndpointId() {
        return panelId;
    }
}

class CuratorConsole implements AlertEndpoint {
    private int consoleId;
    private String curator;
    private List<Exhibit> watched = new ArrayList<>();
    private final List<Incident> received = new ArrayList<>();

    public CuratorConsole(int consoleId, String curator) {
        this.consoleId = consoleId;
        this.curator = curator;
    }

    public void recieveAlert(Incident incident){
        recieved.add(incident);
    }

    public void displayStatus() {
        //print status
    }

    public String requestExhibitSummary(int exhibitId) {
        // Look through the exhibits, if its in watched then return a string with the info else return error
    }

    public void watchedExhibit(Exhibit e) {
        watched.add(e);
    }

    public int getEndpointId() {
        return consoleId;
    }
}

class GuardUnit implements AlertEndpoint {
    private int unitId;
    private String guardName;
    private boolean available = true;
    private String location = "Base";
    private final List<Incident> assigned = new ArrayList<>();

    public GuardUnit(int unitId, String guardName) {
        this.unitId = unitId;
        this.guardName = guardName;
    }

    public void recieveAlert(Incident incident) {
        assigned.add(incident);
        available = false;
        // Prints something
    }

    public void displayStatus() {
        // Print something
    }

    public void confirmAttendance(Incident incident) {
        // Print something
    }

    public void returnToBase() {
        available = true;
        location = "Base";
        // Orint something
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getUnitId() {
        return unitId;
    }

    @Override 
    public int getEndpointId() {
        return unitId;
    }
}

class SecurityCoordinationHub {
    private List<AlertEndpoint> endpoints = new ArrayList<>();
    private List<Exhibit> exhibits = new ArrayList<>();
    private List<Incident> allIncidents = new ArrayList<>();

    public void registerEndpoints(AlertEndpoint endpoint) {
        endpoints.add(endpoint);
    }

    public void registerExhibit(Exhibit exhibit) {
        exhibits.add(exhibit);
    }

    public void processDeviceReport(MonitoringDevice device; String condition) {
        Exhibit exhibit = device .getAttachedExhibit();
    }

    public void notifyRelevantEndpoints(Incident incident){};

    public void updateExhibitState(Exhibit exhibit, boolean vunerable) {};

    public List<Incident> getAllIncidents() {
        return allIncidents;
    }

    public int getEndpointCount() {
        return endpoints.size();
    }

    public int getExhibitCount() {
        return exhibits.size();
    }

    public List<Incident> getOpenIncidents() {
        // Itterates through finding open incidents, adding them to a list and returning the list.
        return List<Incident> OpenIncidents;
    }

    public void printIncidentLog() {
        //prints log
    }
}