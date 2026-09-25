/**
 * Account - Financial account entity encapsulating monetary balance.
 * Assignment 04: Slides 26-28.
 */
public class Account {

    protected double balance;

    public Account(double balance){
        this.balance = balance;
    }

    public double get_balance(){
        return this.balance;
    }

    public boolean deposit(double amount){
        // validate positive deposit amount
        if(amount <= 0){
            return false;
        }

        this.balance += amount;
        return true;
    }

    public boolean withdraw(double amount){
        // optimistic guard: deny if funds insufficient or amount non-positive
        if(amount > this.balance || amount <= 0){
            return false;
        }

        this.balance -= amount;
        return true;
    }

    // Coursework interoperability alias
    public double getBalance(){
        return get_balance();
    }
}
