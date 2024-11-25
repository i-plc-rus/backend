import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionProcessor {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private Logger logger;

    public void processTransactions() {
        List<Transaction> transactions = transactionRepository.getAllTransactions();

        List<Transaction> processedTransactions = transactions.stream()
            .filter(this::isValidTransaction)
            .map(this::processTransaction)
            .collect(Collectors.toList());

        processedTransactions.forEach(transaction -> logger.log("Processed transaction: " + transaction.getId()));
    }

    private boolean isValidTransaction(Transaction transaction) {
        return transaction.getAmount() > 0;
    }

    private Transaction processTransaction(Transaction transaction) {
        transaction.setStatus("PROCESSED");
        return transaction;
    }
}
