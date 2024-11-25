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
import java.util.concurrent.ForkJoinPool;

@Service
@ConditionalOnProperty(prefix = "transaction.processor", name = "type", havingValue = "PARALLEL", matchIfMissing = true)
public class ConcurrentTransactionBatchProcessorImpl implements TransactionBatchProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ConcurrentTransactionBatchProcessorImpl.class);

    private final TransactionProcessor transactionProcessor;
    private final ForkJoinPool transactionProcessingThreadPool;

    public ConcurrentTransactionBatchProcessorImpl(TransactionProcessor transactionProcessor, ForkJoinPool transactionProcessingThreadPool) {
        this.transactionProcessor = transactionProcessor;
        this.transactionProcessingThreadPool = transactionProcessingThreadPool;
    }

    @Override
    public void processTransactions(Collection<Transaction> transactions) {
        if (CollectionUtils.isEmpty(transactions)) {
            logger.warn("Transaction batch processing is skipped cause collection is empty");
            return;
        }
        transactionProcessingThreadPool.execute(() ->
                transactions.parallelStream()
                        .forEach(transactionProcessor::processTransaction)
        );
    }
}
