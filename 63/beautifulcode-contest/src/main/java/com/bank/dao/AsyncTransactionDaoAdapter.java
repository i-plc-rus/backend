package com.bank.dao;

import com.bank.model.Transaction;
import com.bank.schema.TransactionRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface AsyncTransactionDaoAdapter {
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Future<Optional<Exception>> saveTransactions(List<TransactionRecord> transactions);
}
