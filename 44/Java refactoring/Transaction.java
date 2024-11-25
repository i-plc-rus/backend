import java.time.LocalDateTime;
import java.util.Objects;

public final class Transaction {
    private final Long id;
    private final Double amount;
    private final LocalDateTime date;
    private final TransactionStatus status;

    public Transaction(Long id, Double amount, LocalDateTime date, TransactionStatus status) {
        this.id = Objects.requireNonNull(id, "Transaction ID cannot be null");
        this.amount = Objects.requireNonNull(amount, "Transaction amount cannot be null");
        this.date = Objects.requireNonNull(date, "Transaction date cannot be null");
        this.status = Objects.requireNonNull(status, "Transaction status cannot be null");
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) &&
                amount.equals(that.amount) &&
                date.equals(that.date) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, date, status);
    }
}

enum TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED
}
