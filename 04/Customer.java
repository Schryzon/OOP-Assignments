/**
 * Customer - Customer entity maintaining an array of financial accounts.
 * Assignment 04: Slides 26-29.
 */
public class Customer {

    private String first_name;
    private String last_name;
    private Account[] accounts;
    private int number_of_accounts;

    private static final int DEFAULT_CAPACITY = 5;

    public Customer(String first_name, String last_name){
        this.first_name = first_name;
        this.last_name = last_name;
        this.accounts = new Account[DEFAULT_CAPACITY];
        this.number_of_accounts = 0;
    }

    public String get_first_name(){
        return this.first_name;
    }

    public String get_last_name(){
        return this.last_name;
    }

    public void set_account(Account acct){
        // slot account into fixed array if capacity permits
        if(this.number_of_accounts >= this.accounts.length){
            return;
        }

        this.accounts[this.number_of_accounts] = acct;
        this.number_of_accounts++;
    }

    public Account get_account(int account_index){
        // boundary guard
        if(account_index < 0 || account_index >= this.number_of_accounts){
            return null;
        }
        return this.accounts[account_index];
    }

    public Account get_account(){
        return get_account(0);
    }

    public int get_num_of_accounts(){
        return this.number_of_accounts;
    }

    // Coursework interoperability aliases
    public String getFirstName(){
        return get_first_name();
    }

    public String getLastName(){
        return get_last_name();
    }

    public void setAccount(Account acct){
        set_account(acct);
    }

    public Account getAccount(int account_index){
        return get_account(account_index);
    }

    public int getNumOfAccounts(){
        return get_num_of_accounts();
    }
}
