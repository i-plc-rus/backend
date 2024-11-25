package org.example.catalogue.management.beautiful.code.afal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionProcessor {

    private final TransactionRepository repository;

    public void processTransactions(List<Transaction> transactions) {
        try {
            if (transactions == null || transactions.isEmpty()) {
                log.info("Список транзакций пуст - пропускаем обработку");
                return;
            }
            log.info("Обрабатываем список транзакций размера {}", transactions.size());

            List<Transaction> smallTransactions = new ArrayList<>();
            List<Transaction> largeTransactions = new ArrayList<>();
            for (Transaction transaction : transactions) {
                if (!transaction.isPending()) {
                    continue;
                }

                if (transaction.isLarge()) {
                    largeTransactions.add(transaction);
                } else {
                    smallTransactions.add(transaction);
                }
            }

            // TODO: потестить как оптимально распараллелить на реальной нагрузке
            log.debug(
                "Тяжелых транзакций {}. Легких транзакций {}", largeTransactions.size(), smallTransactions.size());
            if (largeTransactions.size() > 5) {
                smallTransactions.forEach(this::processTransaction);
                largeTransactions.parallelStream().forEach(this::processTransaction);
            } else {
                smallTransactions.addAll(largeTransactions);
                smallTransactions.forEach(this::processTransaction);
            }

            log.info("Обработка транзакций завершена");
        } catch (Exception e) {
            log.error("Не удалось полностью обработать список транзакций - исключение", e);
        }
    }

    private void processTransaction(Transaction transaction) {
        try {
            repository.updateTransaction(transaction.makeProcessed());
        } catch (Exception e) {
            log.error(String.format("Не удалось обработать транзакцию %s", transaction.id()), e);
        }
    }
}
