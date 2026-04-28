package app;

import java.util.Objects;

public final class Money {
    private final long amount;
    private final String currency;

    public Money(long amount, String currency) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (currency == null || (!currency.equals("USD") && !currency.equals("RUB"))) {
            throw new IllegalArgumentException("Unsupported or null currency");
        }
        this.amount = amount;
        this.currency = currency;
    }

    public long getAmount() { return amount; }
    public String getCurrency() { return currency; }

    public boolean isSameCurrency(Money other) {
        return this.currency.equals(other.currency);
    }

    public Money addMoney(Money other) {
        if (!this.isSameCurrency(other)) {
            throw new IllegalArgumentException("Different types of currency");
        }
        return new Money(amount + other.getAmount(), currency);
    }

    public Money subtractMoney(Money other) {
        if (!this.isSameCurrency(other)) {
            throw new IllegalArgumentException("Different types of currency");
        }
        if (other.getAmount() > this.amount) {
            throw new IllegalArgumentException("Cannot subtract more than available");
        }
        return new Money(amount - other.getAmount(), currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount && Objects.equals(currency, money.currency);
    }
}
