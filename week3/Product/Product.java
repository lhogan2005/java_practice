package Product;
// Imports for the list utility and the arraylist utility.
import java.util.List;
import java.util.ArrayList;

// Our class to define the functions of our product.
public class Product {
    // Setting types of parameters. This are instance fields. Bascically when you make a new instance of this class in main it will get its own version of these.
    // When you use static this means that it becomes the exact same value across all instances of the class, so if you change it in new Product a it will also change in
    // new product b.
    String productName;
    long price;
    boolean inStock;
    private List<String> tags;

    public Product () {
        this.productName = "Unknown";
        this.price = 0;
        this.inStock = false;
        this.tags = new ArrayList<>();
    }

    // Duplicate for different input types and combinations.
    public Product (String productName) {
        if (productName == null) {
            this.productName = "Unknown";
        } else {
            this.productName = productName;
        }
        // this.price = 0;
        // this.inStock = false;
        this.tags = new ArrayList<>();
    }

    public Product (String productName, long price) {
        if (productName == null) {
            this.productName = "Unknown";
        } else {
            this.productName = productName;
        }

        if (price < 0) {
            this.price = 0;
        } else { 
            this.price = price;
        }
        this.tags = new ArrayList<>();
    }

    public Product (String productName, long price, boolean inStock) {
        if (productName == null) {
            this.productName = "Unknown";
        } else {
            this.productName = productName;
        }
        if (price < 0) {
            this.price = 0;
        } else { 
            this.price = price;
        }
        this.inStock = inStock;
        this.tags = new ArrayList<>();
    }

    public Product (String productName, long price, List<String> tags) {
        if (productName == null) {
            this.productName = "Unknown";
        } else {
            this.productName = productName;
        }
        if (price < 0) {
            this.price = 0;
        } else { 
            this.price = price;
        }
        if (tags != null) {
            this.tags = new ArrayList<>(tags);
        } else {
            this.tags = new ArrayList<>();
        }
    }

    public Product (String productName, long price, boolean inStock, List<String> tags) {
        if (productName == null) {
            this.productName = "Unknown";
        } else {
            this.productName = productName;
        }
        if (price < 0) {
            this.price = 0;
        } else { 
            this.price = price;
        }
        this.inStock = inStock;
        if (tags != null) {
            this.tags = new ArrayList<>(tags);
        } else {
            this.tags = new ArrayList<>();
        }
    }

    // This is the deep copy. Takes original Product object and uses that to save its own values.
    public Product(Product original) {
        this.productName = original.productName;
        this.price = original.price;
        this.inStock = original.inStock;
        this.tags = new ArrayList<>(original.tags);
    }

    // Returns this correctly.
    public List<String> getTags() {
        return this.tags;
    }

    // Adds correctly
    public void addTag(String tag) {
        if (tag != null) {
            this.tags.add(tag);
        }
    }

    // Overwrites toString method correctly.
    @Override
    public String toString() {
        return "Product{" +
        "productName='" + productName + '\'' +
        ", price=" + price +
        ", inStock=" + inStock +
        ", tags=" + tags +
        '}';
    }
}
