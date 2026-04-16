package class_notes.constructorOverloading;

public class main {
    public static void main(String[] args) {
        Rectangle defaultreRectangle = new Rectangle();
        Rectangle square = new Rectangle(5);
        Rectangle customRectangle = new Rectangle(4, 8);

        System.out.println(defaultreRectangle); // Rectangle{width=1, height=1}
        System.out.println(square); // Rectangle{width=5, height=5}
        System.out.println(customRectangle); // Rectangle{width=4, height=8}
    }
}
