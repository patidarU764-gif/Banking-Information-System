import java.util.ArrayList;

public class UserAccount {
    private String accountNumber;
    private String name;
    private String address;
    private String contact;
    private String password;
    private double balance;
    private ArrayList<Transaction> transactionHistory;

    public UserAccount(String accountNumber, String name, String address, String contact, String password, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.password = password;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();

        // Log the initial opening deposit record
        addTransaction("Initial Deposit", initialDeposit);
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public ArrayList<Transaction> getTransactionHistory() { return transactionHistory; }

    // Core Transactions Engine
    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposit", amount);
    }

    public boolean withdraw(double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Transaction failed: Amount must be greater than zero.");
        }
        if (amount > balance) {
            throw new Exception("Transaction failed: Insufficient account funds.");
        }
        balance -= amount;
        addTransaction("Withdrawal", amount);
        return true;
    }

    public void addTransaction(String type, double amount) {
        transactionHistory.add(new Transaction(type, amount, this.balance));
    }
}
