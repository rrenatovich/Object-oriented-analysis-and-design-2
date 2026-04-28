package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Connect to database
        Connection conn = DriverManager.getConnection("jdbc:sqlite:database.db");

        // Create tables if not exist
        try (var stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS accounts (id TEXT PRIMARY KEY, owner TEXT, balance_amount INTEGER, balance_currency TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, from_account_id TEXT, to_account_id TEXT, amount INTEGER, currency TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
        }

        // Initialize services
        AccountRepository repo = new AccountRepository(conn);
        TransferService transferService = new TransferService(repo, conn);

        // Start the server on port 8080
        var app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        })
            .get("/", ctx -> ctx.redirect("/index.html"))
            .post("/accounts", ctx -> {
                AccountRequest req = ctx.bodyAsClass(AccountRequest.class);
                Money initialBalance = new Money(req.initialAmount, req.currency);
                repo.createAccount(req.id, req.owner, initialBalance);
                ctx.status(201).result("Account created");
            })
            .post("/transfer", ctx -> {
                TransferRequest req = ctx.bodyAsClass(TransferRequest.class);
                Money amount = new Money(req.amount, req.currency);
                transferService.transfer(req.fromId, req.toId, amount);
                ctx.status(200).result("Transfer successful");
            })
            .get("/transactions", ctx -> {
                var transactions = new ArrayList<Transaction>();
                try (var stmt = conn.createStatement()) {
                    var rs = stmt.executeQuery("SELECT * FROM transactions ORDER BY timestamp DESC");
                    while (rs.next()) {
                        transactions.add(new Transaction(rs.getInt("id"), rs.getString("from_account_id"), rs.getString("to_account_id"), rs.getLong("amount"), rs.getString("currency"), rs.getString("timestamp")));
                    }
                }
                ctx.json(transactions);
            })
            .get("/accounts-list", ctx -> {
                var accounts = new ArrayList<AccountInfo>();
                try (var stmt = conn.createStatement()) {
                    var rs = stmt.executeQuery("SELECT id, owner, balance_amount, balance_currency FROM accounts ORDER BY id");
                    while (rs.next()) {
                        accounts.add(new AccountInfo(rs.getString("id"), rs.getString("owner"), rs.getLong("balance_amount"), rs.getString("balance_currency")));
                    }
                }
                ctx.json(accounts);
            })
            .delete("/wipe-data", ctx -> {
                try (var stmt = conn.createStatement()) {
                    stmt.execute("DELETE FROM transactions");
                    stmt.execute("DELETE FROM accounts");
                }
                ctx.status(200).result("All data wiped");
            })
            .start(8080);
    }

    public static class AccountRequest {
        public String id;
        public String owner;
        public long initialAmount;
        public String currency;
    }

    public static class TransferRequest {
        public String fromId;
        public String toId;
        public long amount;
        public String currency;
    }

    public static class Transaction {
        public int id;
        public String fromAccountId;
        public String toAccountId;
        public long amount;
        public String currency;
        public String timestamp;

        public Transaction(int id, String from, String to, long amt, String cur, String ts) {
            this.id = id;
            this.fromAccountId = from;
            this.toAccountId = to;
            this.amount = amt;
            this.currency = cur;
            this.timestamp = ts;
        }
    }

    public static class AccountInfo {
        public String id;
        public String owner;
        public long balanceAmount;
        public String balanceCurrency;

        public AccountInfo(String id, String owner, long balanceAmount, String balanceCurrency) {
            this.id = id;
            this.owner = owner;
            this.balanceAmount = balanceAmount;
            this.balanceCurrency = balanceCurrency;
        }
    }
}