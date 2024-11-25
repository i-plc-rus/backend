package com.bank.transactions;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class TransactionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);

    private final TransactionRepository repository;

    @Autowired
    public TransactionProcessor(TransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void processTransactionList(@NotNull @Valid List<Transaction> transactions) {
        transactions.forEach(this::processTransaction);
    }

    private void processTransaction(Transaction transaction) {
        logIfLargeTransaction(transaction);
        processSingleTransaction(transaction);
    }

    @Transactional(propagation = Propagation.NESTED)
    private void processSingleTransaction(Transaction transaction) {
        if ("PENDING".equals(transaction.status())) {
            updateTransactionStatus(transaction, "PROCESSED");
        }
    }

    private void updateTransactionStatus(Transaction transaction, String newStatus) {
        transaction = new Transaction(transaction.id(), transaction.amount(), transaction.date(), newStatus);
        repository.save(transaction);
    }

    private void logIfLargeTransaction(Transaction transaction) {
        if (isLargeTransaction(transaction)) {
            logger.info("Processing large transaction: {}", transaction.id());
        }
    }

    private boolean isLargeTransaction(Transaction transaction) {
        return transaction.amount() > 10000;
    }
}