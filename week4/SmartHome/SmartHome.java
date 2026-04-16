/*
    NOTE:
    - A constructor is a definition with no return type and has the same name as the class.
    - Composition that list doesn't exits we are instead making said list after it is declared in our class.
*/

// Aggregation: the list of things already exists we are just taking advantage of that
// Co

import java.util.ArrayList;
import java.util.List;

class SmartHome {
    private String ownerName;
    // Here we use aggregation because we are making a list of appliances, those appliances already exist and we are accessing them.
    // This allows the storing of objects of:
    //  - WashingMachine
    //  - Refrigerator
    //  - SmartWashingMachine
    // All subclasses of appliance
    private List<Appliance> appliances;

    public SmartHome(String ownerName) {
        this.ownerName = ownerName;
        this.appliances = new ArrayList<>();
    }

    public void addAppliance(Appliance appliance) {
        this.appliances.add(appliance);
    }

    public void removeAppliance(Appliance appliance) {
        this.appliances.remove(appliance);
    }

    public int getTotalAppliancesInHome() {
        return this.appliances.size();
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void turnOnAllAppliances() {
        if (this.appliances.size() > 0) {
            for (int i = 0; i < this.appliances.size(); i++) {
                this.appliances.get(i).turnOn();
            }
        }
    }

    public void turnOffAllAppliances() {
        if (this.appliances.size() > 0) {
            for (int j = 0; j < this.appliances.size(); j++)
                this.appliances.get(j).turnOff();
        }
    }
}

class Appliance {
    private final int id;
    private String brand;
    private double powerConsumption;
    private boolean isOn;
    private static int nextID = 1;

    public Appliance(String brand, double powerConsumption) {
        this.id = nextID;
        // This doesn't use this. because its will be the same accross all instances of this class, one class instance while the others change between instances. (static)
        Appliance.nextID++;
        this.brand = brand;
        this.powerConsumption = powerConsumption;
        this.isOn = false;
    }

    public void turnOn() {
        if (this.isOn == false) {
            System.out.println("Turning on " + this.brand + " appliance (ID: " + this.id + ")");
            this.isOn = true;
        } else if (this.isOn == true) {
            System.out.println(this.brand + " appliance (ID: " + this.id + ") is already ON");
        }
    }

    public void turnOff() {
        if (this.isOn == false) {
            System.out.println(this.brand + " appliance (ID: " + this.id + ") is already OFF");
        } else if (this.isOn == true) {
            System.out.println("Turning off " + this.brand + " appliance (ID: " + this.id + ")");
            this.isOn = false;
        }
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPowerConsumption(double powerConsumption) {
        if (powerConsumption > 0) {
            this.powerConsumption = powerConsumption;
        } else {
            System.out.println("Invalid value. Must be positive.");
        }
    }

    public boolean isOn() {
        return this.isOn;
    }

    public String getBrand() {
        return this.brand;
    }

    public double getPowerConsumption() {
        return this.powerConsumption;
    }

    public int getId() {
        return id;
    }
}

class WashingMachine extends Appliance {
    private int drumSize;

    public WashingMachine(String brand, double powerConsumption, int drumSize) {
        super(brand, powerConsumption);
        this.drumSize = drumSize;
    }

    public void setDrumSize(int drumSize) {
        if (drumSize > 0) {
            this.drumSize = drumSize;
        } else {
            System.out.println("Invalid value. Must be positive.");
        }
    }

    public void washClothes() {
        if (this.isOn() == false) {
            System.out.println("Cannot wash clothes. The washing machine is OFF.");
        } else if (this.isOn() == true) {
            System.out.println("Washing clothes in a " + this.getBrand() + " washing machine");
        }
    }

    public int getDrumSize() {
        return drumSize;
    }
}

class SmartWashingMachine extends WashingMachine {
    private boolean hasWiFi;

    public SmartWashingMachine(String brand, double powerConsumption, int drumSize, boolean hasWiFi) {
        super(brand, powerConsumption, drumSize);
        this.hasWiFi = hasWiFi;
    }

    public void connectToWiFi() {
        if (this.isOn() == true) {
            System.out.println("Smart Washing Machine (ID: " + this.getId() + ") connected to WiFi.");
        } else if (this.isOn() == false) {
            System.out.println("Cannot connect to WiFi. The machine is OFF.");
        }
    }

    public boolean hasWiFi() {
        return this.hasWiFi;
    }
}

class Refrigerator extends Appliance {
    private double temperature;

    public Refrigerator(String brand, double powerConsumption, double temperature) {
        super(brand, powerConsumption);
        this.temperature = temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void coolItems() {
        if (this.isOn() == true) {
            System.out.println("Cooling items in " + this.getBrand() + " refrigerator at " + this.getTemperature() + "°C (ID: " + this.getId() + ")");
        } else if (this.isOn() == false) {
            System.out.println("Cannot cool items. The refrigerator is OFF.");
        }
    }

    public double getTemperature() {
        return temperature;
    }
}

