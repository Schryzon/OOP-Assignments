/**
 * Circle - Planar circular geometry extending Shape.
 * Assignment 05: Slide 66.
 */
public class Circle extends Shape {

    public static final double PI = 3.141592653589793;
    protected double radius;

    public Circle(double radius, String color){
        super(color);
        this.radius = radius;
    }

    public double get_radius(){
        return this.radius;
    }

    public void set_radius(double radius){
        if(radius > 0){
            this.radius = radius;
        }
    }

    public double area(){
        return PI * this.radius * this.radius;
    }

    @Override
    public void print_info(){
        System.out.println("Circle " + this.color + ", area = " + area());
    }

    // Interoperability aliases
    public double getRadius(){
        return get_radius();
    }

    public void setRadius(double radius){
        set_radius(radius);
    }
}
