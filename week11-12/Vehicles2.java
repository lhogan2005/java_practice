class Engine {
    int horsePower;

    public Engine(int horsePower) {
        this.horsePower = horsePower;
    }
}

class Vehicle {
    String brand;
    Engine engine;

    public Vehicle(String brand, Engine engine) {
        this.brand = brand;
        this.engine = engine;
    }

    public void startEngine() {
        System.out.printf("Starting car with %d horsepowers", engine.horsePower);
    }
}

class Car extends Vehicle {
    int numDoors;

    public Car(String brand, int numDoors, Engine engine) {
        super(brand, engine);
        this.numDoors = numDoors;
    }

    @Override
    public void startEngine() {
        System.out.printf("Starting car with %d horsepowers%n", engine.horsePower);
    }
}

class Bike extends Vehicle{
    boolean hasCarrier;

    public Bike(String brand, boolean hasCarrier, Engine engine) {
        super(brand, engine);
        this.hasCarrier = hasCarrier;
    }

    @Override
    public void startEngine() {
        System.out.printf("Starting bike with %d horsepowers%n", engine.horsePower);
    }
}


class ElectricCar extends Car {
    int batteryCapacity;

    public ElectricCar(String brand, int numDoors, int batteryCapacity, Engine engine) {
        super(brand, numDoors, engine);
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public void startEngine() {
        System.out.printf("Starting electric car silently with %d horsepowers%n", engine.horsePower);
    }
}