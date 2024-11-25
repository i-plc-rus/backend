
package com.bank.transactions.source_code;

import com.bank.transactions.mode.Transaction;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryOld {

    private final List<Transaction> transactions = new ArrayList<>();

    public void updateTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
