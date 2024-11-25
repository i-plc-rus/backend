package com.bank.transactions.controller;

import com.bank.transactions.service.TransactionProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class TransactionController {
    private final TransactionProcessor transactionProcessor;

    public TransactionController(@Qualifier("MapBasedTransactionProcessor") TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    // ...
}
