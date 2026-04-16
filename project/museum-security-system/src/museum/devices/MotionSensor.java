package museum.devices;

import java.time.LocalDateTime;

public class MotionSensor extends MonitoringDevice {
    private float detectionRangeMeters;
    private boolean motionDetected;
    private String sensitivityLevel;
    private LocalDateTime lastTriggerTime;

    public MotionSensor(int id, String name, float rangeMeters, String sensitivity) {
        super(id, name, "MOTION");
        this.detectionRangeMeters = rangeMeters;
        this.motionDetected = false;
        this.sensitivityLevel = sensitivity;
    }

    @Override
    public String detectCondition() {
        if (motionDetected) {
            setDeviceStatus("CRITICAL");
            return "CRITICAL";
        }
        setDeviceStatus("NORMAL");
        return "NORMAL";
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        System.out.println("  [MotionSensor: " + getDeviceName() + "] Status change -> " + condition
                + (motionDetected ? " | MOTION DETECTED at " + LocalDateTime.now() : " | Clear"));
    }

    public void triggerMotion() {
        this.motionDetected = true;
        this.lastTriggerTime = LocalDateTime.now();
        System.out.println("  [MotionSensor: " + getDeviceName() + "] *** MOTION TRIGGERED ***");
    }

    public void clearMotion() {
        this.motionDetected = false;
    }

    public boolean isMotionDetected() { return motionDetected; }
    public float getDetectionRangeMeters() { return detectionRangeMeters; }
    public String getSensitivityLevel() { return sensitivityLevel; }
}
