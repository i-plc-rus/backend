package com.bank.transactions.v2;

import static com.bank.transactions.v2.TransactionStatus.COMPLETED;
import static com.bank.transactions.v2.TransactionStatus.PENDING;
import static com.bank.transactions.v2.TransactionStatus.PROCESSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bank.transactions.Transaction;
import com.bank.transactions.TransactionProcessor;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AutoConfiguration.class)
public class TransactionProcessorTest {

    @Autowired
    private TransactionProcessor transactionProcessor;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
    }

    private List<Transaction> transactions() {
        return List.of(
            transaction("1", 5000.0, "2023-01-01", PENDING),
            transaction("2", 15000.0, "2023-01-02", PENDING),
            transaction("3", 2000.0, "2023-01-03", COMPLETED)
        );
    }

    private Transaction transaction(String id, Double amount, String date, String status) {
        return new com.bank.transactions.v2.Transaction(id, amount, date, status);
    }

    @Test
    void testProcessTransactions() {
        transactionProcessor.processTransactions(transactions());

        var actual = transactionRepository.getTransactions();

        assertThat(actual).extracting("id", "amount", "date", "status").containsExactly(
            tuple("1", 5000.0, "2023-01-01", PROCESSED),
            tuple("2", 15000.0, "2023-01-02", PROCESSED)
        );
    }

    @Test
    void testProcessTransactions_originTransactionsNotChanged() {
        var transactions = transactions();
        transactionProcessor.processTransactions(transactions);

        // после обработки, исходные транзакции должны остаться без изменений
        assertThat(transactions).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(transactions());
    }

    @Test
    void testProcessTransactions_whenNull() {
        transactionProcessor.processTransactions(null);
        var actual = transactionRepository.getTransactions();
        assertThat(actual).hasSize(0);
    }

    @Test
    void testGetTransactions_shouldBeUnmodifiable() {
        transactionProcessor.processTransactions(transactions());
        var actual = transactionRepository.getTransactions();
        assertThat(actual).hasSize(2);
        Throwable throwable = assertThrows(Throwable.class, () -> actual.remove(0));
        assertThat(throwable.getClass()).isEqualTo(UnsupportedOperationException.class);
    }

    @Test
    void testProcessTransactions_poorTransactions() {
        List<Transaction> poorTransactions = new ArrayList<>();
        poorTransactions.add(null);
        poorTransactions.addAll(List.of(
            transaction(null, null, null, null),
            transaction("ID", null, null, null),
            transaction(null, 0.0, null, null),
            transaction(null, null, "THIS_IS_NOT_DATE", null),
            transaction(null, null, null, "UNKNOWN_STATUS"),
            transaction(null, 123456.0, null, PENDING),
            transaction(null, 234567.0, null, PROCESSED),
            transaction(null, 345678.0, null, COMPLETED)
        ));

        transactionProcessor.processTransactions(poorTransactions);

        var actual = transactionRepository.getTransactions();
        assertThat(actual).extracting("id", "amount", "date", "status").containsExactly(
            tuple(null, 123456.0, null, PROCESSED)
        );
    }
}
