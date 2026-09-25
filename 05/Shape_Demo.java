import java.util.Scanner;

/**
 * Shape_Demo - Driver harness for Shape inheritance hierarchy and polymorphic dispatch.
 * Assignment 05: Slide 66 & 91.
 * Features automated polymorphic verification and an interactive Shape console calculator.
 */
public class Shape_Demo {

    public static void main(String[] args){
        if(args.length > 0 && (args[0].equals("-i") || args[0].equals("--interactive") || args[0].equals("menu"))){
            run_interactive_inspector();
        }else{
            run_automated_demonstration();
            System.out.println("Tip: Launch with '-i' or '--interactive' flag to enter interactive Shape Inspector mode.");
        }
    }

    private static void run_automated_demonstration(){
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

    private static void run_interactive_inspector(){
        Scanner scanner = new Scanner(System.in);
        boolean active = true;

        System.out.println("==================================================");
        System.out.println("          INTERACTIVE SHAPE CALCULATOR            ");
        System.out.println("==================================================");

        while(active){
            System.out.println("\nSelect a Shape to Inspect:");
            System.out.println("1. Create Square (Calculates Area)");
            System.out.println("2. Create Circle (Calculates Area)");
            System.out.println("3. Create Cylinder (Calculates Volume)");
            System.out.println("4. Run Automated Polymorphic Array Traversal");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            if(!scanner.hasNextInt()){
                break;
            }
            int choice = scanner.nextInt();

            if(choice == 0){
                active = false;
                System.out.println("Exiting Shape Calculator. Goodbye.");
            }else if(choice == 1){
                System.out.print("Enter side length: ");
                double side = scanner.nextDouble();
                System.out.print("Enter color: ");
                String color = scanner.next();
                Square sq = new Square(side, color);
                System.out.print("Result: ");
                sq.print_info();
            }else if(choice == 2){
                System.out.print("Enter radius: ");
                double radius = scanner.nextDouble();
                System.out.print("Enter color: ");
                String color = scanner.next();
                Circle cr = new Circle(radius, color);
                System.out.print("Result: ");
                cr.print_info();
            }else if(choice == 3){
                System.out.print("Enter height: ");
                double height = scanner.nextDouble();
                System.out.print("Enter radius: ");
                double radius = scanner.nextDouble();
                System.out.print("Enter color: ");
                String color = scanner.next();
                Cylinder cyl = new Cylinder(height, radius, color);
                System.out.print("Result: ");
                cyl.print_info();
            }else if(choice == 4){
                run_automated_demonstration();
            }
        }
    }
}
