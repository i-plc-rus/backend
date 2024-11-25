package com.bank.transactions.repository;

import com.bank.transactions.BaseTest;
import com.bank.transactions.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionFileRepositoryTest extends BaseTest {

    @Test
    public void getTransactionTest() {
        String s = """
                1 123.123 1728388938966 1
                2 55.01 1728388938966 3
                3 665434.22344 1728388938966 2
                4 990 1728388938966 2""";
        try (FileWriter writer = new FileWriter(properties.getStorageName())) {
            writer.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).hasSize(4)
                .extracting(Transaction::getId).containsOnly("1", "2", "3", "4");
    }

    @Test
    public void getTransactionFromEmptyStorageTest() {
        List<Transaction> transactions = repository.getTransactions();
        assertThat(transactions).isEmpty();
    }

    @Test
    public void getTransactionFromCorruptedStorageTest() {
        String s = """
                1 123.123 1728388938966 1
                2 wrong data 1728388938966 3
                4 990 1728388938966 2""";
        try (FileWriter writer = new FileWriter(properties.getStorageName())) {
            writer.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Transaction> transactions = repository.getTransactions();

        assertThat(transactions).hasSize(2)
                .extracting(Transaction::getId).containsOnly("1", "4");
    }

    @Test
    public void writeToStorageTest() {
        List<Transaction> transactions = createPendingTransactions(100_000);
        repository.updateTransactions(transactions);

        int size = repository.getTransactions().size();
        assertThat(size).isEqualTo(transactions.size());
    }

}