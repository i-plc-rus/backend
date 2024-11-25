package com.bank.service;

import com.bank.exception.ExceptionMessage;
import com.bank.schema.TransactionRecord;

import java.util.List;

public interface TransactionService {
    List<ExceptionMessage> processTransaction(List<TransactionRecord> transactions);
}
