package com.bank.transactions;

import com.bank.transactions.service.TransactionProcessor;
import org.springframework.beans.factory.annotation.Qualifier;

class MapBasedTransactionProcessorTest extends AbstractTransactionProcessorTest {

    MapBasedTransactionProcessorTest(@Qualifier("MapBasedTransactionProcessor") TransactionProcessor service) {
        super(service);
    }
}
