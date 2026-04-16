package museum.devices;

import java.time.LocalDateTime;

public class GlassVibrationSensor extends MonitoringDevice {
    private float vibrationThreshold;
    private float lastRecordedVibration;
    private LocalDateTime lastRecordedVibrationTime;
    private String sensitivityLevel;

    public GlassVibrationSensor(int id, String name, float threshold, String sensitivity) {
        super(id, name, "VIBRATION");
        this.vibrationThreshold = threshold;
        this.lastRecordedVibration = 0f;
        this.sensitivityLevel = sensitivity;
    }

    @Override
    public String detectCondition() {
        if (lastRecordedVibration >= vibrationThreshold) {
            setDeviceStatus("CRITICAL");
            return "CRITICAL";
        } else if (lastRecordedVibration >= vibrationThreshold * 0.7f) {
            setDeviceStatus("WARNING");
            return "WARNING";
        }
        setDeviceStatus("NORMAL");
        return "NORMAL";
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        System.out.println("  [VibrationSensor: " + getDeviceName() + "] Status -> " + condition
                + " | Vibration: " + lastRecordedVibration + " (threshold: " + vibrationThreshold + ")");
    }

    public void recordVibration(float level) {
        this.lastRecordedVibration = level;
        this.lastRecordedVibrationTime = LocalDateTime.now();
        System.out.println("  [VibrationSensor: " + getDeviceName() + "] Vibration recorded: " + level);
    }

    public void calibrateSensitivity(String sensitivity) {
        this.sensitivityLevel = sensitivity;
        System.out.println("  [VibrationSensor: " + getDeviceName() + "] Sensitivity calibrated to: " + sensitivity);
    }

    public float getLastRecordedVibration() { return lastRecordedVibration; }
    public float getVibrationThreshold() { return vibrationThreshold; }
    public String getSensitivityLevel() { return sensitivityLevel; }
}
