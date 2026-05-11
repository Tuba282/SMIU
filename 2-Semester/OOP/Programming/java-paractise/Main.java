class BankAccount {
    // 1. Private variable (Encapsulation: data bahar se hide hai)
    private double balance;

    // Constructor to initialize balance
    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    // 2. Public method to deposit money
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        }
    }

    // 3. Public method to withdraw money (with logic check)
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance! Transaction failed.");
        }
    }

    // Method to get final balance
    public double getBalance() {
        return balance;
    }
}

public class Main {
    public static void main(String[] args) {
        // Object create karna
        BankAccount myAccount = new BankAccount(1000.0);

        // Operations perform karna
        myAccount.deposit(500.0);
        myAccount.withdraw(200.0);
        myAccount.withdraw(2000.0); // Yeh check karega insufficient balance ko

        // Final balance display karna
        System.out.println("Final Balance: " + myAccount.getBalance());
    }
}