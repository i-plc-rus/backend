package com.bank.transactions.service.impl.batch;

import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionBatchProcessor;
import com.bank.transactions.service.TransactionProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

@Service
@ConditionalOnProperty(prefix = "transaction.processor", name = "type", havingValue = "SIMPLE")
public class SimpleTransactionBatchConcurrentProcessorImpl implements TransactionBatchProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTransactionBatchConcurrentProcessorImpl.class);

    private final TransactionProcessor transactionProcessor;

    public SimpleTransactionBatchConcurrentProcessorImpl(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @Override
    public void processTransactions(Collection<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            logger.warn("Transaction batch processing is skipped cause collection is empty");
            return;
        }
        transactions.forEach(transactionProcessor::processTransaction);
    }
}
