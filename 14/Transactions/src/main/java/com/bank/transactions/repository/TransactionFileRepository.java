package com.bank.transactions.repository;

import com.bank.transactions.domain.Transaction;
import com.bank.transactions.domain.TransactionProperties;
import com.bank.transactions.exception.TransactionParsingException;
import com.bank.transactions.exception.TransactionRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
public class TransactionFileRepository implements TransactionRepository {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionFileRepository.class);

    private final String fileName;

    public TransactionFileRepository(TransactionProperties properties) {
        this.fileName = properties.getStorageName();
        createFileStorage();
    }

    private void createFileStorage() {
        try {
            if (new File(fileName).createNewFile()) {
                LOG.info("New file with name={} is created", fileName);
            }
        } catch (IOException e) {
            throw new TransactionRepositoryException("Cannot create file storage", e);
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        updateTransactions(List.of(transaction));
    }

    @Override
    public void updateTransactions(List<Transaction> transactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            synchronized (fileName) {
                for (Transaction transaction : transactions) {
                    writer.write(transaction.toString());
                    writer.newLine();
                }
            }
            writer.flush();
        } catch (IOException e) {
            throw new TransactionRepositoryException("Cannot update transactions", e);
        }
    }

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactions;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            transactions = reader.lines()
                    .map(this::parseTransaction)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            throw new TransactionRepositoryException("Cannot read transaction from file", e);
        }
        return transactions;
    }

    private Transaction parseTransaction(String str) {
        try {
            return Transaction.fromString(str);
        } catch (TransactionParsingException e) {
            LOG.error("Error while parsing transaction", e);
        }
        return null;
    }
}