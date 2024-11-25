package com.bank.transactions.v1;

import com.bank.common.utils.ThreadUtils;
import com.bank.transactions.Transaction;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public void updateTransaction(Transaction transaction) {
        ThreadUtils.sleep1Ms();
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
