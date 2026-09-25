/**
 * Bank - Aggregation root managing customer directory.
 * Assignment 04: Slides 30-31.
 */
public class Bank {

    private Customer[] customers;
    private int number_of_customers;

    private static final int DEFAULT_CUSTOMER_CAPACITY = 10;

    public Bank(){
        this(DEFAULT_CUSTOMER_CAPACITY);
    }

    public Bank(int capacity){
        int initial_capacity = Math.max(capacity, 10);
        this.customers = new Customer[initial_capacity];
        this.number_of_customers = 0;
    }

    public void add_customer(String first_name, String last_name){
        if(this.number_of_customers >= this.customers.length){
            // array is full, no resizing supported on fixed array
            return;
        }

        Customer customer = new Customer(first_name, last_name);
        this.customers[this.number_of_customers] = customer;
        this.number_of_customers++;
    }

    public int get_num_of_customers(){
        return this.number_of_customers;
    }

    public Customer get_customer(int index){
        if(index < 0 || index >= this.number_of_customers){
            return null;
        }
        return this.customers[index];
    }

    // Coursework interoperability aliases
    public void addCustomer(String first_name, String last_name){
        add_customer(first_name, last_name);
    }

    public int getNumOfCustomers(){
        return get_num_of_customers();
    }

    public Customer getCustomer(int index){
        return get_customer(index);
    }
}
