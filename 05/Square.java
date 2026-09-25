/**
 * Square - Planar quadrilateral extending Shape.
 * Assignment 05: Slide 66.
 */
public class Square extends Shape {

    private double side;

    public Square(double side, String color){
        super(color);
        this.side = side;
    }

    public double get_side(){
        return this.side;
    }

    public void set_side(double side){
        if(side > 0){
            this.side = side;
        }
    }

    public double area(){
        return this.side * this.side;
    }

    @Override
    public void print_info(){
        System.out.println("Square colored " + this.color + ", area = " + area());
    }

    // Interoperability aliases
    public double getSide(){
        return get_side();
    }

    public void setSide(double side){
        set_side(side);
    }
}
