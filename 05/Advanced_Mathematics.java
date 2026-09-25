/**
 * Advanced_Mathematics - Extended arithmetic engine overloading methods with 3 parameters.
 * Assignment 05: Slide 76.
 */
public class Advanced_Mathematics extends Mathematics {

    // -- Three-Parameter Addition --
    public int add(int a, int b, int c){
        return a + b + c;
    }

    public double add(double a, double b, double c){
        return a + b + c;
    }

    // -- Three-Parameter Subtraction --
    public int subtract(int a, int b, int c){
        return a - b - c;
    }

    public double subtract(double a, double b, double c){
        return a - b - c;
    }

    // -- Three-Parameter Multiplication --
    public int multiply(int a, int b, int c){
        return a * b * c;
    }

    public double multiply(double a, double b, double c){
        return a * b * c;
    }

    // -- Three-Parameter Division --
    public int divide(int a, int b, int c){
        if(b == 0 || c == 0){
            return 0;
        }
        return a / b / c;
    }

    public double divide(double a, double b, double c){
        if(b == 0.0 || c == 0.0){
            return 0.0;
        }
        return a / b / c;
    }

    // -- Three-Parameter Modulus --
    public int modulus(int a, int b, int c){
        if(b == 0 || c == 0){
            return 0;
        }
        return (a % b) % c;
    }

    public double modulus(double a, double b, double c){
        if(b == 0.0 || c == 0.0){
            return 0.0;
        }
        return (a % b) % c;
    }
}
