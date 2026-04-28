package app;

public class Account {
    private final String id;
    private final String owner;
    private Money balance; // This is our Value Object

    public Account(String id, String owner, Money initialBalance) {
        this.id = id;
        this.owner = owner;
        this.balance = initialBalance;
    }

    public String getCurrency() {
        return balance.getCurrency();
    }

    public void deposit(Money amount) {
        this.balance = this.balance.addMoney(amount);
    }

    public void withdraw(Money amount) {
        this.balance = this.balance.subtractMoney(amount);
    }

    public Money getBalance() { return balance; }
    public String getId() { return id; }
}