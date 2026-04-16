package class_notes.constructorOverloading;

public class Rectangle {
    private int width;
    private int height;

    public Rectangle() {
        this.width = 1;
        this.height = 1; // Default dimensions
    }

    public Rectangle(int size) {
        this.width = size;
        this.height = size; // Square dimenstions
    }

    // Constructor with one parameter
    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Signals to the class that we are planning to Overwrite this, this reduces errors in our code.
    @Override
    public String toString() {
        return "Rectangle{" +
        "width=" + width +
        ", height=" + height +
        '}';
    }
}