package com.bank.transactions.v1;

import com.bank.transactions.Transaction;
import com.bank.common.utils.ThreadUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessor implements com.bank.transactions.TransactionProcessor {

    private final TransactionRepository repository;
    private final Logger logger;

    @Autowired
    public TransactionProcessor(TransactionRepository repository, Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 10000) {
                logger.log("Processing large transaction: " + transaction.getId());
            }
            processTransaction(transaction);
        }
    }

    private void processTransaction(Transaction transaction) {
        ThreadUtils.sleep10Ms(); // Немного приостановим поток, чтобы оценить производительность в многопоточной обработке
        try {
            if (transaction.getStatus().equals("PENDING")) {
                ((com.bank.transactions.v1.Transaction) transaction).setStatus("PROCESSED");
                repository.updateTransaction(transaction);
            }
        } catch (Exception e) {
            logger.log("Error processing transaction: " + e.getMessage());
        }
    }
}
