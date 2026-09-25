import java.util.Scanner;

/**
 * Bank_Demo - Integrated System and Coursework Driver Harness.
 * Validates Bank, Customer, and Account relationships as specified in Assignment 04.
 * Features automated regression verification and an interactive ATM console interface.
 */
public class Bank_Demo {

    public static void main(String[] args){
        Bank bank = initialize_default_bank();

        if(args.length > 0 && (args[0].equals("-i") || args[0].equals("--interactive") || args[0].equals("menu"))){
            run_interactive_atm(bank);
        }else{
            run_automated_demo(bank);
            System.out.println("Tip: Launch with '-i' or '--interactive' flag to enter interactive ATM mode.");
        }
    }

    private static Bank initialize_default_bank(){
        Bank bank = new Bank(10);

        // Add Customers
        bank.add_customer("Jane", "Simms");
        bank.add_customer("Owen", "Bryant");
        bank.add_customer("Tim", "Soju");
        bank.add_customer("Maria", "Solev");

        // Assign Accounts
        bank.get_customer(0).set_account(new Account(500.00));
        bank.get_customer(1).set_account(new Account(200.00));
        bank.get_customer(2).set_account(new Account(1500.00));
        bank.get_customer(3).set_account(new Account(250.00));

        // Multi-account capability
        bank.get_customer(0).set_account(new Account(1200.00));

        return bank;
    }

    private static void run_automated_demo(Bank bank){
        System.out.println("==================================================");
        System.out.println("          BANK ACCOUNT MANAGEMENT SYSTEM          ");
        System.out.println("==================================================");
        System.out.println();

        System.out.println(">>> Registering bank customers...");
        System.out.println("Total customers registered: " + bank.get_num_of_customers());
        System.out.println();

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
        print_customer_report(bank);
    }

    public static void print_customer_report(Bank bank){
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

    private static void run_interactive_atm(Bank bank){
        Scanner scanner = new Scanner(System.in);
        boolean active = true;

        System.out.println("==================================================");
        System.out.println("            INTERACTIVE BANK ATM CONSOLE          ");
        System.out.println("==================================================");

        while(active){
            System.out.println("\nSelect an Operation:");
            System.out.println("1. View All Customers & Accounts");
            System.out.println("2. Deposit to Account");
            System.out.println("3. Withdraw from Account");
            System.out.println("4. Register New Customer");
            System.out.println("5. Open New Account for Customer");
            System.out.println("0. Exit ATM");
            System.out.print("Enter choice: ");

            if(!scanner.hasNextInt()){
                break;
            }
            int choice = scanner.nextInt();

            if(choice == 0){
                active = false;
                System.out.println("Thank you for using Bank ATM. Goodbye.");
            }else if(choice == 1){
                print_customer_report(bank);
            }else if(choice == 2){
                System.out.print("Enter Customer Index (1 - " + bank.get_num_of_customers() + "): ");
                int c_idx = scanner.nextInt() - 1;
                Customer cust = bank.get_customer(c_idx);
                if(cust != null && cust.get_num_of_accounts() > 0){
                    System.out.print("Enter Account Slot (1 - " + cust.get_num_of_accounts() + "): ");
                    int a_idx = scanner.nextInt() - 1;
                    Account acct = cust.get_account(a_idx);
                    if(acct != null){
                        System.out.print("Enter deposit amount: $");
                        double amount = scanner.nextDouble();
                        boolean ok = acct.deposit(amount);
                        System.out.println("Deposit result: " + (ok ? "SUCCESS" : "FAILED") + " | New Balance: $" + acct.get_balance());
                    }
                }
            }else if(choice == 3){
                System.out.print("Enter Customer Index (1 - " + bank.get_num_of_customers() + "): ");
                int c_idx = scanner.nextInt() - 1;
                Customer cust = bank.get_customer(c_idx);
                if(cust != null && cust.get_num_of_accounts() > 0){
                    System.out.print("Enter Account Slot (1 - " + cust.get_num_of_accounts() + "): ");
                    int a_idx = scanner.nextInt() - 1;
                    Account acct = cust.get_account(a_idx);
                    if(acct != null){
                        System.out.print("Enter withdrawal amount: $");
                        double amount = scanner.nextDouble();
                        boolean ok = acct.withdraw(amount);
                        System.out.println("Withdrawal result: " + (ok ? "SUCCESS" : "FAILED (Insufficient funds or invalid amount)") + " | Balance: $" + acct.get_balance());
                    }
                }
            }else if(choice == 4){
                System.out.print("Enter First Name: ");
                String first = scanner.next();
                System.out.print("Enter Last Name: ");
                String last = scanner.next();
                bank.add_customer(first, last);
                System.out.println("Customer registered: " + first + " " + last);
            }else if(choice == 5){
                System.out.print("Enter Customer Index (1 - " + bank.get_num_of_customers() + "): ");
                int c_idx = scanner.nextInt() - 1;
                Customer cust = bank.get_customer(c_idx);
                if(cust != null){
                    System.out.print("Enter initial opening balance: $");
                    double init_bal = scanner.nextDouble();
                    cust.set_account(new Account(init_bal));
                    System.out.println("New account opened with initial balance $" + init_bal);
                }
            }
        }
    }
}
