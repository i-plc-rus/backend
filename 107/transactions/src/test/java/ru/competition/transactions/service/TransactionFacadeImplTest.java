package ru.competition.transactions.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.service.impl.TransactionFacadeImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.competition.transactions.utils.CommonConstants.INT_ZERO;
import static ru.competition.transactions.utils.TransactionTestUtils.generateTransactions;

@ExtendWith(MockitoExtension.class)
public class TransactionFacadeImplTest {
    @Mock
    private TransactionProcessorFactory processorFactory;
    @Mock
    private TransactionValidationService validationService;

    @InjectMocks
    private TransactionFacadeImpl transactionFacade;

    @Test
    void shouldNotThrowProcessorFactoryException() {
        var transactions = generateTransactions(INT_ZERO);

        when(processorFactory.getTransactionProcessor(any())).thenThrow(IllegalArgumentException.class);

        assertDoesNotThrow(() -> transactionFacade.process(transactions, ProcessorType.CONCURRENT));
    }
}
