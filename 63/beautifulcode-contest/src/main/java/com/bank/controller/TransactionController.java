package com.bank.controller;

import com.bank.exception.ExceptionMessage;
import com.bank.schema.TransactionRecord;
import com.bank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки запросов на сохранения транзакций.
 */
@Validated
@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Метод, принимающий список транзакций для сохранения
     *
     * @param transactions - список транзакций.
     * @return список ошибок. При успешной обработке всего пакета будет пустым.
     */
    @PostMapping(path = "/transactions")
    public List<ExceptionMessage> saveTransactions(@RequestBody @Valid List<TransactionRecord> transactions) {
        return transactionService.processTransaction(transactions);
    }
}
