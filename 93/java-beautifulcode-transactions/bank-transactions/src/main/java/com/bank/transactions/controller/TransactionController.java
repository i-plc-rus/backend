package com.bank.transactions.controller;

import com.bank.transactions.model.Transaction;
import com.bank.transactions.service.TransactionProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@Profile("!Test")
public class TransactionController implements ApplicationRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final TransactionProcessor service;

    @Autowired
    public TransactionController(@Qualifier("MapBasedTransactionProcessor") TransactionProcessor service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        while (true) {
            try {
                printMenu();

                String command = scanner.nextLine();
                switch (command) {
                    case "1":
                        System.out.println("Введите сумму");
                        double amount = Double.parseDouble(scanner.nextLine());
                        service.addTransaction(amount);
                        break;
                    case "2":
                        System.out.println("Введите id");
                        service.processTransactions(List.of(Long.parseLong(scanner.nextLine())));
                        break;
                    case "3":
                        System.out.println("Введите id");
                        Transaction transaction = service.getTransactionById(Long.parseLong(scanner.nextLine()));
                        System.out.println(transaction);
                        break;
                    case "4":
                        System.out.println("Введите количество транзакций, которые вы хотите получить:");
                        String input = scanner.nextLine();
                        try {
                            int count = Integer.parseInt(input);
                            if (count <= 0) {
                                System.out.println("Количество должно быть положительным.");
                            } else {
                                List<Transaction> transactions = service.getAllTransactions(count);
                                if (transactions.isEmpty()) {
                                    System.out.println("Нет транзакций для отображения.");
                                } else {
                                    transactions.forEach(System.out::println);
                                }
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Пожалуйста, введите действительное число.");
                        }
                        break;
                    case "5":
                        return;
                }
            } catch (Exception e){
                System.out.println("Ошибочка вышла");
            }
        }
    }

    private static void printMenu() {
        System.out.println("Выберите команду:");
        System.out.println("1 - Добавить новую транзакцию");
        System.out.println("2 - Провести транзакцию");
        System.out.println("3 - Показать транзакцию");
        System.out.println("4 - Показать транзакции");
        System.out.println("5 - Выход");
    }
}
