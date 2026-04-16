package Product;
public class Main {
    public static void main(String[] args) {
        Product original = new Product("Laptop", 1699);
        original.addTag("Gaming");
        
        Product copy = new Product(original);
        // This is added to both the original and the new.
        copy.addTag("Performance");

        System.out.println("Original List: " + original.getTags());
        System.out.println("Copy List: " + copy.getTags());
    }
}