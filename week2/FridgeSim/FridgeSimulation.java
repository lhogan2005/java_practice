package week2.FridgeSim;
public class FridgeSimulation {
    public static void main(String[] args) {
        Fridge fridge = new Fridge(5);

        fridge.addFood("Milk", 3);
        fridge.addFood("Bread", 1);

        // Not enough money now
        // Error message should appear

        fridge.addFood("Eggs", 4);

        fridge.getFood("Milk");
        fridge.getFood("Juice");

        fridge.checkStatus();
    }
}
