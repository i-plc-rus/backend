import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class TransactionRepository {
    private final ConcurrentHashMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

    public List<Transaction> getPendingTransactions() {
        return transactions.values().stream()
                .filter(transaction -> transaction.getStatus() == TransactionStatus.PENDING)
                .collect(Collectors.toList());
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public void addTransaction(Transaction transaction) {
        validateTransaction(transaction);
        transactions.put(transaction.getId(), transaction);
    }

    public void updateTransaction(Transaction updatedTransaction) {
        validateTransaction(updatedTransaction);
        transactions.computeIfPresent(updatedTransaction.getId(), (id, existingTransaction) -> updatedTransaction);
    }

    public void deleteTransaction(Long id) {
        transactions.remove(id);
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount() < 0) {
            throw new IllegalArgumentException("Transaction amount must be non-negative and not null");
        }
        if (transaction.getDate() == null) {
            throw new IllegalArgumentException("Transaction date cannot be null");
        }
        if (transaction.getStatus() == null) {
            throw new IllegalArgumentException("Transaction status cannot be null");
        }
    }
}
