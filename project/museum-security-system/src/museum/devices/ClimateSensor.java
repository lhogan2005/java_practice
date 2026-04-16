package museum.devices;

import java.util.ArrayList;
import java.util.List;

public class ClimateSensor extends MonitoringDevice {
    private float currentTemperature;
    private float currentHumidity;
    private float temperatureThresholdMin;
    private float temperatureThresholdMax;
    private float humidityThresholdMin;
    private float humidityThresholdMax;

    public ClimateSensor(int id, String name,
                         float tempMin, float tempMax,
                         float humMin, float humMax) {
        super(id, name, "CLIMATE");
        this.temperatureThresholdMin = tempMin;
        this.temperatureThresholdMax = tempMax;
        this.humidityThresholdMin = humMin;
        this.humidityThresholdMax = humMax;
        this.currentTemperature = (tempMin + tempMax) / 2;
        this.currentHumidity = (humMin + humMax) / 2;
    }

    @Override
    public String detectCondition() {
        boolean tempOk = currentTemperature >= temperatureThresholdMin
                && currentTemperature <= temperatureThresholdMax;
        boolean humOk = currentHumidity >= humidityThresholdMin
                && currentHumidity <= humidityThresholdMax;

        if (!tempOk || !humOk) {
            setDeviceStatus("WARNING");
            return "WARNING";
        }
        setDeviceStatus("NORMAL");
        return "NORMAL";
    }

    @Override
    public void reportStatusChange() {
        String condition = detectCondition();
        System.out.println("  [ClimateSensor: " + getDeviceName() + "] Status -> " + condition
                + " | Temp: " + currentTemperature + "°C | Humidity: " + currentHumidity + "%");
    }

    public List<String> getCurrentReadings() {
        List<String> readings = new ArrayList<>();
        readings.add("Temperature: " + currentTemperature + "°C (range: " + temperatureThresholdMin + "-" + temperatureThresholdMax + ")");
        readings.add("Humidity: " + currentHumidity + "% (range: " + humidityThresholdMin + "-" + humidityThresholdMax + ")");
        return readings;
    }

    public void setReadings(float temperature, float humidity) {
        this.currentTemperature = temperature;
        this.currentHumidity = humidity;
        System.out.println("  [ClimateSensor: " + getDeviceName() + "] Readings updated: "
                + temperature + "°C / " + humidity + "%");
    }

    public float getCurrentTemperature() { return currentTemperature; }
    public float getCurrentHumidity() { return currentHumidity; }
}
