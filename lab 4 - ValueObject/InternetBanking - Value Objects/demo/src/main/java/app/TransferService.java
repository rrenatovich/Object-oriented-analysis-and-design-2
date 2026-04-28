package app;

import java.sql.Connection;
import java.sql.SQLException;

public class TransferService {
    private final AccountRepository repo;
    private final Connection conn;

    public TransferService(AccountRepository repo, Connection conn) {
        this.repo = repo;
        this.conn = conn;
    }

    public void transfer(String fromId, String toId, Money amount) throws SQLException {
        var from = repo.getAccount(fromId);
        var to = repo.getAccount(toId);
        if (from == null || to == null) {
            throw new RuntimeException("Account not found");
        }
        if (!from.getCurrency().equals(amount.getCurrency())) {
            throw new RuntimeException("Source account currency mismatch");
        }
        if (!to.getCurrency().equals(amount.getCurrency())) {
            throw new RuntimeException("Destination account currency mismatch");
        }
        from.withdraw(amount);
        to.deposit(amount);
        repo.updateBalance(fromId, from.getBalance());
        repo.updateBalance(toId, to.getBalance());
        // log transaction
        try (var stmt = conn.prepareStatement("INSERT INTO transactions (from_account_id, to_account_id, amount, currency) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, fromId);
            stmt.setString(2, toId);
            stmt.setLong(3, amount.getAmount());
            stmt.setString(4, amount.getCurrency());
            stmt.executeUpdate();
        }
    }
}
