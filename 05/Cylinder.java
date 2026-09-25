/**
 * Cylinder - Three-dimensional cylinder extending Circle.
 * Assignment 05: Slide 66.
 */
public class Cylinder extends Circle {

    private double height;

    public Cylinder(double height, double radius, String color){
        super(radius, color);
        this.height = height;
    }

    public double get_height(){
        return this.height;
    }

    public void set_height(double height){
        if(height > 0){
            this.height = height;
        }
    }

    public double volume(){
        // Reuse base area calculation inherited from Circle
        return area() * this.height;
    }

    @Override
    public void print_info(){
        System.out.println("Cylinder " + this.color + ", volume = " + volume());
    }

    // Interoperability aliases
    public double getHeight(){
        return get_height();
    }

    public void setHeight(double height){
        set_height(height);
    }
}
