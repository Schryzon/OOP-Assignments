/**
 * Bank_Account - Lecture slide model for ArrayList demonstration.
 * Assignment 04: Slide 24.
 */
public class Bank_Account {

    private double balance;
    private int account_number;

    public Bank_Account(int account_number){
        this.balance = 0.0;
        this.account_number = account_number;
    }

    public void deposit(double amount){
        this.balance += amount;
    }

    public void withdraw(double amount){
        this.balance -= amount;
    }

    public int get_account_number(){
        return this.account_number;
    }

    public double get_balance(){
        return this.balance;
    }

    // Coursework interoperability aliases
    public int getAccountNumber(){
        return get_account_number();
    }

    public double getBalance(){
        return get_balance();
    }
}
