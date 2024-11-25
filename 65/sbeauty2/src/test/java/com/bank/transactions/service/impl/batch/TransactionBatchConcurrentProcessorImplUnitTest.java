package com.bank.transactions.service.impl.batch;

import com.bank.transactions.data.model.Transaction;
import com.bank.transactions.service.TransactionBatchProcessor;
import com.bank.transactions.service.TransactionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import static com.bank.transactions.data.model.TransactionStatus.PENDING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionBatchConcurrentProcessorImplUnitTest {

    private final TransactionProcessor transactionProcessorMock = mock(TransactionProcessor.class);
    private final ForkJoinPool transactionProcessingThreadPoolMock = mock(ForkJoinPool.class);
    private TransactionBatchProcessor transactionBatchProcessor = new ConcurrentTransactionBatchProcessorImpl(
            transactionProcessorMock,
            transactionProcessingThreadPoolMock
    );

    @BeforeEach
    public void beforeEach() {
        doAnswer(answer -> {
            ((Runnable) answer.getArguments()[0]).run();
            return answer;
        }).when(transactionProcessingThreadPoolMock)
                .execute(any(Runnable.class));
    }


    @ParameterizedTest
    @NullSource
    @MethodSource("emptyCollections")
    void shouldNotProcessCallThreadPoolIfCollectionIsEmpty(List<Transaction> transactions) {
        transactionBatchProcessor.processTransactions(transactions);
        verify(transactionProcessingThreadPoolMock, never())
                .submit(any(Runnable.class));
        verify(transactionProcessorMock, never())
                .processTransaction(any());
    }

    @Test
    void shouldProcessCallThreadPool() {
        Transaction transaction1 = new Transaction("1", BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), PENDING);
        Transaction transaction2 = new Transaction("2", BigDecimal.valueOf(5000), LocalDate.parse("2023-01-01"), PENDING);
        List<Transaction> transactions = List.of(transaction1, transaction2);
        transactionBatchProcessor.processTransactions(transactions);
        verify(transactionProcessorMock, times(2))
                .processTransaction(any());
    }

    public static Stream<Arguments> emptyCollections() {
        return Stream.of(Arguments.of(List.of()));
    }
}
