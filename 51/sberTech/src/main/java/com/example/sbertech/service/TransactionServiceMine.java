package com.example.sbertech.service;

import com.example.sbertech.dao.TransactionRepository;
import com.example.sbertech.pojo.Status;
import com.example.sbertech.pojo.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Primary service. Thread-safe.
 */
@Service("TransactionServiceMine")
@Slf4j
public class TransactionServiceMine implements TransactionService {

    protected final TransactionRepository repository;

    public TransactionServiceMine(@Qualifier("TransactionRepositoryMine") TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void processTransactions(List<Transaction> transactions) {
        transactions.parallelStream().forEach(this::processTransaction);
    }

    @Override
    public void processTransaction(Transaction transaction) {
        try {
            if (transactionIsPendingStatus(transaction)) {
                synchronized (transaction) {
                    repository.updateTransaction(transaction);
                    transaction.setStatus(Status.PROCESSED);
                } //логирование убрала. Оно занимает время. А аналитик в любой момент сделать запрос на получение списка
                // нужных транзакций, не повлияющий на производительность добавления новых.
            }
        } catch (Exception e) {
            log.error("Error processing transaction mine: " + e.getMessage());
        }
    }

    private boolean transactionIsPendingStatus(Transaction transaction) {
        return transaction.isPending();
    }

    @Override
    public int countTransactions() {
        return repository.countTransactions();
    }
}
