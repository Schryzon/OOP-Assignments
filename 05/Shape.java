/**
 * Shape - Base geometric shape entity encapsulating color state.
 * Assignment 05: Slide 66.
 */
public class Shape {

    protected String color;

    public Shape(String color){
        this.color = color;
    }

    public String get_color(){
        return this.color;
    }

    public void set_color(String color){
        this.color = color;
    }

    public void print_info(){
        System.out.println("Shape colored " + this.color);
    }

    // Interoperability aliases
    public String getColor(){
        return get_color();
    }

    public void setColor(String color){
        set_color(color);
    }

    public void printInfo(){
        print_info();
    }
}
