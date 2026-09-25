# Assignment 04: Arrays, ArrayLists, and Domain Aggregation

## Overview

This module covers fixed-size heap arrays, dynamic collections using `ArrayList<T>`, autoboxing/unboxing mechanisms, multidimensional matrices, and domain object modeling using aggregation and association.

---

## File Manifest

| File | Description |
| :--- | :--- |
| `Country_Capital.java` | 2D rectangular matrix pairing 7 nations with their capital cities using column-wise iteration. |
| `Bank_Account.java` | Individual bank account state model managing balance and account identification. |
| `Bank_Account_Array_In_Action.java` | Demonstration driver exercising dynamic `ArrayList` shifts, index insertions, and deletions. |
| `Account.java` | Core financial account entity with positive deposit validation and overdraft guard clauses. |
| `Customer.java` | Customer profile managing multiple financial accounts stored in a bounded fixed-size array (`Account[]`). |
| `Bank.java` | Aggregation root managing a directory of registered customer entities (`Customer[]`). |
| `Bank_Demo.java` | Integrated system validation harness testing customer creation, multi-account routing, deposits, overdraft rejections, and customer portfolio reports. |

---

## Architecture and Domain Model

```mermaid
classDiagram
    class Bank {
        -Customer[] customers
        -int number_of_customers
        +Bank()
        +Bank(int capacity)
        +add_customer(String first_name, String last_name) void
        +get_num_of_customers() int
        +get_customer(int index) Customer
    }

    class Customer {
        -String first_name
        -String last_name
        -Account[] accounts
        -int number_of_accounts
        +Customer(String first_name, String last_name)
        +get_first_name() String
        +get_last_name() String
        +set_account(Account acct) void
        +get_account(int index) Account
        +get_num_of_accounts() int
    }

    class Account {
        #double balance
        +Account(double balance)
        +get_balance() double
        +deposit(double amount) boolean
        +withdraw(double amount) boolean
    }

    Bank "1" *-- "0..*" Customer : aggregates
    Customer "1" o-- "0..5" Account : associates
```

---

## Compilation and Execution

```powershell
# Compile all sources in this directory
javac 04/*.java

# Run Country-Capital matrix exercise
java -cp 04 Country_Capital

# Run ArrayList dynamic operations harness
java -cp 04 Bank_Account_Array_In_Action

# Run core banking system simulation
java -cp 04 Bank_Demo
```
