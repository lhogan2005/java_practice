public class Person {
    String name;
    int age;
    String address;

    public Person(String name, int age, String address) {
        System.out.println("Person constructor");
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
    }
}

interface SpecialFunctionality {
    void fire();
}

class Worker extends Person implements SpecialFunctionality {
    String workerID;
    public Worker(String name, int age, String address, String workerID) {
        super(name, age, address);
        this.workerID = workerID;
    }

    public void updateWorkerInfo(String newAddress) {
        super.address = newAddress;
    }

    public void updateWorkerInfo(int newAge) {
        super.age = newAge;
    } 

    public void updateWorkerInfo(String newAddress, int newAge) {
        super.address = newAddress;
        super.age = newAge;
    } 

    @Override
    public void fire() {
        super.address = "Fired";
        System.out.printf("Worker %s has been fired!%n", this.workerID);
    }
} 