package com.bank.transactions;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class TransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void updateTransaction(Transaction transaction) {
        entityManager.merge(transaction);
    }

    public List<Transaction> getTransactions() {
        TypedQuery<Transaction> query = entityManager.createQuery("SELECT t FROM Transaction t", Transaction.class);
        return query.getResultList();
    }
}
