/**
 * Bank_Demo - Integrated System and Coursework Driver Harness.
 * Validates Bank, Customer, and Account relationships as specified in Assignment 04.
 */
public class Bank_Demo {

    public static void main(String[] args){
        System.out.println("==================================================");
        System.out.println("          BANK ACCOUNT MANAGEMENT SYSTEM          ");
        System.out.println("==================================================");
        System.out.println();

        // Initialize Bank
        Bank bank = new Bank(10);
        System.out.println(">>> Registering bank customers...");

        // Add Customers
        bank.add_customer("Jane", "Simms");
        bank.add_customer("Owen", "Bryant");
        bank.add_customer("Tim", "Soju");
        bank.add_customer("Maria", "Solev");

        System.out.println("Total customers registered: " + bank.get_num_of_customers());
        System.out.println();

        // Assign Accounts
        bank.get_customer(0).set_account(new Account(500.00));
        bank.get_customer(1).set_account(new Account(200.00));
        bank.get_customer(2).set_account(new Account(1500.00));
        bank.get_customer(3).set_account(new Account(250.00));

        // Customer 0 also receives a second savings account (multi-account capability)
        bank.get_customer(0).set_account(new Account(1200.00));

        // Perform Financial Operations
        System.out.println(">>> Executing transaction cycles...");

        // Customer 0: Jane Simms
        Customer jane = bank.get_customer(0);
        Account jane_primary = jane.get_account(0);
        System.out.println("Customer [" + jane.get_first_name() + " " + jane.get_last_name() + "]");
        System.out.println("  Primary Initial balance : $" + jane_primary.get_balance());
        jane_primary.withdraw(150.00);
        System.out.println("  Withdraw $150.00        : balance = $" + jane_primary.get_balance());
        jane_primary.deposit(22.50);
        System.out.println("  Deposit $22.50          : balance = $" + jane_primary.get_balance());
        System.out.println();

        // Customer 1: Owen Bryant (Test Overdraft Guard)
        Customer owen = bank.get_customer(1);
        Account owen_primary = owen.get_account(0);
        System.out.println("Customer [" + owen.get_first_name() + " " + owen.get_last_name() + "]");
        System.out.println("  Initial balance         : $" + owen_primary.get_balance());
        boolean withdraw_success = owen_primary.withdraw(300.00);
        System.out.println("  Withdraw $300 (Attempt) : success = " + withdraw_success + ", balance = $" + owen_primary.get_balance());
        owen_primary.deposit(100.00);
        System.out.println("  Deposit $100.00         : balance = $" + owen_primary.get_balance());
        System.out.println();

        // Generate Bank Customer Report via Array Iteration
        System.out.println("==================================================");
        System.out.println("               BANK CUSTOMER REPORT               ");
        System.out.println("==================================================");

        int total_customers = bank.get_num_of_customers();
        for(int i = 0; i < total_customers; i++){
            Customer customer = bank.get_customer(i);
            System.out.println("Customer [" + (i + 1) + "] : " + customer.get_last_name() + ", " + customer.get_first_name());

            int total_accounts = customer.get_num_of_accounts();
            for(int j = 0; j < total_accounts; j++){
                Account acct = customer.get_account(j);
                System.out.printf("  Account %d: Balance = $%.2f\n", (j + 1), acct.get_balance());
            }
            System.out.println("--------------------------------------------------");
        }
    }
}
