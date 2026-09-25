import java.util.ArrayList;

/**
 * Bank_Account_Array_In_Action - Demonstration driver for ArrayList operations.
 * Assignment 04: Slide 25 (translated from BankAccountArrayBeraksi to English).
 */
public class Bank_Account_Array_In_Action {

    public static void main(String[] args){
        ArrayList<Bank_Account> accounts = new ArrayList<Bank_Account>();

        // Populate accounts
        accounts.add(new Bank_Account(1001));
        accounts.add(new Bank_Account(1015));
        accounts.add(new Bank_Account(1729));

        // Insert at index 1
        accounts.add(1, new Bank_Account(1008));

        // Remove element at index 0
        accounts.remove(0);

        System.out.println("Size: " + accounts.size());
        System.out.println("Expected: 3");

        Bank_Account first = accounts.get(0);
        System.out.println("First account number: " + first.get_account_number());
        System.out.println("Expected: 1008");

        Bank_Account last = accounts.get(accounts.size() - 1);
        System.out.println("Last account number: " + last.get_account_number());
        System.out.println("Expected: 1729");
    }
}
