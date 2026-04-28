package app;

public class RUBAccount extends Account {
    public RUBAccount(String id, String owner, long initialBalance) {
        super(id, owner, initialBalance);
    }

    @Override
    public String getCurrency() {
        return "RUB";
    }
}
