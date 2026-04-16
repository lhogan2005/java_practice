package week2.FridgeSim;
import java.util.ArrayList;
import java.util.List;

public class Fridge {

        private List<String> foodItems = new ArrayList<>();
        private int balance;

        public Fridge(int initialBalance) {
                if (initialBalance < 0) {
                        System.out.println("Error");
                        this.balance = 0;
                } else {
                        this.balance = initialBalance;
                }
        }

        public void addFood(String item, int cost) {
            if (((balance - cost) < 0) || (cost < 0)) {
                System.out.println("Error");
            } else {
                if (item != null) {
                    foodItems.add(item);
                    this.balance = balance - cost;
                    System.out.println("Item " + item + " has been added to the fridge.");
                } else {
                    System.out.println("Error");
                }
            }
        }

        public void getFood(String item) {
            if (foodItems.contains(item)) {
                foodItems.remove(item);
                System.out.println("Item " + item + " has been removed from the fridge.");
            } else {
                System.out.println("Error");
            }
        }

        public void checkStatus() {
            if (foodItems.size() == 0) {
                System.out.println("Food items:");
                System.out.println("(none)");
            } else {
                System.out.println("Food items:");
            }
            for (String food : foodItems) {
                System.out.println(food);
            }

            System.out.println("Balance: €" + this.balance);
        }
}