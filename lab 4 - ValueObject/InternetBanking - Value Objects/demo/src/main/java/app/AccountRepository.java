package app;

import java.sql.*;

public class AccountRepository {
    private final Connection conn;

    public AccountRepository(Connection conn) {
        this.conn = conn;
    }

    public void createAccount(String id, String owner, Money initialBalance) throws SQLException {
        try (var stmt = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, id);
            stmt.setString(2, owner);
            stmt.setLong(3, initialBalance.getAmount());
            stmt.setString(4, initialBalance.getCurrency());
            stmt.executeUpdate();
        }
    }

    public Account getAccount(String id) throws SQLException {
        try (var stmt = conn.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
            stmt.setString(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                var balance = new Money(rs.getLong("balance_amount"), rs.getString("balance_currency"));
                return new Account(rs.getString("id"), rs.getString("owner"), balance);
            }
            return null;
        }
    }

    public void updateBalance(String id, Money newBalance) throws SQLException {
        try (var stmt = conn.prepareStatement("UPDATE accounts SET balance_amount = ?, balance_currency = ? WHERE id = ?")) {
            stmt.setLong(1, newBalance.getAmount());
            stmt.setString(2, newBalance.getCurrency());
            stmt.setString(3, id);
            stmt.executeUpdate();
        }
    }
}