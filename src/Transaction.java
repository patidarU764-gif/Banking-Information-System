import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String date;
    private String type;
    private double amount;
    private double balanceAfterTransaction;

    public Transaction(String type, double amount, double balanceAfterTransaction) {
        // Automatically captures the current date and time of the transaction
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = dtf.format(LocalDateTime.now());
        this.type = type;
        this.amount = amount;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    public String getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfterTransaction() { return balanceAfterTransaction; }
}
