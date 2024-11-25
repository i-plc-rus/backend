package before;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepository {

    // TODO Изменить тип данных для возможности поиска транзакции по id за константное время
    private final List<Transaction> transactions = new ArrayList<>();

    // TODO Добавить метод записи новой транзакции

    // TODO Изменить логику метода: вместо дополнения коллекции изменять статус имеющейся транзакции
    public void updateTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    // Возможно изменение содержимого transactions
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
