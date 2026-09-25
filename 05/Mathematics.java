/**
 * Mathematics - Fundamental arithmetic operations engine demonstrating method overloading.
 * Assignment 05: Slides 71 & 76.
 */
public class Mathematics {

    // -- Two-Parameter Addition --
    public int add(int a, int b){
        return a + b;
    }

    public double add(double a, double b){
        return a + b;
    }

    // -- Two-Parameter Subtraction --
    public int subtract(int a, int b){
        return a - b;
    }

    public double subtract(double a, double b){
        return a - b;
    }

    // -- Two-Parameter Multiplication --
    public int multiply(int a, int b){
        return a * b;
    }

    public double multiply(double a, double b){
        return a * b;
    }

    // -- Two-Parameter Division --
    public int divide(int a, int b){
        if(b == 0){
            return 0;
        }
        return a / b;
    }

    public double divide(double a, double b){
        if(b == 0.0){
            return 0.0;
        }
        return a / b;
    }

    // -- Two-Parameter Modulus --
    public int modulus(int a, int b){
        if(b == 0){
            return 0;
        }
        return a % b;
    }

    public double modulus(double a, double b){
        if(b == 0.0){
            return 0.0;
        }
        return a % b;
    }
}
