package com.example.sbertech.service;

import com.example.sbertech.dao.TransactionRepository;
import com.example.sbertech.log.Logger;
import com.example.sbertech.pojo.Status;
import com.example.sbertech.pojo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Junior service. Remained for profiling goal. To delete after.
 */
@Service("TransactionServiceJunior")
public class TransactionServiceJunior implements TransactionService {

    private final TransactionRepository repository;
    private final Logger logger;

    @Autowired
    public TransactionServiceJunior(@Qualifier("TransactionRepositoryJunior") TransactionRepository repository,
                                    Logger logger) {
        this.repository = repository;
        this.logger = logger;
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAmountInPenny() > LARGE_TRANSACTION_STARTS_FROM_AMOUNT_IN_PENNY) {
                logger.log("Processing large transaction: " + transaction.getId());
            }
            processTransaction(transaction);
        }
    }

    @Override
    public void processTransaction(Transaction transaction) {
        try {
            if (transaction.isPending()) {
                transaction.setStatus(Status.PROCESSED);
                repository.updateTransaction(transaction);
            }
        } catch (Exception e) {
            logger.log("Error processing transaction junior: " + e.getMessage());
        }
    }

    @Override
    public int countTransactions() {
        return repository.countTransactions();
    }
}
