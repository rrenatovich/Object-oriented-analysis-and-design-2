package app;

public abstract class Account {
    private final String id;
    private final String owner;
    private long balance;

    protected Account(String id, String owner, long initialBalance) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner cannot be blank");
        }
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.id = id;
        this.owner = owner;
        this.balance = initialBalance;
    }

    public abstract String getCurrency();

    public void deposit(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative");
        }
        this.balance += amount;
    }

    public void withdraw(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Withdraw amount cannot be negative");
        }
        if (amount > this.balance) {
            throw new IllegalArgumentException("Cannot withdraw more than available balance");
        }
        this.balance -= amount;
    }

    public long getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }
}

