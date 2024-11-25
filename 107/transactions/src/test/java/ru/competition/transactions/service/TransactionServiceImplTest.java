package ru.competition.transactions.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import ru.competition.transactions.model.enums.ProcessorType;
import ru.competition.transactions.model.enums.TransactionStatus;
import ru.competition.transactions.repository.TransactionRepositoryFactory;
import ru.competition.transactions.service.impl.TransactionServiceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.competition.transactions.utils.TransactionTestUtils.generateTransaction;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "transactions.large-amount=10000")
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepositoryFactory repositoryFactory;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldNotThrowFactoryException() {
        var transaction = generateTransaction(TransactionStatus.PENDING);

        when(repositoryFactory.getTransactionRepository(any())).thenThrow(IllegalArgumentException.class);

        assertDoesNotThrow(() -> transactionService.processTransaction(transaction, ProcessorType.CONCURRENT));
    }
}
