
package com.bank.transactions.repository;

import com.bank.transactions.annotation.AmountAuditable;
import com.bank.transactions.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Класс используется только для тестирования.
 */

@Repository("ListBasedRepository")
public class ListBasedTransactionRepository implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactions.stream()
                .filter(transaction -> transaction.getId().equals(id))
                .findFirst();
    }

    @Override
    @AmountAuditable
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public int size() {
        return transactions.size();
    }
}
