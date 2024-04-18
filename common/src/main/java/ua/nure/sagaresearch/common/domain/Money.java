package ua.nure.sagaresearch.common.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Money {

    public static final Money ZERO = new Money(0);

    private BigDecimal amount;

    public Money() {
    }

    public Money(int i) {
        this.amount = new BigDecimal(Objects.requireNonNull(i));
    }

    public Money(String s) {
        this.amount = new BigDecimal(Objects.requireNonNull(s));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

    public Money add(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money subtract(Money other) {
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(Money other) {
        return new Money(amount.multiply(other.amount));
    }

    public Money multiply(Long other) {
        return new Money(amount.multiply(BigDecimal.valueOf(other)));
    }
}