package week4.SmartHome;
public class SmartHomeDemo {

    public static void main(String[] args) {

        // Create SmartHome
        SmartHome home = new SmartHome("Alex");

        // Create appliances
        WashingMachine wm =
                new WashingMachine("LG", 1300, 6);

        Refrigerator fridge =
                new Refrigerator("Whirlpool", 800, 4.0);

        SmartWashingMachine smartWM =
                new SmartWashingMachine("Samsung", 1500, 7, true);

        // Add appliances to SmartHome
        home.addAppliance(wm);
        home.addAppliance(fridge);
        home.addAppliance(smartWM);

        // Test behavior
        System.out.println("Total appliances: "
                + home.getTotalAppliancesInHome());

        wm.turnOn();
        wm.washClothes();

        fridge.turnOn();
        fridge.coolItems();

        smartWM.turnOn();
        smartWM.connectToWiFi();

        //polymorphism
        System.out.println("Turning off all appliances:");
        home.turnOffAllAppliances();
    }
}