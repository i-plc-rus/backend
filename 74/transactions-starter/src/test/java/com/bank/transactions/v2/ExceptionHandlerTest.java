package com.bank.transactions.v2;

import static com.bank.transactions.v2.TransactionStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.bank.transactions.Transaction;
import com.bank.transactions.TransactionProcessor;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AutoConfiguration.class)
class ExceptionHandlerTest {

    private final static String EXCEPTION_MESSAGE = "exception_message";

    @Autowired
    private TransactionProcessor processor;
    @Autowired
    private ExceptionHandler exceptionHandler;
    @MockBean
    private TransactionRepository repository;
    @SpyBean
    private Logger logger;
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<Throwable> throwableCaptor;

    @BeforeEach
    void setUp() {
        Mockito.doThrow(new RuntimeException(EXCEPTION_MESSAGE))
            .when(repository).updateTransaction(any(Transaction.class));
    }

    @Test
    void testExceptionHandler() {
        processor.processTransactions(List.of(pendingTransaction()));

        verify(logger, times(1)).log(eq(exceptionHandler), stringCaptor.capture(), throwableCaptor.capture());
        assertThat(stringCaptor.getValue()).isEqualTo("Something went wrong: " + EXCEPTION_MESSAGE);
        assertThat(throwableCaptor.getValue().getClass()).isEqualTo(RuntimeException.class);
    }

    private Transaction pendingTransaction() {
        return new com.bank.transactions.v2.Transaction(null, null, null, PENDING);
    }
}
