/**
 * Shape_Demo - Driver harness for Shape inheritance hierarchy and polymorphic dispatch.
 * Assignment 05: Slide 66 & 91.
 */
public class Shape_Demo {

    public static void main(String[] args){
        System.out.println("==================================================");
        System.out.println("         GEOMETRIC SHAPE HIERARCHY DEMO           ");
        System.out.println("==================================================");
        System.out.println();

        // Direct Instance Instantiations
        Square square = new Square(10.0, "red");
        Circle circle = new Circle(7.0, "blue");
        Cylinder cylinder = new Cylinder(15.0, 7.0, "green");

        System.out.println("--- Direct Invocations ---");
        square.print_info();
        circle.print_info();
        cylinder.print_info();
        System.out.println();

        // Polymorphic Collection Traversal (Runtime Late Binding)
        System.out.println("--- Polymorphic Array Traversal ---");
        Shape[] shape_roster = {
            new Square(5.0, "yellow"),
            new Circle(4.0, "cyan"),
            new Cylinder(10.0, 4.0, "magenta")
        };

        for(int i = 0; i < shape_roster.length; i++){
            System.out.print("[" + i + "] ");
            shape_roster[i].print_info();
        }

        System.out.println("==================================================");
    }
}
