
package com.bank.transactions.old;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OldTransactionRepository {

    private final List<OldTransaction> transactions = new ArrayList<>();

    public void updateTransaction(OldTransaction transactionOld) {
        transactions.add(transactionOld);
    }

    public List<OldTransaction> getTransactions() {
        return transactions;
    }
}
