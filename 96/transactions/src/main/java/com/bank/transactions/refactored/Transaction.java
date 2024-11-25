package com.bank.transactions.refactored;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

/// Используем [Getter], [Setter], [ToString] и [AllArgsConstructor] из Lombok, чтобы минимизировать boilerplate-код
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Transaction {
    /// [Long] удобнее, чем [String] для хранения `id`
    private Long id;
    /// `long` надёжнее `double` в вопросе хранения денежных величин, не позволит хранить `null`-значение. (Лучше
    /// использовать специализированные библиотеки типа Moneta или Joda-Money)
    private long amount;
    /// [ZonedDateTime] надёжнее [String] для хранения даты транзакции. Также добавляет время с точностью до наносекунд
    /// и часовой пояс
    private ZonedDateTime date;
    /// Перечисление[TransactionStatus] надёжнее [String] для хранения и передачи статуса транзакции
    private TransactionStatus status;
}
