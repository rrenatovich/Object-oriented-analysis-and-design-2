package app;

public class USDAccount extends Account {
    public USDAccount(String id, String owner, long initialBalance) {
        super(id, owner, initialBalance);
    }

    @Override
    public String getCurrency() {
        return "USD";
    }
}
