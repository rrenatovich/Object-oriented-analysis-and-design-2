package app;

import java.sql.*;

public class AccountRepository {
    private final Connection conn;

    public AccountRepository(Connection conn) {
        this.conn = conn;
    }

    public void createUSDAccount(String id, String owner, long initialBalance) throws SQLException {
        try (var stmt = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, id);
            stmt.setString(2, owner);
            stmt.setLong(3, initialBalance);
            stmt.setString(4, "USD");
            stmt.executeUpdate();
        }
    }

    public void createRUBAccount(String id, String owner, long initialBalance) throws SQLException {
        try (var stmt = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, id);
            stmt.setString(2, owner);
            stmt.setLong(3, initialBalance);
            stmt.setString(4, "RUB");
            stmt.executeUpdate();
        }
    }

    public Account getAccount(String id) throws SQLException {
        try (var stmt = conn.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
            stmt.setString(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("currency") == "USD") return new USDAccount(rs.getString("id"), rs.getString("owner"), rs.getLong("balance_amount"));
                if (rs.getString("currency") == "RUB") return new RUBAccount(rs.getString("id"), rs.getString("owner"), rs.getLong("balance_amount"));
                else throw new IllegalArgumentException("Undefined currency type");
            }
            return null;
        }
    }

    public void updateBalance(String id, long newBalance, String transferType) throws SQLException {
        try (var stmt = conn.prepareStatement("UPDATE accounts SET balance_amount = ?, balance_currency = ? WHERE id = ?")) {
            stmt.setLong(1, newBalance);
            stmt.setString(2, transferType);
            stmt.setString(3, id);
            stmt.executeUpdate();
        }
    }
}