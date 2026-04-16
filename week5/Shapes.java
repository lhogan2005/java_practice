abstract class Shape {
    protected String color;

    public Shape(String color) {
        this.color = color;
    }

    abstract double getArea();

    void displayColor() {
        System.out.println("Shape color: " + this.color);
    }
}

class Circle extends Shape{
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        if (radius > 0) {
            this.radius = radius;
        } else {
            this.radius = 0;
        }
    }

    public double getArea() {
        return (Math.PI * Math.pow(this.radius, 2));
    }
}

class Rectangle extends Shape{

    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        if (width > 0 && height > 0) {
            this.width = width;
            this.height = height;
        } else {
            this.width = 0;
            this.height = 0;
        }
    }

    public double getArea() {
        return (this.width * this.height);
    }
}