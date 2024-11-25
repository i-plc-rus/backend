package com.bank.transactions.v2;

import static com.bank.transactions.v2.TransactionStatus.PENDING;
import static com.bank.transactions.v2.TransactionStatus.PROCESSED;

import com.bank.transactions.Transaction;
import java.util.List;

public class TransactionProcessor implements com.bank.transactions.TransactionProcessor {

    private final TransactionsProperties properties;
    private final TransactionRepository repository;
    private final Logger logger;

    public TransactionProcessor(TransactionsProperties properties,
                                TransactionRepository repository,
                                Logger logger) {
        this.properties = properties;
        this.repository = repository;
        this.logger = logger;
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        if (transactions != null) {
            new TransactionRecursiveAction(transactions.toArray(new Transaction[0]), this::processTransaction)
                .forkJoin();
        }
    }

    /**
     * Обрабатывает транзакцию
     *
     * @param transaction транзакция
     */
    public void processTransaction(Transaction transaction) {
        if (transaction != null) {
            if (hasLargeAmount(transaction)) {
                logger.log(this, "Processing large transaction: " + transaction.getId());
            }
            if (PENDING.equals(transaction.getStatus())) {
                repository.updateTransaction(new com.bank.transactions.v2.Transaction(transaction, PROCESSED));
            }
        }
    }

    /**
     * Определяет, является ли транзакция крупной
     *
     * @param transaction транзакция
     * @return true, если транзакция крупная
     */
    private boolean hasLargeAmount(Transaction transaction) {
        var largeAmount = properties.getLargeAmount();
        return transaction != null && transaction.getAmount() != null && largeAmount != null
            && transaction.getAmount().compareTo(largeAmount) > 0;
    }
}
