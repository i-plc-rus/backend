package com.bank.processor;

import com.bank.exception.BatchSaveException;
import com.bank.schema.TransactionRecord;

import java.util.List;

public interface TransactionProcessor  {
    List<BatchSaveException> processTransactions(List<TransactionRecord> transactions);
}
