package com.example.sbertech;

import com.example.sbertech.pojo.Status;
import com.example.sbertech.pojo.Transaction;
import com.example.sbertech.pojo.TransactionJunior;
import com.example.sbertech.pojo.TransactionMine;
import com.example.sbertech.service.TransactionService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
public class TransactionsGenerator {

    protected static int PENDING_TRANSACTIONS_PERCENT = 90;
    protected static int ONE_HUNDRED_PERCENT = 100;
    protected static String ANY_DATE_STRING = "2023-01-01";

    public List<Transaction> getJuniorTransactions(int howMuchGenerateTransactions) {
        return IntStream.range(0, howMuchGenerateTransactions).
                mapToObj(i -> new TransactionJunior(Integer.toString(i),
                        generateRandomAmount(),
                        ANY_DATE_STRING,
                        generateStatus(i))).collect(Collectors.toList());
    }

    public List<Transaction> getMyTransactions(int howMuchGenerateTransactions) {
        LocalDateTime now = LocalDateTime.now();
        return IntStream.range(0, howMuchGenerateTransactions).
                mapToObj(i -> new TransactionMine(UUID.randomUUID(),
                        generateRandomAmount(),
                        now,
                        generateStatus(i))).collect(Collectors.toList());
    }

    public void assertAllTransactionsGeneratedByThisClassAreAdded(
            TransactionService service, int howMuchTransactionsWereGenerated) {
        assertEquals(howMuchTransactionsWereGenerated * PENDING_TRANSACTIONS_PERCENT / ONE_HUNDRED_PERCENT,
                service.countTransactions());
    }

    public List<Transaction> getFirstPart(long sizeOfCollectionForWarming, List<Transaction> transactions) {
        return transactions.stream().limit(sizeOfCollectionForWarming).collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Transaction> getLastPart(long sizeOfCollectionForWarming, List<Transaction> transactions) {
        return transactions.stream().skip(sizeOfCollectionForWarming).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Half of generated numbers is less than LARGE_TRANSACTION_STARTS_FROM_AMOUNT_IN_PENNY.
     * Half of generated numbers is more.
     *
     * @return random amount
     */
    private Long generateRandomAmount() {
        return Long.valueOf(
                2 * new Random().nextInt(TransactionService.LARGE_TRANSACTION_STARTS_FROM_AMOUNT_IN_PENNY));
    }

    private Status generateStatus(int sequenceNumber) {
        int nonPendingFrequencyPercent = ONE_HUNDRED_PERCENT / (ONE_HUNDRED_PERCENT - PENDING_TRANSACTIONS_PERCENT);
        Status status;
        if (sequenceNumber % (nonPendingFrequencyPercent * 2) == 0) { //в нашем случае — каждая двадцатая
            status = Status.PROCESSED;
        } else if (sequenceNumber % nonPendingFrequencyPercent == 0) { //в нашем случае — каждая нечетная десятая.
            // То есть 10я, 30я, 50я ...
            status = Status.COMPLETED;
        } else {
            status = Status.PENDING;
        }
        return status;
    }
}
