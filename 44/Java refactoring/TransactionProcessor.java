import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;

import java.util.List;

@Component
public class TransactionProcessor {

    private static final Logger logger = LoggerFactory.getLogger(TransactionProcessor.class);
    private static final int BATCH_SIZE = 50;

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionProcessor(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void processTransactions() {
        try {
            List<Transaction> transactions = transactionRepository.getPendingTransactions();
            for (int i = 0; i < transactions.size(); i += BATCH_SIZE) {
                int end = Math.min(i + BATCH_SIZE, transactions.size());
                List<Transaction> batch = transactions.subList(i, end);
                batch.forEach(this::processTransactionWithRetry);
            }
        } catch (Exception e) {
            logger.error("Error while processing transactions", e);
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    private void processTransactionWithRetry(Transaction transaction) {
        processTransaction(transaction);
    }

    private void processTransaction(Transaction transaction) {
        try {
            logger.info("Processing transaction: {}", transaction.getId());
            completeTransaction(transaction);
        } catch (SpecificTransactionException e) {
            handleFailedTransaction(transaction, e);
        } catch (Exception e) {
            handleUnexpectedException(transaction, e);
        }
    }

    private void completeTransaction(Transaction transaction) throws SpecificTransactionException {
        // Business logic to complete the transaction
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.updateTransaction(transaction);
        logger.info("Transaction processed successfully: {}", transaction.getId());
    }

    private void handleFailedTransaction(Transaction transaction, SpecificTransactionException e) {
        logger.warn("Specific error occurred while processing transaction: {}, reason: {}", transaction.getId(), e.getMessage());
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.updateTransaction(transaction);
        logger.warn("Transaction marked as FAILED: {}", transaction.getId());
    }

    private void handleUnexpectedException(Transaction transaction, Exception e) {
        logger.error("Unexpected error while processing transaction: {}", transaction.getId(), e);
        transaction.setStatus(TransactionStatus.FAILED);
        transactionRepository.updateTransaction(transaction);
        logger.warn("Transaction marked as FAILED due to unexpected error: {}", transaction.getId());
    }
}
