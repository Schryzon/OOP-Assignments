/**
 * Math_Demo - Demonstration driver for arithmetic method overloading.
 * Assignment 05: Slide 76.
 */
public class Math_Demo {

    public static void main(String[] args){
        System.out.println("==================================================");
        System.out.println("       METHOD OVERLOADING DEMONSTRATION           ");
        System.out.println("==================================================");
        System.out.println();

        Advanced_Mathematics math = new Advanced_Mathematics();

        // Slide 76 Required Test Suite
        System.out.println("--- Addition Overloading Suite ---");
        double sum_3_double = math.add(12.5, 28.7, 14.2);
        System.out.println("add(12.5, 28.7, 14.2) = " + sum_3_double);

        int sum_3_int = math.add(12, 28, 14);
        System.out.println("add(12, 28, 14)       = " + sum_3_int);

        int sum_2_int = math.add(23, 34);
        System.out.println("add(23, 34)           = " + sum_2_int);

        double sum_2_double = math.add(3.4, 4.9);
        System.out.println("add(3.4, 4.9)         = " + sum_2_double);
        System.out.println();

        // Comprehensive Overloading Checks for Remaining Operations
        System.out.println("--- Extended Operations Suite ---");
        System.out.println("subtract(50.0, 15.5, 4.5) = " + math.subtract(50.0, 15.5, 4.5));
        System.out.println("multiply(2.5, 4.0, 3.0)   = " + math.multiply(2.5, 4.0, 3.0));
        System.out.println("divide(100.0, 2.0, 5.0)   = " + math.divide(100.0, 2.0, 5.0));
        System.out.println("modulus(100, 15, 4)       = " + math.modulus(100, 15, 4));

        System.out.println("==================================================");
    }
}
